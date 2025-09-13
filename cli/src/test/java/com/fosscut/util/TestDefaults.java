package com.fosscut.util;

public class TestDefaults {

    public static final String FOSSCUT_BINARY_FOLDER_PATH = "./build/native/nativeCompile";
    public static final String FOSSCUT_LINUX_BINARY_NAME = "fosscut";
    public static final String FOSSCUT_WINDOWS_BINARY_NAME = "fosscut.exe";

    public static final String LINUX_SHELL = "bash";
    public static final String LINUX_CMD_PASS_OPTION = "-c";
    public static final String WINDOWS_SHELL = "powershell.exe";

    public static final long DEFAULT_COMMAND_TIMEOUT = 5;

    public static final String VERSION_STRING = "fosscut version \"0.0.1\"";

    public static final String EXAMPLE_DIRECTORY = "./test";

    public static final String REDIS_ORDER_PATH = "redis://127.0.0.1:6379/example-order";
    public static final String EXAMPLE_REDIS_CONNECTION_SECRETS = "./test/example-redis-connection-secrets.yaml";
    public static final String REDIS_ORDER_PATH_PROTOCOL_EXCEPTION = "welp://127.0.0.1:6379/example-order";
    public static final String REDIS_ORDER_PATH_HOSTNAME_EXCEPTION = "redis://:6379/example-order";
    public static final String REDIS_ORDER_PATH_PORT_EXCEPTION = "redis://127.0.0.1/example-order";
    public static final String REDIS_ORDER_PATH_IDENTIFIER_EXCEPTION = "redis://127.0.0.1:6379/";

    public static final String FAIL_EXECUTION_INPUT_COUNT = "./test/order/fail/execution-input-count.yaml";

    public static final String FAIL_VALIDATION_INPUT_COUNT = "./test/order/fail/validation-input-count.yaml";
    public static final String FAIL_VALIDATION_NONPOSITIVE_INPUT_LENGTH = "./test/order/fail/validation-nonpositive-input-length.yaml";
    public static final String FAIL_VALIDATION_NONPOSITIVE_OUTPUT_LENGTH = "./test/order/fail/validation-nonpositive-output-length.yaml";
    public static final String FAIL_VALIDATION_NONNEGATIVE_INPUT_COUNT = "./test/order/fail/validation-nonnegative-input-count.yaml";
    public static final String FAIL_VALIDATION_NONNEGATIVE_OUTPUT_COUNT = "./test/order/fail/validation-nonnegative-output-count.yaml";
    public static final String FAIL_VALIDATION_OUTPUT_LONGER_THAN_INPUT = "./test/order/fail/validation-output-longer-than-input.yaml";

    public static final String EXAMPLE_ORDER = "./test/order/success/default.yaml";
    public static final String EXAMPLE_INPUT_COUNT_ORDER = "./test/order/success/input-count.yaml";
    public static final String EXAMPLE_INPUT_COUNT_ZEROS_ORDER = "./test/order/success/input-count-zeros.yaml";
    public static final String EXAMPLE_INPUT_COST_ORDER = "./test/order/success/input-cost.yaml";
    public static final String EXAMPLE_FFD_RELAX_ORDER = "./test/order/success/ffd-relax.yaml";
    public static final String EXAMPLE_FFD_COMPLEX_PATTERN_ORDER = "./test/order/success/ffd-complex-pattern.yaml";
    public static final String EXAMPLE_FFD_LARGE_MULTI_RELAX_ORDER = "./test/order/success/ffd-large-multi-relax.yaml";
    public static final String EXAMPLE_SHORT_INPUT_COUNT_COST_NULL_ORDER = "./test/order/success/short-input-count-cost-null.yaml";
    public static final String EXAMPLE_RELAX_STRATEGIES_ORDER = "./test/order/success/relax-strategies.yaml";
    public static final String EXAMPLE_MULTI_RELAX_ORDER = "./test/order/success/multi-relax.yaml";

    public static final String CG_CLP_GLOP_SCIP_PLAN = "./test/plan/cg-CLP_GLOP-SCIP.yaml";
    public static final String CG_CLP_CBC_PLAN = "./test/plan/cg-CLP-CBC.yaml";
    public static final String CG_CLP_GLOP_SAT_1_PLAN = "./test/plan/cg-CLP_GLOP-SAT-1.yaml";
    public static final String CG_CLP_GLOP_SAT_2_PLAN = "./test/plan/cg-CLP_GLOP-SAT-2.yaml";
    public static final String CG_CLP_GLOP_SAT_3_PLAN = "./test/plan/cg-CLP_GLOP-SAT-3.yaml";

    public static final String CG_GLOP_CBC_PLAN = "./test/plan/cg-GLOP-CBC.yaml";

    public static final String CG_PDLP_CBC_PLAN = "./test/plan/cg-PDLP-CBC.yaml";
    public static final String CG_PDLP_SAT_1_PLAN = "./test/plan/cg-PDLP-SAT-1.yaml";
    public static final String CG_PDLP_SAT_2_PLAN = "./test/plan/cg-PDLP-SAT-2.yaml";
    public static final String CG_PDLP_SCIP_PLAN = "./test/plan/cg-PDLP-SCIP.yaml";

    public static final String FFD_PLAN = "./test/plan/ffd.yaml";
    public static final String GREEDY_PLAN = "./test/plan/greedy.yaml";
    public static final String GREEDY_SAT_PLAN = "./test/plan/greedy-SAT.yaml";

    public static final String CG_RELAX_0_PLAN = "./test/plan/relax/cg-relax-0.yaml";
    public static final String CG_RELAX_1_PLAN = "./test/plan/relax/cg-relax-1.yaml";
    public static final String CG_MULTI_RELAX_1_PLAN = "./test/plan/relax/cg-multi-relax-1.yaml";
    public static final String RELAX_EQUAL_PLAN = "./test/plan/relax/relax-equal.yaml";
    public static final String RELAX_START_PLAN = "./test/plan/relax/relax-start.yaml";
    public static final String RELAX_END_PLAN = "./test/plan/relax/relax-end.yaml";

    public static final String FFD_WITHOUT_RELAX_PLAN = "./test/plan/ffd-without-relax.yaml";
    public static final String FFD_COMPLEX_PATTERN_PLAN = "./test/plan/ffd-complex-pattern.yaml";

    public static final String FFD_DEFAULT_RELAX_PLAN = "./test/plan/relax/ffd-default-relax.yaml";
    public static final String FFD_RELAX_EQUAL_PLAN = "./test/plan/relax/ffd-relax-equal.yaml";
    public static final String FFD_RELAX_START_PLAN = "./test/plan/relax/ffd-relax-start.yaml";
    public static final String FFD_RELAX_END_PLAN = "./test/plan/relax/ffd-relax-end.yaml";
    public static final String FFD_COMPLEX_PATTERN_EQUAL_RELAX_PLAN = "./test/plan/relax/ffd-complex-pattern-equal-relax.yaml";
    public static final String FFD_COMPLEX_PATTERN_EQUAL_SPACE_PLAN = "./test/plan/relax/ffd-complex-pattern-equal-space.yaml";
    public static final String FFD_COMPLEX_PATTERN_START_PLAN = "./test/plan/relax/ffd-complex-pattern-start.yaml";
    public static final String FFD_COMPLEX_PATTERN_END_PLAN = "./test/plan/relax/ffd-complex-pattern-end.yaml";
    public static final String FFD_MULTI_RELAX_EQUAL_RELAX_PLAN = "./test/plan/relax/ffd-multi-relax-equal-relax.yaml";
    public static final String FFD_MULTI_RELAX_EQUAL_SPACE_PLAN = "./test/plan/relax/ffd-multi-relax-equal-space.yaml";
    public static final String FFD_LARGE_MULTI_RELAX_PLAN = "./test/plan/relax/ffd-large-multi-relax.yaml";

    public static final String GREEDY_RELAX_0_PLAN = "./test/plan/relax/greedy-relax-0.yaml";
    public static final String GREEDY_RELAX_1_PLAN = "./test/plan/relax/greedy-relax-1.yaml";

    public static final String FFD_INPUT_COUNT_PLAN = "./test/plan/ffd-input-count.yaml";
    public static final String GREEDY_INPUT_COUNT_PLAN = "./test/plan/greedy-input-count.yaml";
    public static final String CG_INPUT_COUNT_PLAN = "./test/plan/cg-input-count.yaml";

    public static final String FFD_INPUT_COUNT_ZEROS_PLAN = "./test/plan/ffd-input-count-zeros.yaml";
    public static final String GREEDY_CG_INPUT_COUNT_ZEROS_PLAN = "./test/plan/greedy-cg-input-count-zeros.yaml";

    public static final String FFD_INPUT_COST_PLAN = "./test/plan/ffd-input-cost.yaml";
    public static final String GREEDY_INPUT_COST_PLAN = "./test/plan/greedy-input-cost.yaml";
    public static final String CG_INPUT_COST_PLAN = "./test/plan/cg-input-cost.yaml";

    public static final String SHORT_INPUT_COUNT_COST_NULL_PLAN = "./test/plan/short-input-count-cost-null.yaml";

    public static final String CUTGEN_SIMPLE_ORDER = "./test/order/cutgen/simple-order.yaml";
    public static final String CUTGEN_MULTI_INPUT_ORDER = "./test/order/cutgen/multi-input-order.yaml";
    public static final String CUTGEN_ALLOW_INPUT_DUPLICATES = "./test/order/cutgen/input-duplicates-order.yaml";
    public static final String CUTGEN_ALLOW_OUTPUT_DUPLICATES = "./test/order/cutgen/output-duplicates-order.yaml";

    public static final String PLAN_FAIL_PATTERN_TO_LONG = "./test/plan/fail/pattern-to-long.yaml";
    public static final String PLAN_FAIL_PATTERN_TO_LONG_RELAX = "./test/plan/fail/pattern-to-long-relax.yaml";
    public static final String PLAN_FAIL_DEMAND_NOT_SATISFIED = "./test/plan/fail/demand-not-satisfied.yaml";
    public static final String PLAN_FAIL_RELAX_GREATER_THAN_MAX_RELAX = "./test/plan/fail/relax-greater-than-maxRelax.yaml";

}
