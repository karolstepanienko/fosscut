package com.fosscut.alg.gen;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fosscut.exception.NoRelaxationAppliedException;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.util.Messages;

public class RelaxApplicator {

    private Integer outputTypesToRelaxPercentage;
    private Integer outputTypeLengthRelaxPercentage;
    private Random randomGenerator;

    public RelaxApplicator(
        Integer outputTypesToRelaxPercentage,
        Integer outputTypeLengthRelaxPercentage,
        Long seed
    ) {
        this.outputTypesToRelaxPercentage = outputTypesToRelaxPercentage;
        this.outputTypeLengthRelaxPercentage = outputTypeLengthRelaxPercentage;
        this.randomGenerator = new Random(seed);
    }

    // Since relax applicator does not change the length of the outputs,
    // we can reuse the same output types and just change the max relax values.
    // Changing patterns is unnecessary since relax field value will not change.
    public List<OrderOutput> relaxOrderOutputs(List<OrderOutput> outputs) throws NoRelaxationAppliedException {
        if (validate()) {
            // rounds down
            Integer outputTypesToRelax = (int) (outputTypesToRelaxPercentage / 100.0 * outputs.size());
            List<Integer> indexes = getRandomIndexes(outputs.size(), outputTypesToRelax, randomGenerator);
            if (outputTypesToRelax == 0) {
                throw new NoRelaxationAppliedException("");
            }
            for (Integer index: indexes) {
                outputs.get(index).setMaxRelax((int) (outputs.get(index).getLength() * outputTypeLengthRelaxPercentage / 100.0));
            }
            // no empty max relax values are allowed by algorithms if relaxation is enabled
            for (OrderOutput output: outputs) {
                if (output.getMaxRelax() == null) {
                    output.setMaxRelax(0);
                }
            }
        }
        return outputs;
    }

    private static LinkedList<Integer> getRandomIndexes(int size, int count, Random randomGenerator) {
        LinkedList<Integer> list = IntStream.rangeClosed(0, size - 1)
            .boxed()
            .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(list, randomGenerator);
        return new LinkedList<>(list.subList(0, count));
    }

    private boolean validate() {
        if (!validatePercentages()) {
            throw new IllegalArgumentException(Messages.RELAX_APPLY_PERCENTAGE_ERROR);
        }
        if (!validateParamsDefined()) {
            throw new IllegalArgumentException(Messages.RELAX_APPLY_NULL_ERROR);
        }
        return outputTypesToRelaxPercentage != null && outputTypeLengthRelaxPercentage != null;
    }

    private boolean validatePercentages() {
        if (outputTypesToRelaxPercentage != null) {
            if (outputTypesToRelaxPercentage < 0 || outputTypesToRelaxPercentage > 100) {
                return false;
            }
        }
        if (outputTypeLengthRelaxPercentage != null) {
            if (outputTypeLengthRelaxPercentage < 0 || outputTypeLengthRelaxPercentage > 100) {
                return false;
            }
        }
        return true;
    }

    private boolean validateParamsDefined() {
        if (outputTypesToRelaxPercentage != null && outputTypeLengthRelaxPercentage == null) {
            return false;
        }
        if (outputTypesToRelaxPercentage == null && outputTypeLengthRelaxPercentage != null) {
            return false;
        }
        return true;
    }

}