package com.fosscut.compare.cicd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fosscut.shared.util.load.YamlLoader;
import com.fosscut.utils.PerformanceDefaults;

public class CICDPlotData {

    private LinkedList<String> testNames;
    private LinkedList<String> xAxisLabelsList;
    private LinkedList<String> runIds;

    private LinkedList<Map<String, Double>> dataSeries;

    public CICDPlotData(
        LinkedList<String> testNames,
        LinkedList<String> runIds,
        LinkedList<String> xAxisLabelsList) throws IOException {
        this.testNames = testNames;
        this.runIds = runIds;
        this.xAxisLabelsList = xAxisLabelsList;
        this.dataSeries = new LinkedList<>();
        prepareData();
    }

    public LinkedList<Map<String, Double>> getDataSeries() {
        return dataSeries;
    }

    public LinkedList<LinkedList<String>> getCombinedXAxisLabelsList() {
        LinkedList<LinkedList<String>> combinedXAxisLabelsList = new LinkedList<>();
        combinedXAxisLabelsList.add(xAxisLabelsList);
        combinedXAxisLabelsList.add(xAxisLabelsList);
        combinedXAxisLabelsList.add(xAxisLabelsList);
        return combinedXAxisLabelsList;
    }

    private void prepareData() throws IOException {
        for (String testName : testNames) {
            dataSeries.add(new java.util.HashMap<String, Double>());
            for (String xAxisLabel : xAxisLabelsList) {
                List<Duration> durations = new ArrayList<Duration>();
                for (String runId : runIds) {
                    String reportFilePath = PerformanceDefaults.getResultsFolder(testName)
                        + System.getProperty("file.separator")
                        + "x" + xAxisLabel + "-" + runId + "-report.yaml";
                    CICDReport cicdReport = loadReportFromFile(reportFilePath);
                    durations.add(cicdReport.getMetadata().getExternalTotalDuration());
                }
                double averageExternalTotalDuration = durations.stream().mapToLong(Duration::toMillis).average().orElse(0);
                dataSeries.getLast().put(xAxisLabel, averageExternalTotalDuration / 1000); // in seconds
            }
        }
    }

    private CICDReport loadReportFromFile(String reportFilePath) throws IOException {
        String reportYamlString = Files.readString(Paths.get(reportFilePath));
        YamlLoader yamlLoader = new YamlLoader();
        return yamlLoader.loadClassFromYamlString(reportYamlString, CICDReport.class);
    }

}
