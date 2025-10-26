package com.fosscut.shared.type.cutting.plan;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fosscut.shared.type.cutting.order.OrderOutput;

public class Metadata {
    private Long elapsedTimeMilliseconds;
    private String timestamp;
    private Integer inputCount;
    private Integer outputCount;
    private Integer inputTypeCount;
    private Integer outputTypeCount;
    private List<UnnecessaryOutput> unnecessaryOutputs;
    private Integer totalWaste; // contains only obvious waste from patterns
    // trueTotalWaste = totalWaste + waste from unnecessary outputs
    private Integer trueTotalWaste; // randomly chosen unnecessary outputs since there is no relaxation
    private Integer minTrueTotalWaste; // chosen unnecessary outputs with highest relax values
    private Integer maxTrueTotalWaste; // chosen unnecessary outputs with lowest relax values
    private Integer totalNeededInputLength;
    private Double totalCost;
    private PlanStatus planStatus;

    public Metadata() {
        planStatus = PlanStatus.TIMEOUT;
    }

    public Metadata(Long elapsedTimeMilliseconds) {
        this.elapsedTimeMilliseconds = elapsedTimeMilliseconds;
    }

    public void calculateMetadata(List<PlanInput> inputs, List<OrderOutput> outputs) {
        calculateInputCount(inputs);
        calculateOutputCount(outputs);
        inputTypeCount = inputs.size();
        outputTypeCount = outputs.size();
        determineTimestamp();
        findUnnecessaryOutputs(inputs, outputs);
        calculateTotalWaste(inputs, outputs);
        if (unnecessaryOutputs != null) calculateTrueTotalWaste(inputs);
        calculateTotalNeededInputLength(inputs);
        calculateTotalCost(inputs);
        planStatus = PlanStatus.COMPLETE;
    }

    public Long getElapsedTimeMilliseconds() {
        return elapsedTimeMilliseconds;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getInputCount() {
        return inputCount;
    }

    public Integer getOutputCount() {
        return outputCount;
    }

    public Integer getInputTypeCount() {
        return inputTypeCount;
    }

    public Integer getOutputTypeCount() {
        return outputTypeCount;
    }

    public List<UnnecessaryOutput> getUnnecessaryOutputs() {
        return unnecessaryOutputs;
    }

    public Integer getTotalWaste() {
        return totalWaste;
    }

    public Integer getTrueTotalWaste() {
        return trueTotalWaste;
    }

    public Integer getMinTrueTotalWaste() {
        return minTrueTotalWaste;
    }

    public Integer getMaxTrueTotalWaste() {
        return maxTrueTotalWaste;
    }

    public Integer getTotalNeededInputLength() {
        return totalNeededInputLength;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public PlanStatus getPlanStatus() {
        return planStatus;
    }

    private void calculateInputCount(List<PlanInput> inputs) {
        inputCount = 0;
        for (PlanInput input : inputs) {
            for (Pattern pattern : input.getPatterns()) {
                inputCount += pattern.getCount();
            }
        }
    }

    private void calculateOutputCount(List<OrderOutput> outputs) {
        outputCount = 0;
        for (OrderOutput output : outputs) {
            outputCount += output.getCount();
        }
    }

    private void determineTimestamp() {
        if (this.elapsedTimeMilliseconds == null) return;
        this.timestamp = LocalDate.now().toString() + ":" + LocalTime.now();
    }

    private void findUnnecessaryOutputs(List<PlanInput> inputs, List<OrderOutput> outputs) {
        Map<Integer, Integer> outputCountMap = new HashMap<>();
        for (int outputId = 0; outputId < outputs.size(); outputId++) {
            outputCountMap.put(outputId, outputs.get(outputId).getCount());
        }

        for (PlanInput input : inputs) {
            for (Pattern pattern: input.getPatterns()) {
                for (PlanOutput output : pattern.getPatternDefinition()) {
                    Integer currentCount = outputCountMap.get(output.getId());
                    outputCountMap.put(output.getId(), currentCount - pattern.getCount() * output.getCount());
                }
            }
        }

        unnecessaryOutputs = new ArrayList<UnnecessaryOutput>();
        for (Integer outputId = 0; outputId < outputs.size(); outputId++) {
            if (outputCountMap.get(outputId) != 0) {
                OrderOutput unnecessaryOutput = outputs.get(outputId);
                unnecessaryOutputs.add(new UnnecessaryOutput(
                    outputId,
                    unnecessaryOutput.getLength(),
                    outputCountMap.get(outputId) * -1,
                    unnecessaryOutput.getMaxRelax()
                ));
            }
        }

        if (unnecessaryOutputs.isEmpty()) unnecessaryOutputs = null;
    }

    private void calculateTotalWaste(List<PlanInput> inputs, List<OrderOutput> outputs) {
        totalWaste = 0;
        for (PlanInput input : inputs) {
            for (Pattern pattern: input.getPatterns()) {
                totalWaste += pattern.getCount()
                    * (input.getLength() - pattern.getTotalOutputsLength(outputs));
            }
        }
    }

    private void calculateTrueTotalWaste(List<PlanInput> inputs) {
        minTrueTotalWaste = totalWaste;
        maxTrueTotalWaste = totalWaste;
        Map<Integer, List<PlanOutput>> outputElementsMap = new HashMap<>();

        boolean hasRelaxation = false;
        for (PlanInput input : inputs) {
            for (Pattern pattern: input.getPatterns()) {
                for (PlanOutput output : pattern.getPatternDefinition()) {
                    if (hasRelaxation == false && output.getRelax() != null && output.getRelax() > 0) {
                        hasRelaxation = true;
                    }
                    outputElementsMap
                        .computeIfAbsent(
                            output.getId(), k -> new ArrayList<PlanOutput>()
                        ).add(new PlanOutput(
                            output.getId(),
                            output.getCount() * pattern.getCount(),
                            output.getRelax()
                        ));
                }
            }
        }

        if (hasRelaxation) {
            // sort those lists based on relaxation value (highest first)
            for (List<PlanOutput> outputElements : outputElementsMap.values()) {
                outputElements.sort((o1, o2) -> o2.getRelax().compareTo(o1.getRelax()));
            }
        }

        for (UnnecessaryOutput unnecessaryOutput : unnecessaryOutputs) {
            List<PlanOutput> outputElements = outputElementsMap.get(unnecessaryOutput.getId());
            // create deep copies of those lists so that we can modify them
            List<PlanOutput> outputElementsMinRelax = getDeepCopy(outputElements);
            List<PlanOutput> outputElementsMaxRelax = getDeepCopy(outputElements);

            for (int i = 0; i < unnecessaryOutput.getCount(); i++) {
                // // choose element with highest relax value
                minTrueTotalWaste += getChosenUnnecessaryOutputLength(
                    outputElementsMaxRelax,
                    unnecessaryOutput.getLength(),
                    0
                );

                // choose element with lowest relax value
                maxTrueTotalWaste += getChosenUnnecessaryOutputLength(
                    outputElementsMinRelax,
                    unnecessaryOutput.getLength(),
                    outputElementsMinRelax.size() - 1
                );
            }
        }

        // if there is no relaxation, don't show min and max true total waste
        if (!hasRelaxation && minTrueTotalWaste.compareTo(maxTrueTotalWaste) == 0) {
            trueTotalWaste = minTrueTotalWaste;
            minTrueTotalWaste = null;
            maxTrueTotalWaste = null;
        }
    }

    private Integer getChosenUnnecessaryOutputLength(
        List<PlanOutput> outputElements,
        Integer fullLength,
        int index
    ) {
        int chosenOutputLength = 0;

        PlanOutput outputElement = outputElements.get(index);
        chosenOutputLength += fullLength;
        if (outputElement.getRelax() != null) {
            chosenOutputLength -= outputElement.getRelax();
        }
        outputElement.setCount(outputElement.getCount() - 1);
        if (outputElement.getCount() <= 0) {
            outputElements.remove(index);
        }

        return chosenOutputLength;
    }

    private List<PlanOutput> getDeepCopy(List<PlanOutput> outputs) {
        List<PlanOutput> copy = new ArrayList<>();
        for (PlanOutput output : outputs) {
            copy.add(new PlanOutput(output.getId(), output.getCount(), output.getRelax()));
        }
        return copy;
    }

    private void calculateTotalNeededInputLength(List<PlanInput> inputs) {
        totalNeededInputLength = 0;
        for (PlanInput input : inputs) {
            for (Pattern pattern: input.getPatterns()) {
                totalNeededInputLength += input.getLength() * pattern.getCount();
            }
        }
    }

    private void calculateTotalCost(List<PlanInput> inputs) {
        totalCost = null;
        for (PlanInput input : inputs) {
            Double inputCost = input.getCost();
            if (inputCost != null) {
                if (totalCost == null) totalCost = 0.0;
                for (Pattern pattern: input.getPatterns()) {
                    totalCost += inputCost * pattern.getCount();
                }
            }
        }
    }

}
