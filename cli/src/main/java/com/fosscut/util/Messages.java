package com.fosscut.util;

public class Messages {

    public static final String ORDER_FILE_IS_A_DIRECTORY_EXCEPTION = "Order path points to a directory. Order can only be read from a file.";
    public static final String ORDER_FILE_DOES_NOT_EXIST_EXCEPTION = "Failed to load order file, because it does not exist.";

    public static final String REDIS_ORDER_PATH_ERROR = "Path to resource in redis is incorrect: ";
    public static final String REDIS_ORDER_PATH_PROTOCOL_EXCEPTION = "Incorrect protocol. Must be 'redis'.";
    public static final String REDIS_ORDER_PATH_HOSTNAME_EXCEPTION = "Unable to read hostname.";
    public static final String REDIS_ORDER_PATH_PORT_EXCEPTION = "Unable to read port.";
    public static final String REDIS_ORDER_PATH_IDENTIFIER_EXCEPTION = "Unable to read identifier.";

    public static final String UNABLE_TO_GENERATE_NEW_PATTERNS = "Algorithm was not able to generate new patterns. Specified numer of input elements is not enough to generate a cutting plan. Please increase the 'count' field value of input elements.";
    public static final String LP_UNFEASIBLE_EXCEPTION = "UNFEASIBLE: The solver could not solve the problem. Try increasing the 'count' field value of input elements.";

    public static final String PLAN_PATTERN_DOES_NOT_FIT_IN_INPUT = "Generated pattern does not fit in the input element.";
    public static final String PLAN_DEMAND_NOT_SATISFIED = "Generated plan does not satisfy the demand.";

}
