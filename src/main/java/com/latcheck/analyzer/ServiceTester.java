package com.latcheck.analyzer;

import com.latcheck.model.LatencyResult;
import com.latcheck.model.LatencyStats;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServiceTester implements LatencyAnalyzer {

    private final HttpClient client;
    private final List<LatencyResult> results = new ArrayList<>();

    public ServiceTester() {
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public List<LatencyResult> runTest(String url, int count) {
        results.clear();
        for (int i = 1; i <= count; i++) {
            long start = System.nanoTime();
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

                long duration = (System.nanoTime() - start) / 1_000_000;
                results.add(new LatencyResult(i, duration, true, null));

            } catch (IOException | InterruptedException e) {
                long duration = (System.nanoTime() - start) / 1_000_000;
                results.add(new LatencyResult(i, duration, false, e.getMessage()));
            }
        }
        return new ArrayList<>(results);
    }

    @Override
    public LatencyStats calculateStats(List<LatencyResult> results) {
        List<Long> times = results.stream()
                .filter(LatencyResult::isSuccess)
                .map(LatencyResult::getLatencyMillis)
                .collect(Collectors.toList());

        double avg = times.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long min = times.stream().mapToLong(Long::longValue).min().orElse(0);
        long max = times.stream().mapToLong(Long::longValue).max().orElse(0);
        int success = (int) results.stream().filter(LatencyResult::isSuccess).count();
        int errors = results.size() - success;

        return new LatencyStats(avg, min, max, success, errors);
    }

    public List<LatencyResult> filterResults(Predicate<LatencyResult> predicate) {
        return results.stream().filter(predicate).collect(Collectors.toList());
    }

    public List<LatencyResult> sortResultsByLatency(boolean ascending) {
        return results.stream()
                .sorted(Comparator.comparingLong(LatencyResult::getLatencyMillis)
                        .reversed()
                        .reversed() // redundant, kept for clarity if extended
                        .thenComparingInt(LatencyResult::getRequestNumber))
                .collect(Collectors.toList());
    }

    public void deleteAllResults() {
        results.clear();
    }

    public List<LatencyResult> getResults() {
        return new ArrayList<>(results);
    }
}