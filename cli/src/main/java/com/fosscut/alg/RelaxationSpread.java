package com.fosscut.alg;

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
        if (relaxationSpreadStrategy == RelaxationSpreadStrategy.EQUAL) {
            // Spread remaining space equally to all items with available relaxation
            singlePatternDefinition = equalSpreadRemainingSpace(singlePatternDefinition, remainingSpace, numberOfRelaxedOutputs);
        } else if (relaxationSpreadStrategy == RelaxationSpreadStrategy.LONGEST) {
            // Spread remaining space to items on the end of the pattern (relax longest items first)
            singlePatternDefinition = sideSpreadRemainingSpace(singlePatternDefinition.reversed(), remainingSpace).reversed();
        } else if (relaxationSpreadStrategy == RelaxationSpreadStrategy.SHORTEST) {
            // Spread remaining space to items on the start of the pattern (relax shortest items first)
            singlePatternDefinition = sideSpreadRemainingSpace(singlePatternDefinition, remainingSpace);
        }

        return singlePatternDefinition;
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

    private List<SingleOutput> equalSpreadRemainingSpace(
        List<SingleOutput> singlePatternDefinition,
        int remainingSpace,
        int numberOfRelaxedOutputs
    ) {
        int equalShare = Math.max(remainingSpace / numberOfRelaxedOutputs, 1);

        // avoids an infinite loop when remainingSpace will never reach 0
        int remainingRelax = getRemainingRelax(singlePatternDefinition);

        int i = 0;
        while (remainingSpace > 0 && remainingRelax > 0) {
            SingleOutput singleOutput = singlePatternDefinition.get(i % singlePatternDefinition.size());
            if (singleOutput.getRelax() >= equalShare) {
                singleOutput.setRelax(singleOutput.getRelax() - equalShare);
                remainingSpace -= equalShare;
                remainingRelax -= equalShare;
            } else if (singleOutput.getRelax() > 0) {
                // necessary to make sure that all remaining space is used
                singleOutput.setRelax(singleOutput.getRelax() - 1);
                remainingSpace -= 1;
                remainingRelax -= 1;
            }
            i++;
        }

        return singlePatternDefinition;
    }

    private int getRemainingRelax(List<SingleOutput> singlePatternDefinition) {
        int remainingRelax = 0;
        for (SingleOutput singleOutput : singlePatternDefinition) {
            remainingRelax += singleOutput.getRelax();
        }
        return remainingRelax;
    }

}
