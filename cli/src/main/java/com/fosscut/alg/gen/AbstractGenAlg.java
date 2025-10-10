package com.fosscut.alg.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fosscut.exception.DuplicatesAreNotAllowedException;
import com.fosscut.exception.NotSupportedCutGenConfigException;
import com.fosscut.type.cutting.plan.PlanInput;

public abstract class AbstractGenAlg {

    protected Integer inputLength;
    protected Integer inputTypeCount;
    protected Integer minInputLength;
    protected Integer maxInputLength;
    protected boolean allowInputTypeDuplicates;

    protected int outputTypeCount;
    protected double outputLengthLowerBound;
    protected double outputLengthUpperBound;

    protected Random randomGenerator;

    public AbstractGenAlg(
        Integer inputLength,
        Integer inputTypeCount,
        Integer minInputLength,
        Integer maxInputLength,
        boolean allowInputTypeDuplicates,
        int outputTypeCount,
        double outputLengthLowerBound,
        double outputLengthUpperBound,
        Long seed
    ) {
        this.inputLength = inputLength;
        this.inputTypeCount = inputTypeCount;
        this.minInputLength = minInputLength;
        this.maxInputLength = maxInputLength;
        this.allowInputTypeDuplicates = allowInputTypeDuplicates;
        this.outputTypeCount = outputTypeCount;
        this.outputLengthLowerBound = outputLengthLowerBound;
        this.outputLengthUpperBound = outputLengthUpperBound;
        if (seed == null) this.randomGenerator = new Random();
        else this.randomGenerator = new Random(seed);
    }

    /******************************** Inputs **********************************/

    protected List<PlanInput> generateInputs()
        throws NotSupportedCutGenConfigException,
        DuplicatesAreNotAllowedException
    {
        List<PlanInput> inputs = new ArrayList<>();
        if (isSingleInputType())
            inputs = getSingleInputInputs();
        else if (isMultiInputType())
            inputs = getMultiInputInputs();
        else throw new NotSupportedCutGenConfigException("inputs");

        return inputs;
    }

    protected boolean isSingleInputType() {
        return inputTypeCount == null && inputLength != null;
    }

    protected boolean isMultiInputType() {
        return inputTypeCount != null && inputLength == null
            && minInputLength != null && maxInputLength != null;
    }

    protected List<PlanInput> getSingleInputInputs() {
        List<PlanInput> inputs = new ArrayList<>();
        inputs.add(new PlanInput(inputLength));
        return inputs;
    }

    protected List<PlanInput> getMultiInputInputs() throws DuplicatesAreNotAllowedException {
        List<Integer> inputLengths = new ArrayList<Integer>();
        List<PlanInput> inputs = new ArrayList<>();

        for (int i = 0; i < inputTypeCount; i++) {
            int length = randomGenerator.nextInt(
                minInputLength,
                maxInputLength + 1
            );

            if (inputLengths.contains(length)) {
                if (!allowInputTypeDuplicates) {
                    throw new DuplicatesAreNotAllowedException("input");
                }
            } else {
                inputLengths.add(length);
                inputs.add(new PlanInput(length));
            }
        }

        return inputs;
    }

    /******************************* Outputs **********************************/

    protected int generateNewLength(double lowerBound, double upperBound, int baseLength) {
        double rValue = randomGenerator.nextDouble();
        double percentage = lowerBound + (upperBound - lowerBound) * rValue;
        double length = percentage * baseLength + rValue;
        // casting to int floors the value (drops the decimal part)
        return (int) length;
    }

}
