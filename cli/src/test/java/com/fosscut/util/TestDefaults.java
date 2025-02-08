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

    public static final String REDIS_ORDER_PATH = "redis://127.0.0.1:6379/example-order";
    public static final String EXAMPLE_REDIS_CONNECTION_SECRETS = "./test/example-redis-connection-secrets.yaml";

    public static final String FAIL_EXECUTION_INPUT_COUNT = "./test/order/fail/execution-input-count.yaml";

    public static final String FAIL_VALIDATION_INPUT_COUNT = "./test/order/fail/validation-input-count.yaml";
    public static final String FAIL_VALIDATION_NONPOSITIVE_INPUT_LENGTH = "./test/order/fail/validation-nonpositive-input-length.yaml";
    public static final String FAIL_VALIDATION_NONPOSITIVE_OUTPUT_LENGTH = "./test/order/fail/validation-nonpositive-output-length.yaml";
    public static final String FAIL_VALIDATION_NONNEGATIVE_INPUT_COUNT = "./test/order/fail/validation-nonnegative-input-count.yaml";
    public static final String FAIL_VALIDATION_NONNEGATIVE_OUTPUT_COUNT = "./test/order/fail/validation-nonnegative-output-count.yaml";
    public static final String FAIL_VALIDATION_OUTPUT_LONGER_THAN_INPUT = "./test/order/fail/validation-output-longer-than-input.yaml";

    public static final String EXAMPLE_ORDER = "./test/order/success/default.yaml";
    public static final String EXAMPLE_FFD_RELAX_ORDER = "./test/order/success/default-ffd-relax.yaml";
    public static final String EXAMPLE_INPUT_COUNT_ORDER = "./test/order/success/input-count.yaml";
    public static final String EXAMPLE_INPUT_COUNT_ZEROS_ORDER = "./test/order/success/input-count-zeros.yaml";

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
    public static final String CG_INT_RELAX_1_PLAN = "./test/plan/relax/cg-int-relax-1.yaml";

    public static final String FFD_RELAX_PLAN = "./test/plan/relax/ffd-relax.yaml";
    public static final String FFD_INT_RELAX_PLAN = "./test/plan/relax/ffd-int-relax.yaml";

    public static final String GREEDY_RELAX_0_PLAN = "./test/plan/relax/greedy-relax-0.yaml";
    public static final String GREEDY_RELAX_1_PLAN = "./test/plan/relax/greedy-relax-1.yaml";
    public static final String GREEDY_INT_RELAX_0_PLAN = "./test/plan/relax/greedy-int-relax-0.yaml";

    public static final String FFD_INPUT_COUNT_PLAN = "./test/plan/ffd-input-count.yaml";
    public static final String GREEDY_INPUT_COUNT_PLAN = "./test/plan/greedy-input-count.yaml";
    public static final String CG_INPUT_COUNT_PLAN = "./test/plan/cg-input-count.yaml";

    public static final String FFD_INPUT_COUNT_ZEROS_PLAN = "./test/plan/ffd-input-count-zeros.yaml";
    public static final String GREEDY_CG_INPUT_COUNT_ZEROS_PLAN = "./test/plan/greedy-cg-input-count-zeros.yaml";

    public static final String CUTGEN_SIMPLE_ORDER = "./test/order/cutgen/simple-order.yaml";
    public static final String CUTGEN_MULTI_INPUT_ORDER = "./test/order/cutgen/multi-input-order.yaml";
    public static final String CUTGEN_ALLOW_INPUT_DUPLICATES = "./test/order/cutgen/input-duplicates-order.yaml";
    public static final String CUTGEN_ALLOW_OUTPUT_DUPLICATES = "./test/order/cutgen/output-duplicates-order.yaml";

}
