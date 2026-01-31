package com.fosscut.plot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.shared.type.cutting.plan.PlanStatus;
import com.fosscut.shared.util.load.YamlLoader;
import com.fosscut.utils.PerformanceDefaults;
import com.fosscut.utils.ResultsFilesAfter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PlotData extends ResultsFilesAfter {

    private Map<String, List<OrderAndPlanPair>> orderAndPlanPairs;
    private LinkedList<String> additionalXAxisLabels;

    private class OrderAndPlanPair {
        // object type of a loaded order is Plan to also load metadata
        private Plan order;
        private Plan plan;

        private Integer run;
        private Integer seed;

        public OrderAndPlanPair(String name, Plan order, Plan plan) {
            this.order = order;
            this.plan = plan;
            extractRunAndSeedFromName(name);
        }

        public Plan getOrder() {
            return order;
        }

        public Plan getPlan() {
            return plan;
        }

        public Integer getRun() {
            return run;
        }

        public Integer getSeed() {
            return seed;
        }

        private void extractRunAndSeedFromName(String name) {
            String[] parts = name.split(PerformanceDefaults.RESULTS_RUN_PREFIX)[1]
                .split(PerformanceDefaults.RESULTS_SEED_PREFIX);
            this.run = parts.length > 0 ? Integer.parseInt(parts[0]) : null;
            this.seed = parts.length > 1 ? Integer.parseInt(parts[1]) : null;
        }
    }

    // xAxisLabel -> average value
    private Map<String, Double> averageElapsedTimeMilliseconds;
    private Map<String, Double> averageElapsedTimeSeconds;
    private Map<String, Double> averageInputCount;
    private Map<String, Double> averageOutputCount; // constant for an order
    private Map<String, Double> averageTotalWaste;
    private Map<String, Double> averagePercentageTrueWasteAboveOptimal;
    private Map<String, Double> averageBestPerSeedPercentageTrueWasteAboveOptimal;
    private Map<String, Double> averageTrueTotalWaste;
    private Map<String, Double> averageMinTrueTotalWaste;
    private Map<String, Double> averageMaxTrueTotalWaste;
    private Map<String, Double> averageTotalNeededInputLength;
    private Map<String, Double> averageTotalCost;
    private Map<String, Double> averageMemoryUsagePeakBytes;

    public PlotData(String folderPath) throws IOException {
        super(folderPath);
        prepareData();
    }

    public PlotData(String folderPath, LinkedList<String> additionalXAxisLabels) throws IOException {
        super(folderPath);
        this.additionalXAxisLabels = additionalXAxisLabels;
        prepareData();
    }

    private void prepareData() throws IOException {
        loadOrderAndPlanPairs();
        removeXAxisLabelsWithMissingResults();
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

    public Map<String, Double> getAverageBestPerSeedPercentageTrueWasteAboveOptimal(Integer maxRunsPerSeed) {
        calculateBestPerSeedPercentageTrueWasteAboveOptimal(maxRunsPerSeed);
        return averageBestPerSeedPercentageTrueWasteAboveOptimal;
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

    public Map<String, Double> getAverageMemoryUsagePeakBytes() {
        return averageMemoryUsagePeakBytes;
    }

    public Map<String, Double> getAverageMemoryUsagePeakMebiBytes() {
        Map<String, Double> averageMemoryUsagePeakMebiBytes = new HashMap<>();
        for (String xAxisLabel : averageMemoryUsagePeakBytes.keySet()) {
            double bytes = averageMemoryUsagePeakBytes.get(xAxisLabel);
            double mebiBytes = bytes / (1024.0 * 1024.0);
            averageMemoryUsagePeakMebiBytes.put(xAxisLabel, mebiBytes);
        }
        return averageMemoryUsagePeakMebiBytes;
    }

    public Map<String, Double> getAverageMemoryUsagePeakGibiBytes() {
        Map<String, Double> averageMemoryUsagePeakGibiBytes = new HashMap<>();
        for (String xAxisLabel : averageMemoryUsagePeakBytes.keySet()) {
            double bytes = averageMemoryUsagePeakBytes.get(xAxisLabel);
            double gibiBytes = bytes / (1024.0 * 1024.0 * 1024.0);
            averageMemoryUsagePeakGibiBytes.put(xAxisLabel, gibiBytes);
        }
        return averageMemoryUsagePeakGibiBytes;
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
                        OrderAndPlanPair pair = new OrderAndPlanPair(orderName, order, plan);
                        orderAndPlanPairs.computeIfAbsent(xAxisLabel, k -> new ArrayList<>()).add(pair);
                    }
                }
            }
        }
    }

    private void removeXAxisLabelsWithMissingResults() {
        LinkedHashMap<String, List<File>> filteredPlanFilesList = new LinkedHashMap<>();

        Integer longestListLength = 0;
        for (String xAxisLabel : xAxisLabels) {
            filteredPlanFilesList.put(
                xAxisLabel,
                filterFilesByXAxisLabel(planFiles, xAxisLabel)
            );
            if (filteredPlanFilesList.get(xAxisLabel).size() > longestListLength) {
                longestListLength = filteredPlanFilesList.get(xAxisLabel).size();
            }
        }

        List<String> xAxisLabelsCopy = new ArrayList<>(xAxisLabels);
        for (String xAxisLabel : xAxisLabelsCopy) {
            if (filteredPlanFilesList.get(xAxisLabel).size() < longestListLength) {
                xAxisLabels.remove(xAxisLabel);
            }
        }

        if (additionalXAxisLabels != null) {
            for (String xAxisLabel : additionalXAxisLabels) {
                xAxisLabels.add(0, xAxisLabel);
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
        averageMemoryUsagePeakBytes = new HashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            List<OrderAndPlanPair> pairsForLabel = orderAndPlanPairs.get(xAxisLabel);

            double elapsedTimeSum = 0.0;
            Double inputCountSum = 0.0;
            Double outputCountSum = 0.0;
            Double totalWasteSum = 0.0;
            Double totalNeededInputLengthSum = 0.0;
            double memoryUsagePeakBytesSum = 0.0;

            for (OrderAndPlanPair pair : pairsForLabel) {
                try {
                    inputCountSum += pair.getPlan().getMetadata().getInputCount();
                    outputCountSum += pair.getPlan().getMetadata().getOutputCount();
                    totalWasteSum += pair.getPlan().getMetadata().getTotalWaste();
                    totalNeededInputLengthSum += pair.getPlan().getMetadata().getTotalNeededInputLength();
                } catch (NullPointerException e) {
                    // for plans that finished with a TIMEOUT metadata is missing
                    inputCountSum = null;
                    outputCountSum = null;
                    totalWasteSum = null;
                    totalNeededInputLengthSum = null;
                }
                elapsedTimeSum += pair.getPlan().getMetadata().getElapsedTimeMilliseconds();
                memoryUsagePeakBytesSum += pair.getPlan().getMetadata().getMemoryUsagePeakBytes();
            }

            Double localAverageInputCount = inputCountSum != null ? inputCountSum / pairsForLabel.size() : null;
            averageInputCount.put(xAxisLabel, localAverageInputCount);

            Double localAverageOutputCount = outputCountSum != null ? outputCountSum / pairsForLabel.size() : null;
            averageOutputCount.put(xAxisLabel, localAverageOutputCount);

            Double localAverageTotalWaste = totalWasteSum != null ? totalWasteSum / pairsForLabel.size() : null;
            averageTotalWaste.put(xAxisLabel, localAverageTotalWaste);

            Double localAverageTotalNeededInputLength = totalNeededInputLengthSum != null ? totalNeededInputLengthSum / pairsForLabel.size() : null;
            averageTotalNeededInputLength.put(xAxisLabel, localAverageTotalNeededInputLength);

            double localAverageElapsedTime = elapsedTimeSum / pairsForLabel.size();
            averageElapsedTimeMilliseconds.put(xAxisLabel, localAverageElapsedTime);

            double localAverageMemoryUsagePeakBytes = memoryUsagePeakBytesSum / pairsForLabel.size();
            averageMemoryUsagePeakBytes.put(xAxisLabel, localAverageMemoryUsagePeakBytes);
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

                if (isPlanTimeout(pair)) {
                    // skip plans that finished with TIMEOUT since their calculatedTotalNeededInputLength is null
                    continue;
                }

                if (optimalTotalNeededInputLength != null && optimalTotalNeededInputLength > 0) {
                    double percentageWaste = calculateTruePercentageWasteAboveOptimal(
                        optimalTotalNeededInputLength,
                        calculatedTotalNeededInputLength
                    );
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

    /*
     * First finds all pairs of order-plan for the same seed.
     * Then calculates percentage of true waste above optimal solution for all
     * of them (using  optimalTotalNeededInputLength from order metadata
     * and calculatedTotalNeededInputLength from plan metadata).
     * Then chooses one plan with the lowest percentage of true waste per seed.
     * Averages all those best plan values for each xAxis point.
     */
    private void calculateBestPerSeedPercentageTrueWasteAboveOptimal(Integer maxRunsPerSeed) {
        averageBestPerSeedPercentageTrueWasteAboveOptimal = new HashMap<>();
        boolean cutgenDetected = false;
        for (String xAxisLabel : xAxisLabels) {
            if (cutgenDetected) break;
            List<OrderAndPlanPair> pairsForLabel = orderAndPlanPairs.get(xAxisLabel);
            LinkedHashMap<Integer, LinkedList<OrderAndPlanPair>> seedOrderAndPlanPairMap
                = getSeedOrderAndPlanPairMap(pairsForLabel);

            List<Double> bestPercentageWastesPerSeed = new ArrayList<>();
            for (List<OrderAndPlanPair> pairsForSeed : seedOrderAndPlanPairMap.values()) {
                double bestPercentageWaste = Double.MAX_VALUE;
                boolean foundValidPair = false;

                for (int pairId = 0; pairId < pairsForSeed.size(); pairId++) {
                    if (maxRunsPerSeed != null && pairId >= maxRunsPerSeed) {
                        // limiting to only checking number:maxRunsPerSeed of available runs for one seed
                        // that way multiple graphs can be generated with different maxRunsPerSeed values
                        break;
                    }

                    OrderAndPlanPair pair = pairsForSeed.get(pairId);
                    Integer optimalTotalNeededInputLength = pair.getOrder().getMetadata().getTotalNeededInputLength();
                    Integer calculatedTotalNeededInputLength = pair.getPlan().getMetadata().getTotalNeededInputLength();

                    if (optimalTotalNeededInputLength != null && optimalTotalNeededInputLength > 0) {
                        double percentageWaste = calculateTruePercentageWasteAboveOptimal(
                            optimalTotalNeededInputLength,
                            calculatedTotalNeededInputLength
                        );
                        if (percentageWaste < bestPercentageWaste) {
                            bestPercentageWaste = percentageWaste;
                        }
                        foundValidPair = true;
                    } else {
                        cutgenDetected = true;
                        break;
                    }
                }

                if (foundValidPair) {
                    bestPercentageWastesPerSeed.add(bestPercentageWaste);
                }
            }

            double averageBestPerSeedPercentageWaste = bestPercentageWastesPerSeed.stream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(-200.0); // high negative value to indicate an error

            averageBestPerSeedPercentageTrueWasteAboveOptimal.put(xAxisLabel, averageBestPerSeedPercentageWaste);
        }
        if (cutgenDetected) averageBestPerSeedPercentageTrueWasteAboveOptimal = null;
    }

    private LinkedHashMap<Integer, LinkedList<OrderAndPlanPair>> getSeedOrderAndPlanPairMap(List<OrderAndPlanPair> pairsForLabel) {
        LinkedHashMap<Integer, LinkedList<OrderAndPlanPair>> seedOrderAndPlanPairMap = new LinkedHashMap<>();
        // collect pairs that used the same seed
        for (OrderAndPlanPair pair : pairsForLabel) {
            Integer seedKey = pair.getSeed();
            if (!seedOrderAndPlanPairMap.containsKey(seedKey)) {
                seedOrderAndPlanPairMap.put(seedKey, new LinkedList<>());
            }
            seedOrderAndPlanPairMap.get(seedKey).add(pair);
        }
        // each list of pairs needs to be sorted to always use the same sublist when limiting to maxRunsPerSeed
        for (LinkedList<OrderAndPlanPair> pairList : seedOrderAndPlanPairMap.values()) {
            pairList.sort((p1, p2) -> p1.getRun().compareTo(p2.getRun()));
        }
        return seedOrderAndPlanPairMap;
    }

    private double calculateTruePercentageWasteAboveOptimal(
        Integer optimalTotalNeededInputLength,
        Integer calculatedTotalNeededInputLength
    ) {
        return (double)
            (calculatedTotalNeededInputLength - optimalTotalNeededInputLength)
            * 100.0 / optimalTotalNeededInputLength;
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

                if (isPlanTimeout(pair)) {
                    // skip plans that finished with TIMEOUT since their calculatedTotalNeededInputLength is null
                    continue;
                }

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
                    .average().orElse(Double.NaN)
            );

            averageMinTrueTotalWaste.put(xAxisLabel,
                minTrueTotalWastes.stream().mapToDouble(Integer::doubleValue)
                    .average().orElse(Double.NaN)
            );

            averageMaxTrueTotalWaste.put(xAxisLabel,
                maxTrueTotalWastes.stream().mapToDouble(Integer::doubleValue)
                    .average().orElse(Double.NaN)
            );

            averageTotalCost.put(xAxisLabel,
                totalCosts.stream().mapToDouble(Double::doubleValue)
                    .average().orElse(Double.NaN)
            );
        }
    }

    private boolean isPlanTimeout(OrderAndPlanPair pair) {
        PlanStatus planStatus = pair.getPlan().getMetadata().getPlanStatus();
        return planStatus != null && planStatus == PlanStatus.TIMEOUT;
    }

}
