package com.latcheck.controller;

import com.latcheck.analyzer.ServiceTester;
import com.latcheck.export.ExportService;
import com.latcheck.model.LatencyResult;
import com.latcheck.model.LatencyStats;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.File;
import java.util.List;

public class MainController extends AbstractController {

    @FXML private TextField urlField;
    @FXML private TextField requestCountField;

    @FXML private ProgressIndicator loadingIndicator;

    @FXML private Label avgLabel;
    @FXML private Label minLabel;
    @FXML private Label maxLabel;
    @FXML private Label errorLabel;

    @FXML private LineChart<Number, Number> lineChart;
    @FXML private Button runButton;

    @FXML private HBox titleBar;

    private final ServiceTester tester = new ServiceTester();
    private final ExportService exporter = new ExportService();

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);

        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setLabel("Request");
        xAxis.setMinorTickVisible(false);

        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        yAxis.setLabel("ms");
        yAxis.setMinorTickVisible(false);

        avgLabel.setText("—");
        minLabel.setText("—");
        maxLabel.setText("—");
        errorLabel.setText("—");
    }

    @FXML
    private void onRunTest() {
        String url = urlField.getText();
        String countText = requestCountField.getText();

        if (!isValidInput(url, countText)) {
            showAlert("Input Error", "Enter valid URL and number between 1 and 1000", Alert.AlertType.WARNING);
            return;
        }

        int count = Integer.parseInt(countText);

        runButton.setDisable(true);
        loadingIndicator.setVisible(true);

        Task<List<LatencyResult>> task = new Task<>() {
            @Override
            protected List<LatencyResult> call() {
                return tester.runTest(url, count);
            }
        };

        task.setOnSucceeded(e -> {
            List<LatencyResult> results = task.getValue();
            updateChart(results);
            updateMetrics(results);
            runButton.setDisable(false);
            loadingIndicator.setVisible(false);
        });

        task.setOnFailed(e -> {
            showAlert("Test Failed", task.getException().getMessage(), Alert.AlertType.ERROR);
            runButton.setDisable(false);
            loadingIndicator.setVisible(false);
        });

        new Thread(task).start();
    }

    public boolean isValidInput(String url, String countText) {
        try {
            int count = Integer.parseInt(countText);
            if (count < 1 || count > 1000) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return url != null && !url.isBlank();
    }

    private void updateChart(List<LatencyResult> results) {
        lineChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Latency");

        for (LatencyResult r : results) {
            if (r.isSuccess()) {
                series.getData().add(new XYChart.Data<>(r.getRequestNumber(), r.getLatencyMillis()));
            }
        }
        lineChart.getData().add(series);
    }

    private void updateMetrics(List<LatencyResult> results) {
        LatencyStats stats = tester.calculateStats(results);
        avgLabel.setText(String.format("%.0f ms", stats.getAverage()));
        minLabel.setText(stats.getMin() + " ms");
        maxLabel.setText(stats.getMax() + " ms");
        errorLabel.setText(String.valueOf(stats.getErrorCount()));
    }

    @FXML
    private void onExportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try {
                exporter.exportToCSV(tester.getResults(), file);
                showAlert("Success", "File saved successfully.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Export Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onReset() {
        tester.deleteAllResults();
        lineChart.getData().clear();
        avgLabel.setText("—");
        minLabel.setText("—");
        maxLabel.setText("—");
        errorLabel.setText("—");
    }

    @FXML
    private void onCloseClicked() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onMinimizeClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}