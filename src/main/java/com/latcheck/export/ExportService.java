package com.latcheck.export;

import com.latcheck.model.LatencyResult;
import com.opencsv.CSVWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExportService {

    /**
     * Exports latency results to a CSV file.
     */
    public void exportToCSV(List<LatencyResult> results, Path path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            //
            csvWriter.writeNext(new String[]{"Request #", "Latency (ms)", "Success", "Error"});

            for (LatencyResult result : results) {
                csvWriter.writeNext(new String[]{
                        String.valueOf(result.getRequestNumber()),
                        String.valueOf(result.getLatencyMillis()),
                        String.valueOf(result.isSuccess()),
                        result.getErrorMessage() == null ? "" : result.getErrorMessage()
                });
            }
        }
    }

    /**
     * Exports latency results to a TXT file.
     */
    public void exportToTXT(List<LatencyResult> results, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (LatencyResult result : results) {
                writer.write(result.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Exports latency results to a JSON file.
     */
    public void exportToJSON(List<LatencyResult> results, Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), results);
    }
}
