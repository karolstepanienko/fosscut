package com.fosscut.compare.cicd;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class CICDReportMetadata {

    private Duration totalDuration;
    private Duration averageDuration;
    private Duration medianDuration;
    private Duration standardDeviationDuration;
    private Duration shortestDuration;
    private Duration longestDuration;
    private Instant earliestCreationTimestamp;
    private Instant latestCompletionTimestamp;

    public CICDReportMetadata(List<CICDReportLine> reportLines) {
        this.totalDuration = calculateTotalDuration(reportLines);
        sortReportLinesByPart(reportLines);

        List<Duration> durations = reportLines.stream().map(line -> line.duration).toList();
        this.averageDuration = calculateAverageDuration(durations);
        this.medianDuration = calculateMedianDuration(durations);
        this.standardDeviationDuration = calculateStandardDeviationDuration(durations);
        this.shortestDuration = calculateShortestDuration(durations);
        this.longestDuration = calculateLongestDuration(durations);
        this.earliestCreationTimestamp = getEarliestCreationTimestamp(reportLines);
        this.latestCompletionTimestamp = getLatestCompletionTimestamp(reportLines);
    }

    private void sortReportLinesByPart(List<CICDReportLine> reportLines) {
        reportLines.sort((line1, line2) ->
            extractPartFromName(line1.name)
            .compareTo(extractPartFromName(line2.name))
        );
        Collections.reverse(reportLines);
    }

    private Integer extractPartFromName(String name) {
        String[] parts = name.split("-part-");
        if (parts.length < 2) {
            return null;
        }
        String partString = parts[1].split("_")[0];
        try {
            return Integer.parseInt(partString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Duration calculateTotalDuration(List<CICDReportLine> reportLines) {
        Instant earliestCreation = getEarliestCreationTimestamp(reportLines);
        Instant latestCompletion = getLatestCompletionTimestamp(reportLines);
        return Duration.between(earliestCreation, latestCompletion);
    }

    private Duration calculateAverageDuration(List<Duration> durations) {
        if (durations.isEmpty()) {
            return Duration.ZERO;
        }
        double averageMillis = durations.stream()
            .mapToLong(Duration::toMillis)
            .average().orElse(0L);
        return Duration.ofMillis((long) averageMillis);
    }

    private Duration calculateMedianDuration(List<Duration> durations) {
        if (durations.isEmpty()) {
            return Duration.ZERO;
        }

        List<Duration> sorted = durations.stream().sorted().toList();
        int n = sorted.size();
        if (n % 2 == 1) {
            return sorted.get(n / 2);
        } else {
            Duration d1 = sorted.get(n / 2 - 1);
            Duration d2 = sorted.get(n / 2);
            return d1.plus(d2).dividedBy(2);
        }
    }

    private Duration calculateStandardDeviationDuration(List<Duration> durations) {
        if (durations.isEmpty()) {
            return Duration.ZERO;
        }
        double averageMillis = durations.stream()
            .mapToLong(Duration::toMillis)
            .average().orElse(0L);
        double variance = durations.stream()
            .mapToDouble(d -> Math.pow(d.toMillis() - averageMillis, 2))
            .average().orElse(0L);
        double stdDevMillis = Math.sqrt(variance);
        return Duration.ofMillis((long) stdDevMillis);
    }

    private Duration calculateShortestDuration(List<Duration> durations) {
        Duration shortest = durations.stream()
            .min(Duration::compareTo)
            .orElse(Duration.ZERO);
        return shortest;
    }

    private Duration calculateLongestDuration(List<Duration> durations) {
        Duration longest = durations.stream()
            .max(Duration::compareTo)
            .orElse(Duration.ZERO);
        return longest;
    }

    private Instant getEarliestCreationTimestamp(List<CICDReportLine> reportLines) {
        return reportLines.stream()
            .map(line -> line.creationTimestamp)
            .min(Instant::compareTo)
            .orElse(null);
    }

    private Instant getLatestCompletionTimestamp(List<CICDReportLine> reportLines) {
        return reportLines.stream()
            .map(line -> line.completionTimestamp)
            .max(Instant::compareTo)
            .orElse(null);
    }

    @Override public String toString() {
        return "# Total Duration: " + totalDuration + "\n" +
               "# Average Duration: " + averageDuration + "\n" +
               "# Median Duration: " + medianDuration + "\n" +
               "# Standard Deviation Duration: " + standardDeviationDuration + "\n" +
               "# Shortest Duration: " + shortestDuration + "\n" +
               "# Longest Duration: " + longestDuration + "\n" +
               "# Earliest Creation Timestamp: " + earliestCreationTimestamp + "\n" +
               "# Latest Completion Timestamp: " + latestCompletionTimestamp;
    }

}