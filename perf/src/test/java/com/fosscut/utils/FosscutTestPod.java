package com.fosscut.utils;

import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.LogWatch;

public class FosscutTestPod {

    private static final Logger logger = LoggerFactory.getLogger(CloudCommand.class);
    private static final PrintStream logStream = new PrintStream(new LoggerOutputStream(logger));

    private String podName;
    private boolean enableLogging;
    private String cpu;
    private String memory;

    FosscutTestPod(String podName, boolean enableLogging, String cpu, String memory) {
        this.podName = podName;
        this.enableLogging = enableLogging;
        this.cpu = cpu;
        this.memory = memory;
    }

    public void runSingleCommand(KubernetesClient k8sClient, String fullCommand)
        throws InterruptedException {
        Pod pod = buildPod(fullCommand);
        createPod(k8sClient, pod);

        waitForPodScheduling(k8sClient);
        LogWatch watch = null;
        if (enableLogging) watch = attachToPodLogs(k8sClient);
        waitForPodCompletion(k8sClient);
        if (watch != null) watch.close();

        logPodStatus(k8sClient);
        deletePod(k8sClient);
    }

    private Pod buildPod(String fullCommand) {
        return new PodBuilder()
            .withNewMetadata()
                .withName(podName)
            .endMetadata()
            .withNewSpec()
                .addNewContainer()
                    .withName("fosscut-cli-container")
                    .withImage("karolstepanienko/fosscut-cli-native:0.0.1")
                    .withCommand("sh", "-c", fullCommand)
                    .withResources(getResourceRequirements())
                .endContainer()
                .withRestartPolicy("Never")
            .endSpec()
            .build();
    }

    private ResourceRequirements getResourceRequirements() {
        ResourceRequirements resources = new ResourceRequirements();
        resources.setLimits(Map.of(
            "cpu", new Quantity(cpu),
            "memory", new Quantity(memory)
        ));
        resources.setRequests(Map.of(
            "cpu", new Quantity(cpu),
            "memory", new Quantity(memory)
        ));
        return resources;
    }

    private void createPod(KubernetesClient k8sClient, Pod pod) {
        k8sClient.pods()
            .inNamespace(PerformanceDefaults.DEFAULT_NAMESPACE)
            .resource(pod).create();
        logger.info("Pod created: {}", pod.getMetadata().getName());
    }

    private void waitForPodScheduling(KubernetesClient k8sClient) {
        k8sClient.pods()
            .inNamespace(PerformanceDefaults.DEFAULT_NAMESPACE)
            .withName(podName)
            .waitUntilCondition(p -> isPodScheduled(p),
                    PerformanceDefaults.DEFAULT_CLOUD_SCHEDULING_TIMEOUT,
                    TimeUnit.HOURS);
    }

    private LogWatch attachToPodLogs(KubernetesClient k8sClient) {
        logger.info("Pod {} logs:", podName);
        LogWatch watch = k8sClient.pods()
                .inNamespace(PerformanceDefaults.DEFAULT_NAMESPACE)
                .withName(podName)
                .watchLog(logStream);

        return watch;
    }

    private boolean isPodScheduled(Pod pod) {
        return pod.getStatus() != null
            && pod.getStatus().getPhase() != null
            && !pod.getStatus().getPhase().equals("Pending");
    }

    private void waitForPodCompletion(KubernetesClient k8sClient) throws InterruptedException {
        k8sClient.pods()
            .inNamespace(PerformanceDefaults.DEFAULT_NAMESPACE)
            .withName(podName)
            .waitUntilCondition(
                p -> isPodFinished(p),
                PerformanceDefaults.DEFAULT_CLOUD_EXECUTION_TIMEOUT,
                TimeUnit.MINUTES
            );
    }

    private boolean isPodFinished(Pod pod) {
        if (pod.getStatus() == null || pod.getStatus().getPhase() == null) {
            return false;
        } else {
            String phase = pod.getStatus().getPhase();
            return "Succeeded".equals(phase) || "Failed".equals(phase);
        }
    }

    private void logPodStatus(KubernetesClient k8sClient) {
        Pod finishedPod = k8sClient.pods().inNamespace(PerformanceDefaults.DEFAULT_NAMESPACE).withName(podName).get();
        String phase = finishedPod.getStatus().getPhase();
        logger.info("Pod finished with status: " + phase);
    }

    private void deletePod(KubernetesClient k8sClient) {
        k8sClient.pods()
            .inNamespace(PerformanceDefaults.DEFAULT_NAMESPACE)
            .withName(podName)
            .delete();
        logger.info("Pod deleted: {}", podName);
    }

}
