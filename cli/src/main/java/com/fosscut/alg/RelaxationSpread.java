package com.fosscut.alg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.RelaxationSpreadStrategy;

public class RelaxationSpread {

    private RelaxationSpreadStrategy relaxationSpreadStrategy;

    public RelaxationSpread(RelaxationSpreadStrategy relaxationSpreadStrategy) {
        this.relaxationSpreadStrategy = relaxationSpreadStrategy;
    }

    public List<SingleOutput> applyRelaxationSpread(
        List<SingleOutput> singlePatternDefinition,
        int remainingSpace,
        int numberOfRelaxedOutputs
    ) {
        if (relaxationSpreadStrategy == RelaxationSpreadStrategy.EQUAL_RELAX) {
            // Spread relaxation equally to all items with available relaxation
            singlePatternDefinition = equalSpreadRelaxation(singlePatternDefinition, remainingSpace, numberOfRelaxedOutputs);
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
        int remainingSpace,
        int numberOfRelaxedOutputs
    ) {
        int sumMaxRelax = 0;
        int minMaxRelax = Integer.MAX_VALUE;

        // assume that no relaxation needs to be used
        for (SingleOutput singleOutput : singlePatternDefinition) {
            singleOutput.setRelax(0);
            sumMaxRelax += singleOutput.getMaxRelax();
            if (singleOutput.getMaxRelax() < minMaxRelax) {
                minMaxRelax = singleOutput.getMaxRelax();
            }
        }

        // Create a shallow copy to avoid modifying the order in the given list
        List<SingleOutput> relaxedOutputs = new ArrayList<SingleOutput>(singlePatternDefinition);

        // apply relaxation only if necessary
        if (remainingSpace < sumMaxRelax) {
            int remainingRelax = sumMaxRelax - remainingSpace;
            int averageRelax = Math.max(remainingRelax / numberOfRelaxedOutputs, 1);

            int i = 0;
            while (remainingRelax > 0) {
                SingleOutput singleOutput = singlePatternDefinition.get(i % singlePatternDefinition.size());
                int toRelax = singleOutput.getMaxRelax() - singleOutput.getRelax();

                if (toRelax >= averageRelax && remainingRelax >= averageRelax) {
                    singleOutput.setRelax(singleOutput.getRelax() + averageRelax);
                    remainingRelax -= averageRelax;
                } else if (remainingRelax >= toRelax && toRelax > 0) {
                    singleOutput.setRelax(singleOutput.getMaxRelax());
                    remainingRelax -= toRelax;
                    singlePatternDefinition.remove(singleOutput);
                } else if (toRelax > 0) {
                    singleOutput.setRelax(singleOutput.getRelax() + 1);
                    remainingRelax -= 1;
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
