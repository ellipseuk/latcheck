package com.latcheck.analyzer;

import com.latcheck.model.LatencyResult;
import com.latcheck.model.LatencyStats;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTesterTest {

    @Test
    void testCalculateStats() {
        List<LatencyResult> input = List.of(
                new LatencyResult(1, 100, true, null),
                new LatencyResult(2, 200, true, null),
                new LatencyResult(3, 150, true, null),
                new LatencyResult(4, 0, false, "error")
        );

        ServiceTester tester = new ServiceTester();
        LatencyStats stats = tester.calculateStats(input);

        assertEquals(3, stats.getSuccessCount());
        assertEquals(1, stats.getErrorCount());
        assertEquals(100, stats.getMin());
        assertEquals(200, stats.getMax());
        assertEquals(150.0, stats.getAverage());
    }
}