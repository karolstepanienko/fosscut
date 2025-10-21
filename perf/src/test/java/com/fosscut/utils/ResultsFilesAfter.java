package com.fosscut.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResultsFilesAfter extends ResultsFilesBefore {

    protected String testName;
    protected String resultsFolder;
    protected List<File> orderFiles;
    protected List<File> planFiles;
    protected List<String> xAxisLabels;

    public ResultsFilesAfter(String testName) {
        this.testName = testName;
        this.resultsFolder = PerformanceDefaults.RESULTS_PATH + testName;
        this.orderFiles = getOrderFiles();
        this.planFiles = getPlanFiles();
        this.xAxisLabels = collectXAxisLabels();
    }

    public List<String> getXAxisLabels() {
        return xAxisLabels;
    }

    private List<File> getOrderFiles() {
        return getFilesWithSuffix(PerformanceDefaults.RESULTS_ORDER_SUFFIX);
    }

    private List<File> getPlanFiles() {
        return getFilesWithSuffix(PerformanceDefaults.RESULTS_PLAN_SUFFIX);
    }

    private List<File> getFilesWithSuffix(String suffix) {
        File folder = new File(resultsFolder);
        System.out.println("Looking for files in folder: " + folder.getAbsolutePath());
        File[] files = folder.listFiles((dir, name) -> name.endsWith(suffix));
        return files != null ? Arrays.asList(files) : Collections.emptyList();
    }

    private List<String> collectXAxisLabels() {
        Set<String> xAxisLabelsSet = new HashSet<>();
        for (File file : planFiles) {
            String fileName = file.getName();
            String[] xParts = fileName.split("x");
            String label = xParts[1].split(PerformanceDefaults.RESULTS_RUN_PREFIX)[0];
            xAxisLabelsSet.add(label);
        }

        List<Double> numericLabels = new ArrayList<>();
        for (String label : xAxisLabelsSet) {
            numericLabels.add(Double.parseDouble(label));
        }

        Collections.sort(numericLabels);
        xAxisLabels = new ArrayList<>();
        for (int i = 0; i < numericLabels.size(); i++) {
            String sortedLabel = String.valueOf(numericLabels.get(i)).replaceAll("\\.0$", "");
            xAxisLabels.add(sortedLabel);
        }

        return xAxisLabels;
    }

}
