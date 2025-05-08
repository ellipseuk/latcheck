package com.latcheck.model;

public class LatencyStats {
    private final double average;
    private final long min;
    private final long max;
    private final int successCount;
    private final int errorCount;

    public LatencyStats(double average, long min, long max, int successCount, int errorCount) {
        this.average = average;
        this.min = min;
        this.max = max;
        this.successCount = successCount;
        this.errorCount = errorCount;
    }

    public double getAverage() {
        return average;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public String toString() {
        return String.format("Avg: %.2f ms, Min: %d ms, Max: %d ms, Success: %d, Errors: %d",
                average, min, max, successCount, errorCount);
    }
}