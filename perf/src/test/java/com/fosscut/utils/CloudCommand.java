package com.fosscut.utils;

import java.util.List;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class CloudCommand {

    private String testName;
    private String orderCommand;
    private String generatorCommand;
    private boolean enableLogging;

    public CloudCommand(String testName, String orderCommand, String generatorCommand) {
        this.testName = testName;
        this.orderCommand = orderCommand;
        this.generatorCommand = generatorCommand;
        this.enableLogging = false;
    }

    public CloudCommand(String testName, String orderCommand, String generatorCommand, boolean enableLogging) {
        this.testName = testName;
        this.orderCommand = orderCommand;
        this.generatorCommand = generatorCommand;
        this.enableLogging = enableLogging;
    }

    public void run(List<String> seeds) throws InterruptedException {
        try (KubernetesClient k8sClient = new KubernetesClientBuilder().build()) {
            seeds.parallelStream().forEach(seed -> {
                try {
                    new FosscutTestPod(getPodName(seed), enableLogging)
                        .runSingleCommand(k8sClient, buildCommand(seed));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private String getPodName(String seed) {
        // Has to be lowercase since Kubernetes requires pod names to be lowercase
        return testName.toLowerCase() + "-pod-run-" + seed;
    }

    private String buildCommand(String seed) {
        return orderCommand + " && echo 'Seed " + seed + "' && " + generatorCommand;
    }

}
