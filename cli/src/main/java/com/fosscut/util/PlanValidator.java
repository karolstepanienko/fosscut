package com.fosscut.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.PlanValidationException;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.shared.util.save.YamlDumper;
import com.fosscut.type.cutting.plan.CuttingPlan;
import com.fosscut.type.cutting.plan.Pattern;
import com.fosscut.type.cutting.plan.PlanInput;
import com.fosscut.type.cutting.plan.PlanOutput;
import com.fosscut.type.cutting.plan.PlanOutputDouble;
import com.fosscut.type.cutting.plan.PlanOutputInteger;

public class PlanValidator {

    private static final Logger logger = LoggerFactory.getLogger(PlanValidator.class);
    private OffendingPattern offendingPattern;

    public PlanValidator() {}

    public void validatePlan(CuttingPlan plan) throws PlanValidationException {
        logger.info("Running plan validation...");
        this.validate(plan);
        logger.info("Plan valid.");
    }

    private void validate(CuttingPlan plan) throws PlanValidationException {
        if (!patternsFitInInputs(plan)) throw new PlanValidationException(Messages.PLAN_PATTERN_DOES_NOT_FIT_IN_INPUT + " \n" + offendingPattern.toString());
        else if (!isDemandSatisfied(plan)) throw new PlanValidationException(Messages.PLAN_DEMAND_NOT_SATISFIED);
    }

    /******************** Do patterns fit in inputs? **************************/

    private boolean patternsFitInInputs(CuttingPlan plan) {
        for (PlanInput input : plan.getInputs()) {
            if (!patternsFitInInput(input, plan.getOutputs())) {
                return false;
            }
        }
        return true;
    }

    private boolean patternsFitInInput(PlanInput input, List<OrderOutput> outputs) {
        for (Pattern pattern: input.getPatterns()) {
            if (!patternFitInInput(pattern, input, outputs)) {
                return false;
            }
        }
        return true;
    }

    private boolean patternFitInInput(Pattern pattern, PlanInput input, List<OrderOutput> outputs) {
        PatternLength patternLengthObj = calculatePatternLength(pattern, outputs);

        // Round up to get completely remove decimal part of pattern length to avoid precision issues
        BigDecimal patternLength = BigDecimal.valueOf(patternLengthObj.getLength())
            .setScale(0, RoundingMode.UP);

        if (patternLength.compareTo(BigDecimal.valueOf(input.getLength())) > 0) {
            this.offendingPattern = new OffendingPattern(input.getLength(), pattern);
            return false; // Pattern does not fit
        }
        return true;
    }

    private PatternLength calculatePatternLength(Pattern pattern, List<OrderOutput> outputs) {
        PatternLength patternLengthObj = new PatternLength();

        pattern.getPatternDefinition().forEach(planOutput -> {
            Integer outputLength = outputs.get(planOutput.getId()).getLength();

            Double relaxedOutputLength = Double.valueOf(outputLength);
            Double relaxValue = getRelaxValue(planOutput);
            if (relaxValue != null) {
                relaxedOutputLength -= relaxValue;
            }

            Double increment = planOutput.getCount() * relaxedOutputLength;
            patternLengthObj.setLength(patternLengthObj.getLength() + increment);
        });

        return patternLengthObj;
    }

    private Double getRelaxValue(PlanOutput planOutput) {
        Double relaxValue = null;
        String outputClassName = planOutput.getClass().getSimpleName();

        if (outputClassName.equals("PlanOutputDouble")) {
            PlanOutputDouble planOutputDouble = (PlanOutputDouble) planOutput;
            relaxValue = planOutputDouble.getRelax();
        } else if (outputClassName.equals("PlanOutputInteger")) {
            PlanOutputInteger planOutputInteger = (PlanOutputInteger) planOutput;
            relaxValue = Double.valueOf(planOutputInteger.getRelax());
        }

        return relaxValue;
    }

    private class PatternLength {
        private Double length;

        public PatternLength() {
            this.length = 0.0;
        }

        public Double getLength() {
            return length;
        }

        public void setLength(Double length) {
            this.length = length;
        }
    }

    private class OffendingPattern {
        private Integer inputLength;
        private Pattern pattern;

        public OffendingPattern(Integer inputLength, Pattern pattern) {
            this.inputLength = inputLength;
            this.pattern = pattern;
        }

        public Integer getInputLength() {
            return inputLength;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public String toString() {
            return new YamlDumper().dump(this);
        }
    }

    /*********************** Is demand satisfied? *****************************/

    private boolean isDemandSatisfied(CuttingPlan plan) {
        // TODO implement
        return true;
    }

}
