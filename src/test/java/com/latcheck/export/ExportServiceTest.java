package com.latcheck.export;

import com.latcheck.model.LatencyResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExportServiceTest {

    @Test
    void testExportToCSV_createsFileWithExpectedContent() throws Exception {
        List<LatencyResult> results = List.of(
                new LatencyResult(1, 100, true, null),
                new LatencyResult(2, 0, false, "timeout")
        );

        File tempFile = File.createTempFile("latcheck-test", ".csv");
        tempFile.deleteOnExit();

        ExportService exporter = new ExportService();

        exporter.exportToCSV(results, tempFile);

        List<String> lines = Files.readAllLines(tempFile.toPath());

        System.out.println("=== CSV OUTPUT ===");
        lines.forEach(System.out::println);

        assertEquals(3, lines.size());
        assertEquals("Request #,Latency (ms),Error Message", lines.get(0));
        assertEquals("1,100,", lines.get(1));
        assertEquals("2,0,timeout", lines.get(2));
    }
}