package com.latcheck.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LatencyStatsTest {

    @Test
    void testFieldValues() {
        LatencyStats stats = new LatencyStats(123.45, 10, 300, 8, 2);

        assertEquals(123.45, stats.getAverage());
        assertEquals(10, stats.getMin());
        assertEquals(300, stats.getMax());
        assertEquals(8, stats.getSuccessCount());
        assertEquals(2, stats.getErrorCount());

        String expected = "Avg: 123.45 ms, Min: 10 ms, Max: 300 ms, Success: 8, Errors: 2";
        assertEquals(expected, stats.toString());
    }
}