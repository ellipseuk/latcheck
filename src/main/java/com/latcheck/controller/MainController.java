package com.latcheck.controller;

import com.latcheck.analyzer.ServiceTester;
import com.latcheck.export.ExportService;
import com.latcheck.model.LatencyResult;
import com.latcheck.model.LatencyStats;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class MainController extends AbstractController {

    @FXML private TextField urlField;
    @FXML private TextField requestCountField;

    @FXML private Label avgLabel;
    @FXML private Label minLabel;
    @FXML private Label maxLabel;
    @FXML private Label errorLabel;

    @FXML private LineChart<Number, Number> latencyChart;

    private final ServiceTester tester = new ServiceTester();
    private final ExportService exporter = new ExportService();

    @FXML
    private void initialize() {
        latencyChart.setCreateSymbols(true);

        NumberAxis xAxis = (NumberAxis) latencyChart.getXAxis();
        xAxis.setLabel("Request");

        NumberAxis yAxis = (NumberAxis) latencyChart.getYAxis();
        yAxis.setLabel("ms");

        avgLabel.setText("—");
        minLabel.setText("—");
        maxLabel.setText("—");
        errorLabel.setText("—");
    }

    @FXML
    private void onRunTest() {
        String url = urlField.getText();
        int count;

        try {
            count = Integer.parseInt(requestCountField.getText());
            if (count < 1 || count > 1000) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Enter a number between 1 and 1000", Alert.AlertType.WARNING);
            return;
        }

        if (url == null || url.isBlank()) {
            showAlert("Input Error", "Enter a valid URL", Alert.AlertType.WARNING);
            return;
        }

        List<LatencyResult> results = tester.runTest(url, count);

        latencyChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Latency");

        for (LatencyResult r : results) {
            if (r.isSuccess()) {
                series.getData().add(new XYChart.Data<>(r.getRequestNumber(), r.getLatencyMillis()));
            }
        }
        latencyChart.getData().add(series);

        // Metrics
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
            try {
                exporter.exportToCSV(tester.getResults(), file.toPath());
                showAlert("Success", "File saved successfully.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Export Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void onReset() {
        tester.deleteAllResults();
        latencyChart.getData().clear();
        avgLabel.setText("");
        minLabel.setText("");
        maxLabel.setText("");
        errorLabel.setText("");
    }
}