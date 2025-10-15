package com.fosscut.utils;

import java.util.Map;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class CloudCommand {

    private String testName;
    private String orderCommand;
    private String planCommand;
    private String cpu;
    private String memory;
    private boolean enableLogging;

    public CloudCommand(String testName, String orderCommand, String planCommand) {
        this.testName = testName;
        this.orderCommand = orderCommand;
        this.planCommand = planCommand;
        this.cpu = PerformanceDefaults.DEFAULT_CPU;
        this.memory = PerformanceDefaults.DEFAULT_MEMORY;
        this.enableLogging = false;
    }

    public CloudCommand(String testName, String orderCommand, String planCommand, String cpu, String memory) {
        this.testName = testName;
        this.orderCommand = orderCommand;
        this.planCommand = planCommand;
        this.cpu = cpu;
        this.memory = memory;
        this.enableLogging = false;
    }

    public CloudCommand(String testName, String orderCommand, String planCommand, String cpu, String memory, boolean enableLogging) {
        this.testName = testName;
        this.orderCommand = orderCommand;
        this.planCommand = planCommand;
        this.cpu = cpu;
        this.memory = memory;
        this.enableLogging = enableLogging;
    }

    public void run(Map<Integer, Integer> seeds) throws InterruptedException {
        try (KubernetesClient k8sClient = new KubernetesClientBuilder().build()) {
            seeds.entrySet().parallelStream().forEach(seed -> {
                try {
                    new FosscutTestPod(getPodName(seed), enableLogging, cpu, memory)
                        .runSingleCommand(k8sClient, buildCommand(seed));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private String getPodName(Map.Entry<Integer, Integer> seed) {
        // Has to be lowercase since Kubernetes requires pod names to be lowercase
        return getRedisKey(seed).toLowerCase();
    }

    private String buildCommand(Map.Entry<Integer, Integer> seed) {
        return buildOrderCommand(seed) + " && " + buildPlanCommand(seed);
    }

    private String buildOrderCommand(Map.Entry<Integer, Integer> seed) {
        return PerformanceDefaults.CLI_TOOL_PATH
            + " " + orderCommand  + " --seed " + seed.getValue()
            + " " + PerformanceDefaults.CLOUD_REDIS_SECRETS_PATH
            + " -o " + PerformanceDefaults.CLOUD_REDIS_URL + getRedisKey(seed);
    }

    private String buildPlanCommand(Map.Entry<Integer, Integer> seed) {
        return PerformanceDefaults.CLI_TOOL_PATH
            + " " + planCommand
            + " " + PerformanceDefaults.CLOUD_REDIS_SECRETS_PATH
            + " " + PerformanceDefaults.CLOUD_REDIS_URL + getRedisKey(seed);
    }

    private String getRedisKey(Map.Entry<Integer, Integer> seed) {
        return testName + getRunIdentifier(seed);
    }

    private String getRunIdentifier(Map.Entry<Integer, Integer> seed) {
        return "-run-" + seed.getKey() + "-seed-" + seed.getValue();
    }

}
