package com.fosscut.alg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.type.cutting.CHOutput;

public class RelaxationSpread {

    private RelaxationSpreadStrategy relaxationSpreadStrategy;

    public RelaxationSpread(RelaxationSpreadStrategy relaxationSpreadStrategy) {
        this.relaxationSpreadStrategy = relaxationSpreadStrategy;
    }

    public List<SingleOutput> getSinglePatternDefinition(
        List<OrderOutput> outputs,
        List<Integer> outputCounts,
        List<Integer> relaxValues
    ) {
        List<SingleOutput> singlePatternDefinition = new ArrayList<SingleOutput>();

        for (int outputId = 0; outputId < outputs.size(); outputId++) {
            Integer outputCount = outputCounts.get(outputId);
            Integer relaxValue = relaxValues.get(outputId);

            // Only add outputs with a count higher than 0 to pattern definition
            if (outputCount > 0) {
                int remainingSpace = 0;
                int numberOfRelaxedOutputs = 0;
                OrderOutput output = outputs.get(outputId);

                if (output.getMaxRelax() != null && output.getMaxRelax() > 0) {
                    numberOfRelaxedOutputs += outputCount;
                    remainingSpace += outputCount * output.getMaxRelax();
                    remainingSpace -= relaxValue;
                }

                List<SingleOutput> singlePatternDefinitionForOneOutput = new ArrayList<SingleOutput>();
                for (int i = 0; i < outputCount; ++i) {
                    singlePatternDefinitionForOneOutput.add(new SingleOutput(
                        outputId,
                        output.getLength(),
                        output.getMaxRelax(),
                        output.getMaxRelax()
                    ));
                }

                if (numberOfRelaxedOutputs > 0) {
                    singlePatternDefinitionForOneOutput = applyRelaxationSpread(
                        singlePatternDefinitionForOneOutput,
                        remainingSpace,
                        numberOfRelaxedOutputs
                    );
                }

                singlePatternDefinition.addAll(singlePatternDefinitionForOneOutput);
            }
        }

        return singlePatternDefinition;
    }

    public List<CHOutput> convertSingleToChPatternDefinition(List<SingleOutput> singlePatternDefinition) {
        List<CHOutput> chPatternDefinition = new ArrayList<CHOutput>();

        CHOutput latest = null;
        for (SingleOutput singleOutput : singlePatternDefinition) {
            if (latest != null
                && singleOutput.getId().equals(latest.getId())
                && singleOutput.getRelax().equals(latest.getRelax().intValue())) {
                latest.setCount(latest.getCount() + 1);
            } else {
                chPatternDefinition.add(new CHOutput(
                    singleOutput.getId(),
                    singleOutput.getLength(),
                    1,
                    singleOutput.getRelax()
                ));
            }
            latest = chPatternDefinition.getLast();
        }

        return chPatternDefinition;
    }

    public List<SingleOutput> applyRelaxationSpread(
        List<SingleOutput> singlePatternDefinition,
        int remainingSpace,
        int numberOfRelaxedOutputs
    ) {
        if (relaxationSpreadStrategy == RelaxationSpreadStrategy.EQUAL_RELAX) {
            // Spread relaxation equally to all items with available relaxation
            singlePatternDefinition = equalSpreadRelaxation(singlePatternDefinition, remainingSpace);
        } else if (relaxationSpreadStrategy == RelaxationSpreadStrategy.EQUAL_SPACE) {
            // Spread remaining space equally to all items with available relaxation
            singlePatternDefinition = equalSpreadRemainingSpace(singlePatternDefinition, remainingSpace, numberOfRelaxedOutputs);
        } else if (relaxationSpreadStrategy == RelaxationSpreadStrategy.START) {
            // Spread remaining space to items on the end of the pattern (for ffd spread relax to longest items first)
            singlePatternDefinition = sideSpreadRemainingSpace(singlePatternDefinition.reversed(), remainingSpace).reversed();
        } else if (relaxationSpreadStrategy == RelaxationSpreadStrategy.END) {
            // Spread remaining space to items on the start of the pattern (for ffd spread relax to shortest items first)
            singlePatternDefinition = sideSpreadRemainingSpace(singlePatternDefinition, remainingSpace);
        }

        return singlePatternDefinition;
    }

    private List<SingleOutput> equalSpreadRelaxation(
        List<SingleOutput> singlePatternDefinition,
        int remainingSpace
    ) {
        int sumMaxRelax = 0;

        // Create a shallow copy to avoid modifying the order in the given list
        List<SingleOutput> relaxedOutputs = new ArrayList<SingleOutput>(singlePatternDefinition);

        // assume that no relaxation needs to be used
        for (SingleOutput singleOutput : relaxedOutputs) {
            singleOutput.setRelax(0);
            sumMaxRelax += singleOutput.getMaxRelax();
            if (singleOutput.getMaxRelax() == 0) {
                // do not consider outputs that cannot be relaxed
                singlePatternDefinition.remove(singleOutput);
            }
        }

        // apply relaxation only if necessary
        if (remainingSpace < sumMaxRelax) {
            int remainingRelax = sumMaxRelax - remainingSpace;

            int i = 0;
            int averageRelax = 0;
            while (remainingRelax > 0) {
                if (i % singlePatternDefinition.size() == 0) {
                    // recalculate averageRelax every time we loop through all outputs and on the first iteration
                    averageRelax = Math.max(remainingRelax / singlePatternDefinition.size(), 1);
                }

                SingleOutput singleOutput = singlePatternDefinition.reversed().get(i % singlePatternDefinition.size());
                int toRelax = singleOutput.getMaxRelax() - singleOutput.getRelax();

                if (toRelax >= averageRelax && remainingRelax >= averageRelax) {
                    singleOutput.setRelax(singleOutput.getRelax() + averageRelax);
                    remainingRelax -= averageRelax;
                } else if (toRelax > 0) {
                    singleOutput.setRelax(singleOutput.getRelax() + 1);
                    remainingRelax -= 1;
                } else {
                    singlePatternDefinition.remove(singleOutput);
                }

                i++;
            }
        }

        return relaxedOutputs;
    }

    private List<SingleOutput> equalSpreadRemainingSpace(
        List<SingleOutput> singlePatternDefinition,
        int remainingSpace,
        int numberOfRelaxedOutputs
    ) {
        int equalShare = Math.max(remainingSpace / numberOfRelaxedOutputs, 1);

        // avoids an infinite loop when remainingSpace will never reach 0
        int sumMaxRelax = getSumMaxRelax(singlePatternDefinition);

        int i = 0;
        while (remainingSpace > 0 && sumMaxRelax > 0) {
            SingleOutput singleOutput = singlePatternDefinition.get(i % singlePatternDefinition.size());
            if (singleOutput.getRelax() >= equalShare) {
                singleOutput.setRelax(singleOutput.getRelax() - equalShare);
                remainingSpace -= equalShare;
                sumMaxRelax -= equalShare;
            } else if (singleOutput.getRelax() > 0) {
                // necessary to make sure that all remaining space is used
                singleOutput.setRelax(singleOutput.getRelax() - 1);
                remainingSpace -= 1;
                sumMaxRelax -= 1;
            }
            i++;
        }

        return singlePatternDefinition;
    }

    private int getSumMaxRelax(List<SingleOutput> singlePatternDefinition) {
        int sumMaxRelax = 0;
        for (SingleOutput singleOutput : singlePatternDefinition) {
            sumMaxRelax += singleOutput.getMaxRelax();
        }
        return sumMaxRelax;
    }

    private List<SingleOutput> sideSpreadRemainingSpace(
        List<SingleOutput> singlePatternDefinition,
        int remainingSpace
    ) {
        int i = 0;
        while (remainingSpace > 0 && i < singlePatternDefinition.size()) {
            SingleOutput singleOutput = singlePatternDefinition.get(i);
            Integer spreadAmount = Math.min(remainingSpace, singleOutput.getRelax());
            singleOutput.setRelax(singleOutput.getRelax() - spreadAmount);
            remainingSpace -= spreadAmount;
            i++;
        }

        return singlePatternDefinition;
    }

}
