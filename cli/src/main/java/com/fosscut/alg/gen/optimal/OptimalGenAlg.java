package com.fosscut.alg.gen.optimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fosscut.alg.gen.AbstractGenAlg;
import com.fosscut.exception.DuplicatesAreNotAllowedException;
import com.fosscut.exception.NotSupportedCutGenConfigException;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.shared.type.cutting.plan.Pattern;
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.shared.type.cutting.plan.PlanInput;
import com.fosscut.shared.type.cutting.plan.PlanOutput;

public class OptimalGenAlg extends AbstractGenAlg {

    private int outputCount;
    private boolean disableReuseOfExistingOutputTypes;

    private HashMap<Integer, OrderOutput> outputTypeMap;

    public OptimalGenAlg(
        Integer inputLength,
        Integer inputTypeCount,
        Integer minInputLength,
        Integer maxInputLength,
        boolean allowInputTypeDuplicates,
        int outputCount,
        int outputTypeCount,
        double outputLengthLowerBound,
        double outputLengthUpperBound,
        Long seed,
        boolean disableReuseOfExistingOutputTypes
    ) {
        super(
            inputLength,
            inputTypeCount,
            minInputLength,
            maxInputLength,
            allowInputTypeDuplicates,
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            seed
        );
        this.outputCount = outputCount;
        this.disableReuseOfExistingOutputTypes = disableReuseOfExistingOutputTypes;
        this.outputTypeMap = new HashMap<>();
    }

    public Plan nextOrder()
        throws NotSupportedCutGenConfigException,
        DuplicatesAreNotAllowedException
    {
        List<PlanInput> inputs = divideInputsIntoOutputs(generateInputs());
        List<OrderOutput> outputs = new ArrayList<>(outputTypeMap.values());
        Plan orderWithCuttingPlan = new Plan(inputs, outputs);
        return orderWithCuttingPlan;
    }

    private List<PlanInput> divideInputsIntoOutputs(List<PlanInput> inputs) {
        // at least one input must generate new output types
        inputs.set(0, divideInputIntoNewOutputs(inputs.get(0)));

        // now try to generate patterns with already existing output types
        // this allows for finer control of output type count
        int i = 1;
        while (outputTypeMap.size() < outputTypeCount) {
            int chosenInputIndex = i % inputs.size();

            List<Integer> newPattern = divideInputIntoExistingOutputs(inputs.get(chosenInputIndex).getLength());
            if (!disableReuseOfExistingOutputTypes || wasNewOutputTypeGenerated(newPattern)) {
                addOutputTypesToMap(newPattern);
                inputs.set(chosenInputIndex, addPatternToInput(inputs.get(chosenInputIndex), newPattern));
            } else {
                // if no new output type was generated while reusing already existing output types
                // then try generating only new output types without reusing existing ones
                inputs.set(chosenInputIndex, divideInputIntoNewOutputs(inputs.get(chosenInputIndex)));
            }

            i++;
        }

        // finally just generate demands with existing patterns
        i = 0;
        while (getGeneratedOutputCount() < outputCount) {
            int chosenInputIndex = i % inputs.size();
            inputs.set(chosenInputIndex, generateDemandWithExistingPatterns(inputs.get(chosenInputIndex)));
            i++;
        }

        return inputs;
    }

    // generates new pattern with new output element types
    private PlanInput divideInputIntoNewOutputs(PlanInput input) {
        Integer remainingLength = input.getLength();

        List<Integer> patternInList = new ArrayList<>();
        while (remainingLength > 0) {
            int newOutputLength = generateNewLength(
                outputLengthLowerBound,
                outputLengthUpperBound,
                input.getLength()
            );

            if (remainingLength - newOutputLength < getMinOutputLength())
                newOutputLength = remainingLength;

            patternInList.add(newOutputLength);
            addOutputTypeToMap(newOutputLength);

            remainingLength -= newOutputLength;
        }

        return addPatternToInput(input, patternInList);
    }

    // can generate up to one new pattern and output type at the end of the pattern
    private List<Integer> divideInputIntoExistingOutputs(Integer inputLength) {
        Integer remainingLength = inputLength;

        List<Integer> patternInList = new ArrayList<>();
        while (remainingLength > 0) {
            int id = randomGenerator.nextInt(0, outputTypeMap.size());
            int outputLength = outputTypeMap.get(id).getLength();

            if (remainingLength - outputLength < getMinOutputLength())
                outputLength = remainingLength;

            patternInList.add(outputLength);

            remainingLength -= outputLength;
        }

        return patternInList;
    }

    // does not generate new output types or patterns
    // reuses existing patterns to generate more demand
    private PlanInput generateDemandWithExistingPatterns(PlanInput input) {
        int chosenPatternId = randomGenerator.nextInt(0, input.getPatterns().size());
        Pattern chosenPattern = input.getPatterns().get(chosenPatternId);

        // increase count of chosen pattern
        chosenPattern.setCount(chosenPattern.getCount() + 1);
        input.getPatterns().set(chosenPatternId, chosenPattern);

        // increase count of output elements in outputTypeMap
        chosenPattern.getPatternDefinition().forEach(po -> {
            OrderOutput orderOutput = outputTypeMap.get(po.getId());
            int newCount = orderOutput.getCount() + po.getCount();
            orderOutput.setCount(newCount);
        });

        return input;
    }

    /******************************* Helpers **********************************/

    private int getMinOutputLength() {
        return (int) (outputLengthLowerBound * getMinInputLength());
    }

    private int getMinInputLength() {
        if (minInputLength != null) return minInputLength;
        else if (inputLength != null) return inputLength;
        else return 0;
    }

    private boolean wasNewOutputTypeGenerated(List<Integer> patternInList) {
        for (Integer length : patternInList) {
            if (!isOutputTypeInMap(length)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOutputTypeInMap(int length) {
        return outputTypeMap.values().stream().anyMatch(o -> o.getLength() == length);
    }

    private void addOutputTypesToMap(List<Integer> lengths) {
        for (Integer length : lengths) {
            addOutputTypeToMap(length);
        }
    }

    private void addOutputTypeToMap(int length) {
        OrderOutput existingOutputType = outputTypeMap.values().stream()
            .filter(o -> o.getLength() == length)
            .findFirst()
            .orElseGet(() -> {
                int newId = outputTypeMap.size();
                OrderOutput newOutputType = new OrderOutput(length, 0);
                outputTypeMap.put(newId, newOutputType);
                return newOutputType;
            });
        existingOutputType.setCount(existingOutputType.getCount() + 1);
    }

    private PlanInput addPatternToInput(PlanInput input, List<Integer> patternInList) {
        Pattern pattern = new Pattern(1, patternInListToPlanOutputList(patternInList));
        if (input.getPatterns() == null) input.setPatterns(new ArrayList<>());
        // Increase count if pattern already exists
        if (input.getPatterns().contains(pattern)) {
            int existingPatternIndex = input.getPatterns().indexOf(pattern);
            Pattern existingPattern = input.getPatterns().get(existingPatternIndex);
            existingPattern.setCount(existingPattern.getCount() + 1);
            input.getPatterns().set(existingPatternIndex, existingPattern);
        } else {
            input.getPatterns().add(pattern);
        }
        return input;
    }

    private List<PlanOutput> patternInListToPlanOutputList(List<Integer> patternInList) {
        List<PlanOutput> planOutputs = new ArrayList<>();
        HashMap<Integer, Integer> lengthToCountMap = new HashMap<>();

        for (Integer length : patternInList) {
            if (!lengthToCountMap.containsKey(length)) {
                lengthToCountMap.put(length, 1);
            } else {
                lengthToCountMap.put(length, lengthToCountMap.get(length) + 1);
            }
        }

        for (Integer length : lengthToCountMap.keySet()) {
            Integer count = lengthToCountMap.get(length);
            Integer id = null;
            for (Map.Entry<Integer, OrderOutput> entry : outputTypeMap.entrySet()) {
                if (entry.getValue().getLength().equals(length)) {
                    id = entry.getKey();
                }
            }
            if (id == null) {
                throw new IllegalStateException("Output type with length " + length + " not found in outputTypeMap.");
            }
            planOutputs.add(new PlanOutput(id, count, null));
        }

        return planOutputs;
    }

    private int getGeneratedOutputCount() {
        int totalCount = 0;
        for (OrderOutput output : outputTypeMap.values()) {
            totalCount += output.getCount();
        }
        return totalCount;
    }

}
