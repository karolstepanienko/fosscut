package com.fosscut.utils;

public class PerformanceDefaults {

    // CLI
    public static final String DEFAULT_NAMESPACE = "performance";
    public static final long DEFAULT_CLOUD_SCHEDULING_TIMEOUT = 24L; // hours
    public static final long DEFAULT_CLOUD_EXECUTION_TIMEOUT = 20L; // minutes

    public static final String CLI_TOOL_PATH = "fosscut";
    public static final String CLOUD_REDIS_URL = "redis://redis-replicas.redis.svc.cluster.local:6379/";
    public static final String CLOUD_REDIS_SECRETS_PATH = "--redis-connection-secrets /secrets/redis-connection-secrets.yaml";

    public static final String RESULTS_PATH = "." + System.getProperty("file.separator") + "results" + System.getProperty("file.separator");
    public static final String RESULTS_ORDER_SUFFIX = "-order.yaml";
    public static final String RESULTS_PLAN_SUFFIX = "-plan.yaml";
    public static final String RESULTS_RUN_PREFIX = "-run-";
    public static final String RESULTS_SEED_PREFIX = "-seed-";
    public static final String RESULTS_PLOT_PATH = "../../fosscut-doc/tex/img/perf/";

    public static final String FOSSCUT_API_REDIS_URL = "https://haproxy-kubernetes-ingress.haproxy-controller/api/redis/get/";
    public static final String FOSSCUT_API_HOSTNAME = "fosscut.com";
    public static final String FOSSCUT_API_TRUSTSTORE_PATH = "../helm/secrets/haproxy-truststore.jks";
    public static final String FOSSCUT_INTERNAL_TRUSTSTORE_PATH = "../helm/secrets/truststore.p12";
    public static final String FOSSCUT_API_TRUSTSTORE_PASSWORD = "password";

    public static final String DEFAULT_CPU = "1";
    public static final String DEFAULT_MEMORY = "3Gi";

    public static final String GRAPH_X_LABEL_CPU = "Liczba wątków";
    public static final String GRAPH_X_LABEL_OUTPUT_TYPES = "Liczba typów elementów wyjściowych";
    public static final String GRAPH_Y_LABEL_CPU_TIME = "Średni czas [s]";
    public static final String GRAPH_Y_LABEL_CPU_WASTE = "Średni odpad [\\%]";
    public static final String GRAPH_Y_LABEL_MEMORY_USAGE_MEBI_BYTES = "Średnie zużycie pamięci [MiB]";
    public static final String GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES = "Średnie zużycie pamięci [GiB]";

    // CICD
    public static final String CICD_PERFORMANCE_TEKTON_TASK_RUN_NAME = "fosscut-cicd-performance";
    public static final String CICD_PERFORMANCE_TEKTON_TASK_RUN_NAME_PREFIX = CICD_PERFORMANCE_TEKTON_TASK_RUN_NAME + "-tekton-";

    public static final String CICD_PERFORMANCE_AIRFLOW_HOSTNAME = "airflow.fosscut.com";
    public static final String CICD_PERFORMANCE_AIRFLOW_PORT = "443";
    public static final String CICD_PERFORMANCE_AIRFLOW_DAG_ID = "fosscut_cicd_performance_kubernetes_executor";
    public static final String CICD_PERFORMANCE_AIRFLOW_CLEANUP_DAG_ID = "maintenance.cleanup_orphaned_logs";
    public static final String CICD_PERFORMANCE_AIRFLOW_USERNAME = "fosscut-api-local";
    public static final String CICD_PERFORMANCE_AIRFLOW_PASSWORD = "password";

    public static final String CICD_PERFORMANCE_JENKINS_HOSTNAME = "jenkins.fosscut.com";
    public static final String CICD_PERFORMANCE_JENKINS_PORT = "443";
    public static final String CICD_PERFORMANCE_JENKINS_JOB = "fosscut-cicd-performance";
    public static final String CICD_PERFORMANCE_JENKINS_USERNAME = "fosscut-api-local";
    public static final String CICD_PERFORMANCE_JENKINS_API_TOKEN = "115bc5d999f2b0e7a8b1b18e38bcbd34ed";

    public static String getResultsFolder(String testName) {
        return PerformanceDefaults.RESULTS_PATH + testName;
    }

}
