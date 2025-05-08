package com.latcheck.analyzer;

import com.latcheck.model.LatencyResult;
import com.latcheck.model.LatencyStats;

import java.util.List;

public interface LatencyAnalyzer {
    /**
     * Performs a series of latency measurements.
     *
     * @param url   Target URL
     * @param count Number of requests
     * @return List of request results
     */
    List<LatencyResult> runTest(String url, int count);

    /**
     * Calculates statistics from a list of results.
     *
     * @param results List of results
     * @return Aggregated statistics
     */
    LatencyStats calculateStats(List<LatencyResult> results);
}