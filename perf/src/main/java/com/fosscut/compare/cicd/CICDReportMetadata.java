package com.fosscut.compare.cicd;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class CICDReportMetadata {

    private Duration externalTotalDuration;
    private Duration internalTotalDuration;
    private Duration internalAverageDuration;
    private Duration internalMedianDuration;
    private Duration internalStandardDeviationDuration;
    private Duration internalShortestDuration;
    private Duration internalLongestDuration;
    private Instant internalEarliestCreationTimestamp;
    private Instant internalLatestCompletionTimestamp;

    public CICDReportMetadata() {}

    public CICDReportMetadata(List<CICDReportLine> reportLines, Instant startTimestamp) {
        this.internalTotalDuration = calculateTotalDuration(reportLines);
        sortReportLinesByPart(reportLines);

        List<Duration> durations = reportLines.stream().map(line -> line.duration).toList();
        this.internalAverageDuration = calculateAverageDuration(durations);
        this.internalMedianDuration = calculateMedianDuration(durations);
        this.internalStandardDeviationDuration = calculateStandardDeviationDuration(durations);
        this.internalShortestDuration = calculateShortestDuration(durations);
        this.internalLongestDuration = calculateLongestDuration(durations);
        this.internalEarliestCreationTimestamp = getEarliestCreationTimestamp(reportLines);
        this.internalLatestCompletionTimestamp = getLatestCompletionTimestamp(reportLines);

        this.externalTotalDuration = Duration.between(
            startTimestamp, this.internalLatestCompletionTimestamp
        );
    }

    public Duration getExternalTotalDuration() {
        return externalTotalDuration;
    }

    public Duration getInternalTotalDuration() {
        return internalTotalDuration;
    }

    public Duration getInternalAverageDuration() {
        return internalAverageDuration;
    }

    public Duration getInternalMedianDuration() {
        return internalMedianDuration;
    }

    public Duration getInternalStandardDeviationDuration() {
        return internalStandardDeviationDuration;
    }

    public Duration getInternalShortestDuration() {
        return internalShortestDuration;
    }

    public Duration getInternalLongestDuration() {
        return internalLongestDuration;
    }

    public Instant getInternalEarliestCreationTimestamp() {
        return internalEarliestCreationTimestamp;
    }

    public Instant getInternalLatestCompletionTimestamp() {
        return internalLatestCompletionTimestamp;
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
        return "# External Total Duration: " + externalTotalDuration + "\n" +
               "# Internal Total Duration: " + internalTotalDuration + "\n" +
               "# Internal Average Duration: " + internalAverageDuration + "\n" +
               "# Internal Median Duration: " + internalMedianDuration + "\n" +
               "# Internal Standard Deviation Duration: " + internalStandardDeviationDuration + "\n" +
               "# Internal Shortest Duration: " + internalShortestDuration + "\n" +
               "# Internal Longest Duration: " + internalLongestDuration + "\n" +
               "# Internal Earliest Creation Timestamp: " + internalEarliestCreationTimestamp + "\n" +
               "# Internal Latest Completion Timestamp: " + internalLatestCompletionTimestamp;
    }

}