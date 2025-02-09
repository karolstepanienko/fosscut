package com.fosscut.util;

public class Messages {
    public static final String ORDER_FILE_IS_A_DIRECTORY_EXCEPTION = "Order path points to a directory. Order can only be read from a file.";
    public static final String ORDER_FILE_DOES_NOT_EXIST_EXCEPTION = "Failed to load order file, because it does not exist.";

    public static final String REDIS_ORDER_PATH_ERROR = "Path to resource in redis is incorrect: ";
    public static final String REDIS_ORDER_PATH_PROTOCOL_EXCEPTION = "Incorrect protocol. Must be 'redis'.";
    public static final String REDIS_ORDER_PATH_HOSTNAME_EXCEPTION = "Unable to read hostname.";
    public static final String REDIS_ORDER_PATH_PORT_EXCEPTION = "Unable to read port.";
    public static final String REDIS_ORDER_PATH_IDENTIFIER_EXCEPTION = "Unable to read identifier.";

    public static final String ORDER_VALID = "Order valid.";
    public static final String OUTPUT_LONGER_THAN_INPUT_ERROR = "Longest input element must be longer than longest output element for the order to be valid.";
    public static final String OUTPUT_SUM_LONGER_THAN_INPUT_SUM_ERROR = "Sum of all available input lengths must be greater than the sum of all required output lengths.";
    public static final String NONPOSITIVE_INPUT_LENGTH_ERROR = "All input lengths have to be positive.";
    public static final String NONPOSITIVE_OUTPUT_LENGTH_ERROR = "All output lengths have to be positive.";
    public static final String NONNEGATIVE_INPUT_COUNT_ERROR = "All input counts must be nonnegative.";
    public static final String NONNEGATIVE_OUTPUT_COUNT_ERROR = "All output counts must be nonnegative.";
    public static final String UNABLE_TO_GENERATE_NEW_PATTERNS = "Algorithm was not able to generate new patterns. Specified numer of input elements is not enough to generate a cutting plan. Please increase the 'count' field value of input elements.";
    public static final String LP_UNFEASIBLE_EXCEPTION = "UNFEASIBLE: The solver could not solve the problem. Try increasing the 'count' field value of input elements.";
}
