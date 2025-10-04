package com.fosscut.shared.util;

import java.util.List;

import com.fosscut.shared.exception.ParameterValidationException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderOutput;

public class RelaxValidator {

    private boolean relaxEnabled;
    private Double relaxCost;

    public RelaxValidator(boolean relaxEnabled, Double relaxCost) {
        this.relaxEnabled = relaxEnabled;
        this.relaxCost = relaxCost;
    }

    public void validate(Order order) throws ParameterValidationException{
        if (relaxEnabled && (relaxCost == null || relaxCost.isNaN() || relaxCost < 0)) {
            // relax cost has to be defined for each output element or globally
            if (!isValidRelaxCostDefinedForAllOutputs(order.getOutputs()))
            throw new ParameterValidationException(SharedMessages.RELAX_COST_UNDEFINED_ERROR);
        }
    }

    private boolean isValidRelaxCostDefinedForAllOutputs(List<OrderOutput> outputs) {
        boolean valid = true;

        for (OrderOutput output: outputs) {
            if (output.getRelaxCost() == null
                || output.getRelaxCost().isNaN()
                || output.getRelaxCost() < 0
            ) {
                valid = false;
                break;
            }
        }

        return valid;
    }

}

