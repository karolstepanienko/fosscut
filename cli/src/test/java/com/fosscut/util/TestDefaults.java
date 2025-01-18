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

    public static final String FAIL_VALIDATION_NON_POSITIVE_INPUT = "./test/order/fail-validation-non-positive-input.yaml";
    public static final String FAIL_VALIDATION_NON_POSITIVE_OUTPUT = "./test/order/fail-validation-non-positive-output.yaml";
    public static final String FAIL_VALIDATION_OUTPUT_LONGER_THAN_INPUT = "./test/order/fail-validation-output-longer-than-input.yaml";
    public static final String EXAMPLE_REDIS_CONNECTION_SECRETS = "./test/example-redis-connection-secrets.yaml";

    public static final String EXAMPLE_ORDER = "./test/order/example-cutting-order.yaml";
    public static final String CG_PLAN = "./test/plan/cg-cutting-plan.yaml";
    public static final String FFD_PLAN = "./test/plan/ffd-cutting-plan.yaml";
    public static final String GREEDY_PLAN = "./test/plan/greedy-cutting-plan.yaml";

    public static final String CG_RELAX_0_PLAN = "./test/plan/relax/cg-relax-0-cutting-plan.yaml";
    public static final String CG_RELAX_1_PLAN = "./test/plan/relax/cg-relax-1-cutting-plan.yaml";
    public static final String CG_INT_RELAX_1_PLAN = "./test/plan/relax/cg-int-relax-1-cutting-plan.yaml";

    public static final String GREEDY_RELAX_0_PLAN = "./test/plan/relax/greedy-relax-0-cutting-plan.yaml";
    public static final String GREEDY_RELAX_1_PLAN = "./test/plan/relax/greedy-relax-1-cutting-plan.yaml";
    public static final String GREEDY_INT_RELAX_0_PLAN = "./test/plan/relax/greedy-int-relax-0-cutting-plan.yaml";

}
