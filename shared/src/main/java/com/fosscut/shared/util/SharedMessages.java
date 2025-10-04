package com.fosscut.shared.util;

public class SharedMessages {

    public static final String ORDER_VALID = "Order valid.";

    public static final String NONPOSITIVE_INPUT_LENGTH_ERROR = "All input lengths have to be positive.";
    public static final String NONPOSITIVE_OUTPUT_LENGTH_ERROR = "All output lengths have to be positive.";
    public static final String NONNEGATIVE_INPUT_COUNT_ERROR = "All input counts must be nonnegative.";
    public static final String NONNEGATIVE_OUTPUT_COUNT_ERROR = "All output counts must be nonnegative.";
    public static final String OUTPUT_LONGER_THAN_INPUT_ERROR = "Longest input element must be longer than longest output element for the order to be valid.";
    public static final String OUTPUT_SUM_LONGER_THAN_INPUT_SUM_ERROR = "Sum of all available input lengths must be greater than the sum of all required output lengths.";
    public static final String NULL_COST_EXCEPTION = "When minimizing cutting plan cost, cost has to be defined for all input elements.";
    public static final String RELAX_COST_UNDEFINED_ERROR = "When enabling relaxation, relax cost has to be defined locally for each output element or globally for all outputs using '-c' parameter.";

}
