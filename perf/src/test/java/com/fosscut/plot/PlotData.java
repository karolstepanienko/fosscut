package com.fosscut.plot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.shared.util.load.YamlLoader;
import com.fosscut.utils.ResultsFilesAfter;

import java.util.ArrayList;
import java.util.HashMap;

public class PlotData extends ResultsFilesAfter {

    private Map<String, List<Plan>> plans;
    // orders - in the future could be used for gathering pattern statistics
    // for orders with known optimal solutions
    private Map<String, List<Order>> orders;

    // xAxisLabel -> average value
    private Map<String, Double> averageElapsedTimeMilliseconds;
    private Map<String, Double> averageElapsedTimeSeconds;
    private Map<String, Double> averageInputCount;
    private Map<String, Double> averageOutputCount; // constant for an order
    private Map<String, Double> averageTotalWaste;
    private Map<String, Double> averageTrueTotalWaste;
    private Map<String, Double> averageMinTrueTotalWaste;
    private Map<String, Double> averageMaxTrueTotalWaste;
    private Map<String, Double> averageTotalNeededInputLength;
    private Map<String, Double> averageTotalCost;

    public PlotData(String folderPath) throws IOException {
        super(folderPath);
        this.orders = loadObjectsToMap(Order.class);
        this.plans = loadObjectsToMap(Plan.class);
        calculateSimpleFieldAverages();
        calculateElapsedTimeSeconds();
        calculateAdvancedFieldAverages();
    }

    public Map<String, List<Order>> getOrders() {
        return this.orders;
    }

    public Map<String, Double> getAverageElapsedTimeMilliseconds() {
        return averageElapsedTimeMilliseconds;
    }

    public Map<String, Double> getAverageElapsedTimeSeconds() {
        return averageElapsedTimeSeconds;
    }

    public Map<String, Double> getAverageInputCount() {
        return averageInputCount;
    }

    public Map<String, Double> getAverageOutputCount() {
        return averageOutputCount;
    }

    public Map<String, Double> getAverageTotalWaste() {
        return averageTotalWaste;
    }

    public Map<String, Double> getAverageTrueTotalWaste() {
        return averageTrueTotalWaste;
    }

    public Map<String, Double> getAverageMinTrueTotalWaste() {
        return averageMinTrueTotalWaste;
    }

    public Map<String, Double> getAverageMaxTrueTotalWaste() {
        return averageMaxTrueTotalWaste;
    }

    public Map<String, Double> getAverageTotalNeededInputLength() {
        return averageTotalNeededInputLength;
    }

    public Map<String, Double> getAverageTotalCost() {
        return averageTotalCost;
    }

    private <T> Map<String, List<T>> loadObjectsToMap(Class<T> clazz) throws IOException {
        Map<String, List<T>> objects = new HashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            List<File> filteredFiles = filterFilesByXAxisLabel(planFiles, xAxisLabel);
            List<T> plansForLabel = loadObjectsFromFiles(filteredFiles, clazz);
            objects.put(xAxisLabel, plansForLabel);
        }
        return objects;
    }

    private List<File> filterFilesByXAxisLabel(List<File> files, String xAxisLabel) {
        List<File> filteredFiles = new ArrayList<>();
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.contains("x" + xAxisLabel)) {
                filteredFiles.add(file);
            }
        }
        return filteredFiles;
    }

    private <T> List<T> loadObjectsFromFiles(List<File> files, Class<T> clazz) throws IOException {
        List<T> objects = new ArrayList<>();
        YamlLoader yamlLoader = new YamlLoader();
        for (File file : files) {
            String fileContent = Files.readString(file.toPath());
            objects.add(yamlLoader.loadClassFromYamlString(fileContent, clazz));
        }
        return objects;
    }

    private void calculateSimpleFieldAverages() {
        averageElapsedTimeMilliseconds = new HashMap<>();
        averageInputCount = new HashMap<>();
        averageOutputCount = new HashMap<>();
        averageTotalWaste = new HashMap<>();
        averageTotalNeededInputLength = new HashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            List<Plan> plansForLabel = plans.get(xAxisLabel);

            double elapsedTimeSum = 0.0;
            double inputCountSum = 0.0;
            double outputCountSum = 0.0;
            double totalWasteSum = 0.0;
            double totalNeededInputLengthSum = 0.0;

            for (Plan plan : plansForLabel) {
                elapsedTimeSum += plan.getMetadata().getElapsedTimeMilliseconds();
                inputCountSum += plan.getMetadata().getInputCount();
                outputCountSum += plan.getMetadata().getOutputCount();
                totalWasteSum += plan.getMetadata().getTotalWaste();
                totalNeededInputLengthSum += plan.getMetadata().getTotalNeededInputLength();
            }

            double localAverageElapsedTime = elapsedTimeSum / plansForLabel.size();
            averageElapsedTimeMilliseconds.put(xAxisLabel, localAverageElapsedTime);

            double localAverageInputCount = inputCountSum / plansForLabel.size();
            averageInputCount.put(xAxisLabel, localAverageInputCount);

            double localAverageOutputCount = outputCountSum / plansForLabel.size();
            averageOutputCount.put(xAxisLabel, localAverageOutputCount);

            double localAverageTotalWaste = totalWasteSum / plansForLabel.size();
            averageTotalWaste.put(xAxisLabel, localAverageTotalWaste);

            double localAverageTotalNeededInputLength = totalNeededInputLengthSum / plansForLabel.size();
            averageTotalNeededInputLength.put(xAxisLabel, localAverageTotalNeededInputLength);
        }
    }

    private void calculateElapsedTimeSeconds() {
        averageElapsedTimeSeconds = new HashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            double elapsedTimeMilliseconds = averageElapsedTimeMilliseconds.get(xAxisLabel);
            double elapsedTimeSeconds = elapsedTimeMilliseconds / 1000.0;
            averageElapsedTimeSeconds.put(xAxisLabel, elapsedTimeSeconds);
        }
    }

    private void calculateAdvancedFieldAverages() {
        averageTrueTotalWaste = new HashMap<>();
        averageMinTrueTotalWaste = new HashMap<>();
        averageMaxTrueTotalWaste = new HashMap<>();
        averageTotalCost = new HashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            List<Plan> plansForLabel = plans.get(xAxisLabel);

            List<Integer> trueTotalWastes = new ArrayList<>();
            List<Integer> minTrueTotalWastes = new ArrayList<>();
            List<Integer> maxTrueTotalWastes = new ArrayList<>();
            List<Double> totalCosts = new ArrayList<>();

            for (Plan plan : plansForLabel) {
                Integer localTotalWaste = plan.getMetadata().getTotalWaste();
                Integer localTrueTotalWaste = plan.getMetadata().getTrueTotalWaste();

                trueTotalWastes.add(localTrueTotalWaste == null
                    ? localTotalWaste
                    : localTrueTotalWaste);

                if (plan.getMetadata().getMinTrueTotalWaste() != null) {
                    minTrueTotalWastes.add(plan.getMetadata().getMinTrueTotalWaste());
                } else if (localTrueTotalWaste != null) {
                    minTrueTotalWastes.add(localTrueTotalWaste);
                } else {
                    minTrueTotalWastes.add(localTotalWaste);
                }

                if (plan.getMetadata().getMaxTrueTotalWaste() != null) {
                    maxTrueTotalWastes.add(plan.getMetadata().getMaxTrueTotalWaste());
                } else if (localTrueTotalWaste != null) {
                    maxTrueTotalWastes.add(localTrueTotalWaste);
                } else {
                    maxTrueTotalWastes.add(localTotalWaste);
                }

                if (plan.getMetadata().getTotalCost() != null) {
                    totalCosts.add(plan.getMetadata().getTotalCost());
                }
            }

            averageTrueTotalWaste.put(xAxisLabel,
                trueTotalWastes.stream().mapToDouble(Integer::doubleValue)
                    .average().getAsDouble()
            );

            averageMinTrueTotalWaste.put(xAxisLabel,
                minTrueTotalWastes.stream().mapToDouble(Integer::doubleValue)
                    .average().getAsDouble()
            );

            averageMaxTrueTotalWaste.put(xAxisLabel,
                maxTrueTotalWastes.stream().mapToDouble(Integer::doubleValue)
                    .average().getAsDouble()
            );

            averageTotalCost.put(xAxisLabel,
                totalCosts.stream().mapToDouble(Double::doubleValue)
                    .average().orElse(0.0)
            );
        }
    }

}
