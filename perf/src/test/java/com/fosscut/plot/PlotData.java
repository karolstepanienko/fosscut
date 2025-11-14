package com.fosscut.plot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.shared.util.load.YamlLoader;
import com.fosscut.utils.PerformanceDefaults;
import com.fosscut.utils.ResultsFilesAfter;

import java.util.ArrayList;
import java.util.HashMap;

public class PlotData extends ResultsFilesAfter {

    private Map<String, List<OrderAndPlanPair>> orderAndPlanPairs;

    private class OrderAndPlanPair {
        // object type of a loaded order is Plan to also load metadata
        private Plan order;
        private Plan plan;

        public OrderAndPlanPair(Plan order, Plan plan) {
            this.order = order;
            this.plan = plan;
        }

        public Plan getOrder() {
            return order;
        }

        public Plan getPlan() {
            return plan;
        }
    }

    // xAxisLabel -> average value
    private Map<String, Double> averageElapsedTimeMilliseconds;
    private Map<String, Double> averageElapsedTimeSeconds;
    private Map<String, Double> averageInputCount;
    private Map<String, Double> averageOutputCount; // constant for an order
    private Map<String, Double> averageTotalWaste;
    private Map<String, Double> averagePercentageTrueWasteAboveOptimal; // uses order's optimal totalNeededInputLength and plan's trueTotalWaste to calculate true waste percentage including unnecessary output elements
    private Map<String, Double> averageTrueTotalWaste;
    private Map<String, Double> averageMinTrueTotalWaste;
    private Map<String, Double> averageMaxTrueTotalWaste;
    private Map<String, Double> averageTotalNeededInputLength;
    private Map<String, Double> averageTotalCost;

    public PlotData(String folderPath) throws IOException {
        super(folderPath);
        loadOrderAndPlanPairs();
        calculateSimpleFieldAverages();
        calculateElapsedTimeSeconds();
        calculatePercentageTrueWasteAboveOptimal();
        calculateAdvancedFieldAverages();
    }

    public Map<String, List<OrderAndPlanPair>> getOrderAndPlanPairs() {
        return this.orderAndPlanPairs;
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

    public Map<String, Double> getAveragePercentageTrueWasteAboveOptimal() {
        return averagePercentageTrueWasteAboveOptimal;
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

    private void loadOrderAndPlanPairs() throws IOException {
        orderAndPlanPairs = new HashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            List<File> filteredOrderFiles = filterFilesByXAxisLabel(orderFiles, xAxisLabel);
            List<File> filteredPlanFiles = filterFilesByXAxisLabel(planFiles, xAxisLabel);

            for (File orderFile : filteredOrderFiles) {
                for (File planFile : filteredPlanFiles) {
                    String orderName = orderFile.getName().replace(PerformanceDefaults.RESULTS_ORDER_SUFFIX, "");
                    String planName = planFile.getName().replace(PerformanceDefaults.RESULTS_PLAN_SUFFIX, "");
                    if (orderName.equals(planName)) {
                        Plan order = loadObjectFromFile(orderFile, Plan.class);
                        Plan plan = loadObjectFromFile(planFile, Plan.class);
                        OrderAndPlanPair pair = new OrderAndPlanPair(order, plan);
                        orderAndPlanPairs.computeIfAbsent(xAxisLabel, k -> new ArrayList<>()).add(pair);
                    }
                }
            }
        }
    }

    private List<File> filterFilesByXAxisLabel(List<File> files, String xAxisLabel) {
        List<File> filteredFiles = new ArrayList<>();
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.contains("x" + xAxisLabel + "-run")) {
                filteredFiles.add(file);
            }
        }
        return filteredFiles;
    }

    private <T> T loadObjectFromFile(File file, Class<T> clazz) throws IOException {
        YamlLoader yamlLoader = new YamlLoader();
        String fileContent = Files.readString(file.toPath());
        return yamlLoader.loadClassFromYamlString(fileContent, clazz);
    }

    private void calculateSimpleFieldAverages() {
        averageElapsedTimeMilliseconds = new HashMap<>();
        averageInputCount = new HashMap<>();
        averageOutputCount = new HashMap<>();
        averageTotalWaste = new HashMap<>();
        averageTotalNeededInputLength = new HashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            List<OrderAndPlanPair> pairsForLabel = orderAndPlanPairs.get(xAxisLabel);

            double elapsedTimeSum = 0.0;
            double inputCountSum = 0.0;
            double outputCountSum = 0.0;
            double totalWasteSum = 0.0;
            double totalNeededInputLengthSum = 0.0;

            for (OrderAndPlanPair pair : pairsForLabel) {
                elapsedTimeSum += pair.getPlan().getMetadata().getElapsedTimeMilliseconds();
                inputCountSum += pair.getPlan().getMetadata().getInputCount();
                outputCountSum += pair.getPlan().getMetadata().getOutputCount();
                totalWasteSum += pair.getPlan().getMetadata().getTotalWaste();
                totalNeededInputLengthSum += pair.getPlan().getMetadata().getTotalNeededInputLength();
            }

            double localAverageElapsedTime = elapsedTimeSum / pairsForLabel.size();
            averageElapsedTimeMilliseconds.put(xAxisLabel, localAverageElapsedTime);

            double localAverageInputCount = inputCountSum / pairsForLabel.size();
            averageInputCount.put(xAxisLabel, localAverageInputCount);

            double localAverageOutputCount = outputCountSum / pairsForLabel.size();
            averageOutputCount.put(xAxisLabel, localAverageOutputCount);

            double localAverageTotalWaste = totalWasteSum / pairsForLabel.size();
            averageTotalWaste.put(xAxisLabel, localAverageTotalWaste);

            double localAverageTotalNeededInputLength = totalNeededInputLengthSum / pairsForLabel.size();
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

    /*
     * Uses optimalTotalNeededInputLength from order metadata
     * and calculatedTotalNeededInputLength from plan metadata
     * to calculate the percentage of true waste above optimal solution.
     * This includes unnecessary output elements in the waste calculation.
     */
    private void calculatePercentageTrueWasteAboveOptimal() {
        averagePercentageTrueWasteAboveOptimal = new HashMap<>();
        boolean cutgenDetected = false;
        for (String xAxisLabel : xAxisLabels) {
            if (cutgenDetected) break;

            List<OrderAndPlanPair> pairsForLabel = orderAndPlanPairs.get(xAxisLabel);

            List<Double> percentageWastes = new ArrayList<>();

            for (OrderAndPlanPair pair : pairsForLabel) {
                Integer optimalTotalNeededInputLength = pair.getOrder().getMetadata().getTotalNeededInputLength();
                Integer calculatedTotalNeededInputLength = pair.getPlan().getMetadata().getTotalNeededInputLength();

                if (optimalTotalNeededInputLength != null && optimalTotalNeededInputLength > 0) {
                    double percentageWaste = (double)
                        (calculatedTotalNeededInputLength - optimalTotalNeededInputLength)
                        * 100.0 / optimalTotalNeededInputLength;
                    percentageWastes.add(percentageWaste);
                } else {
                    cutgenDetected = true;
                    break;
                }
            }

            double averagePercentageWaste = percentageWastes.stream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(-200.0); // high negative value to indicate an error

            averagePercentageTrueWasteAboveOptimal.put(xAxisLabel, averagePercentageWaste);
        }
        // do not generate average percentage waste when optimal solution is not known
        if (cutgenDetected) averagePercentageTrueWasteAboveOptimal = null;
    }

    private void calculateAdvancedFieldAverages() {
        averageTrueTotalWaste = new HashMap<>();
        averageMinTrueTotalWaste = new HashMap<>();
        averageMaxTrueTotalWaste = new HashMap<>();
        averageTotalCost = new HashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            List<OrderAndPlanPair> pairsForLabel = orderAndPlanPairs.get(xAxisLabel);

            List<Integer> trueTotalWastes = new ArrayList<>();
            List<Integer> minTrueTotalWastes = new ArrayList<>();
            List<Integer> maxTrueTotalWastes = new ArrayList<>();
            List<Double> totalCosts = new ArrayList<>();

            for (OrderAndPlanPair pair : pairsForLabel) {
                Integer localTotalWaste = pair.getPlan().getMetadata().getTotalWaste();
                Integer localTrueTotalWaste = pair.getPlan().getMetadata().getTrueTotalWaste();

                trueTotalWastes.add(localTrueTotalWaste == null
                    ? localTotalWaste
                    : localTrueTotalWaste);

                if (pair.getPlan().getMetadata().getMinTrueTotalWaste() != null) {
                    minTrueTotalWastes.add(pair.getPlan().getMetadata().getMinTrueTotalWaste());
                } else if (localTrueTotalWaste != null) {
                    minTrueTotalWastes.add(localTrueTotalWaste);
                } else {
                    minTrueTotalWastes.add(localTotalWaste);
                }

                if (pair.getPlan().getMetadata().getMaxTrueTotalWaste() != null) {
                    maxTrueTotalWastes.add(pair.getPlan().getMetadata().getMaxTrueTotalWaste());
                } else if (localTrueTotalWaste != null) {
                    maxTrueTotalWastes.add(localTrueTotalWaste);
                } else {
                    maxTrueTotalWastes.add(localTotalWaste);
                }

                if (pair.getPlan().getMetadata().getTotalCost() != null) {
                    totalCosts.add(pair.getPlan().getMetadata().getTotalCost());
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
