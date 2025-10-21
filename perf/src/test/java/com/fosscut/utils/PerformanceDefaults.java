package com.fosscut.utils;

public class PerformanceDefaults {

    public static final String DEFAULT_NAMESPACE = "performance";
    public static final long DEFAULT_CLOUD_SCHEDULING_TIMEOUT = 8L; // hours
    public static final long DEFAULT_CLOUD_EXECUTION_TIMEOUT = 10L; // minutes

    public static final String CLI_TOOL_PATH = "fosscut";
    public static final String CLOUD_REDIS_URL = "redis://redis-replicas.redis.svc.cluster.local:6379/";
    public static final String CLOUD_REDIS_SECRETS_PATH = "--redis-connection-secrets /secrets/redis-connection-secrets.yaml";

    public static final String RESULTS_PATH = "." + System.getProperty("file.separator") + "results" + System.getProperty("file.separator");
    public static final String RESULTS_ORDER_SUFFIX = "-order.yaml";
    public static final String RESULTS_PLAN_SUFFIX = "-plan.yaml";
    public static final String RESULTS_RUN_PREFIX = "-run-";
    public static final String RESULTS_PLOT_PATH = "../../fosscut-doc/tex/img/perf/";

    public static final String FOSSCUT_API_REDIS_URL = "https://haproxy-kubernetes-ingress.haproxy-controller/api/redis/get/";
    public static final String FOSSCUT_API_HOSTNAME = "fosscut.com";
    public static final String FOSSCUT_API_TRUSTSTORE_PATH = "../helm/secrets/haproxy-truststore.jks";

    public static final String DEFAULT_CPU = "2";
    public static final String DEFAULT_MEMORY = "5Gi";

}
