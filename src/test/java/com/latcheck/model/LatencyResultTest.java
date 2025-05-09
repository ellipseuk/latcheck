package com.latcheck.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LatencyResultTest {

    @Test
    void testSuccessfulResultFields() {
        LatencyResult result = new LatencyResult(1, 123, true, null);

        assertEquals(1, result.getRequestNumber());
        assertEquals(123, result.getLatencyMillis());
        assertTrue(result.isSuccess());
        assertNull(result.getErrorMessage());

        assertEquals("Request #1: 123 ms (success)", result.toString());
    }

    @Test
    void testFailedResultFields() {
        LatencyResult result = new LatencyResult(2, 0, false, "Timeout");

        assertEquals(2, result.getRequestNumber());
        assertEquals(0, result.getLatencyMillis());
        assertFalse(result.isSuccess());
        assertEquals("Timeout", result.getErrorMessage());

        assertEquals("Request #2: 0 ms (failed) - Timeout", result.toString());
    }
}