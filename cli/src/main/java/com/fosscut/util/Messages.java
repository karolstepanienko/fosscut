package com.fosscut.util;

public class Messages {
    public static final String ORDER_VALID = "Order valid.";
    public static final String OUTPUT_LONGER_THAN_INPUT_ERROR = "Longest input element must be longer than longest output element for the order to be valid.";
    public static final String OUTPUT_SUM_LONGER_THAN_INPUT_SUM_ERROR = "Sum of all available input lengths must be greater than the sum of all required output lengths.";
    public static final String NON_POSITIVE_INPUT_LENGTH_ERROR = "All input lengths have to be positive.";
    public static final String NON_POSITIVE_OUTPUT_LENGTH_ERROR = "All output lengths have to be positive.";
    public static final String UNABLE_TO_GENERATE_NEW_PATTERNS = "Algorithm was not able to generate new patterns. Specified numer of input elements is not enough to generate a cutting plan. Please increase the 'count' field value of input elements.";
}
