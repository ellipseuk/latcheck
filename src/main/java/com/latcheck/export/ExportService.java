package com.latcheck.export;

import com.latcheck.model.LatencyResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportService {

    public void exportToCSV(List<LatencyResult> results, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Request #,Latency (ms),Error Message\n");

            for (LatencyResult r : results) {
                String error = r.isSuccess() ? "" : r.getErrorMessage();
                writer.write(String.format(
                        "%d,%d,%s\n",
                        r.getRequestNumber(),
                        r.getLatencyMillis(),
                        escapeCsv(error)
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String escapeCsv(String input) {
        if (input == null || input.isEmpty()) return "";
        if (input.contains(",") || input.contains("\"")) {
            return "\"" + input.replace("\"", "\"\"") + "\"";
        }
        return input;
    }
}