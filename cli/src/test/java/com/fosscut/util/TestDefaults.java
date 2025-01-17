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

    public static final String SIMPLE_ORDER = "./test/order/simple-cutting-order.yaml";
    public static final String SIMPLE_CG_PLAN = "./test/plan/simple-cg-cutting-plan.yaml";
    public static final String SIMPLE_FFD_PLAN = "./test/plan/simple-ffd-cutting-plan.yaml";

    public static final String SIMPLE_CG_RELAX_0_PLAN = "./test/plan/relax/simple-cg-relax-0-cutting-plan.yaml";
    public static final String SIMPLE_CG_RELAX_1_PLAN = "./test/plan/relax/simple-cg-relax-1-cutting-plan.yaml";
    public static final String SIMPLE_CG_INT_RELAX_1_PLAN = "./test/plan/relax/simple-cg-int-relax-1-cutting-plan.yaml";

}
