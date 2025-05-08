package com.latcheck.model;

public class LatencyResult {
    private final int requestNumber;
    private final long latencyMillis;
    private final boolean success;
    private final String errorMessage;

    public LatencyResult(int requestNumber, long latencyMillis, boolean success, String errorMessage) {
        this.requestNumber = requestNumber;
        this.latencyMillis = latencyMillis;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public long getLatencyMillis() {
        return latencyMillis;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return String.format("Request #%d: %d ms (%s)%s",
                requestNumber, latencyMillis,
                success ? "success" : "failed",
                success ? "" : " - " + errorMessage);
    }
}