package com.fosscut.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import com.fosscut.shared.util.save.SaveFile;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class CloudCommand extends ResultsFilesBefore {

    private String testName;
    private String xAxisLabel;
    private String orderCommand;
    private String planCommand;
    private String cpu;
    private String memory;
    private boolean enableLogging;

    private String resultsFolderName;
    private RedisClient redisClient;
    private int failedRuns;

    public CloudCommand(String testName, String xAxisLabel, String orderCommand, String planCommand) {
        this.resultsFolderName = prepareResultsFolderName(testName);
        this.testName = testName;
        this.xAxisLabel = xAxisLabel;
        this.orderCommand = orderCommand;
        this.planCommand = planCommand;
        this.cpu = PerformanceDefaults.DEFAULT_CPU;
        this.memory = PerformanceDefaults.DEFAULT_MEMORY;
        this.enableLogging = false;
        this.redisClient = new RedisClient();
        this.failedRuns = 0;
    }

    public CloudCommand(String testName, String xAxisLabel, String orderCommand, String planCommand, String cpu, String memory) {
        this.resultsFolderName = prepareResultsFolderName(testName);
        this.testName = testName;
        this.xAxisLabel = xAxisLabel;
        this.orderCommand = orderCommand;
        this.planCommand = planCommand;
        this.cpu = cpu;
        this.memory = memory;
        this.enableLogging = false;
        this.redisClient = new RedisClient();
        this.failedRuns = 0;
    }

    public CloudCommand(String testName, String xAxisLabel, String orderCommand, String planCommand, String cpu, String memory, boolean enableLogging) {
        this.resultsFolderName = prepareResultsFolderName(testName);
        this.testName = testName;
        this.xAxisLabel = xAxisLabel;
        this.orderCommand = orderCommand;
        this.planCommand = planCommand;
        this.cpu = cpu;
        this.memory = memory;
        this.enableLogging = enableLogging;
        this.redisClient = new RedisClient();
        this.failedRuns = 0;
    }

    public boolean run(LinkedHashMap<Integer, Integer> seeds) throws InterruptedException {
        return runAllGivenSeeds(removeAlreadyExecutedRuns(seeds));
    }

    public boolean run(LinkedList<Integer> seeds) throws InterruptedException {
        return runAllGivenSeeds(removeAlreadyExecutedRuns(seeds, 1));
    }

    public boolean run(LinkedList<Integer> seeds, int nRunsInit) throws InterruptedException {
        return runAllGivenSeeds(removeAlreadyExecutedRuns(seeds, nRunsInit));
    }

    // each seed will be run eachSeedRunsEnd - eachSeedRunsStart times
    // use for examples where generated results are non-deterministic
    public boolean run(LinkedList<Integer> seeds, int nRunsInit,
        int eachSeedRunsStart, int eachSeedRunsEnd)
    throws InterruptedException {
        LinkedHashMap<Integer, Integer> finalSeedsMap = generateFinalSeedsMap(
            seeds, nRunsInit, eachSeedRunsStart, eachSeedRunsEnd);
        finalSeedsMap = removeAlreadyExecutedRuns(
            seeds, finalSeedsMap, nRunsInit, eachSeedRunsStart, eachSeedRunsEnd);
        return runAllGivenSeeds(finalSeedsMap);
    }

    private boolean runAllGivenSeeds(LinkedHashMap<Integer, Integer> seeds) throws InterruptedException {
        try (KubernetesClient k8sClient = new KubernetesClientBuilder().build()) {
            seeds.entrySet().parallelStream().forEach(seed -> {
                try {
                    new FosscutTestPod(getPodName(seed), enableLogging, cpu, memory)
                        .runSingleCommand(k8sClient, buildCommand(seed));
                    downloadFromRedis(seed);
                } catch (InterruptedException | RuntimeException | IOException e) {
                    Thread.currentThread().interrupt();
                    failedRuns++;
                }
            });
        }
        saveCloudCommandResultsReport(seeds.size());
        return failedRuns == 0;
    }

    private LinkedHashMap<Integer, Integer> removeAlreadyExecutedRuns(
        LinkedHashMap<Integer, Integer> seeds) {
        ResultsReport report = new ResultsReport(testName, new ArrayList<>(),
            seeds
        );
        report.generateReportData();
        LinkedHashMap<Integer, Integer> missingSeedsRuns =
            report.getMissingRuns().get(xAxisLabel.substring(1));

        if (missingSeedsRuns == null) return seeds;
        else return missingSeedsRuns;
    }

    private LinkedHashMap<Integer, Integer> removeAlreadyExecutedRuns(
        LinkedList<Integer> seeds, int nRunsInit
    ) {
        ResultsReport report = new ResultsReport(testName, new ArrayList<>(),
            seeds, nRunsInit
        );
        report.generateReportData();
        LinkedHashMap<Integer, Integer> missingSeedsRuns =
            report.getMissingRuns().get(xAxisLabel.substring(1));

        if (missingSeedsRuns == null) return generateFinalSeedsMap(seeds, nRunsInit);
        else return missingSeedsRuns;
    }

    private LinkedHashMap<Integer, Integer> removeAlreadyExecutedRuns(
        LinkedList<Integer> seeds,
        LinkedHashMap<Integer, Integer> finalSeedsMap,
        int nRunsInit, int eachSeedRunsStart, int eachSeedRunsEnd
    ) {
        ResultsReport report = new ResultsReport(testName, new ArrayList<>(),
            seeds, nRunsInit, eachSeedRunsStart, eachSeedRunsEnd
        );
        report.generateReportData();
        LinkedHashMap<Integer, Integer> missingSeedsRuns =
            report.getMissingRuns().get(xAxisLabel.substring(1));

    
        if (missingSeedsRuns == null) return finalSeedsMap;
        else return missingSeedsRuns;
    }

    private String getPodName(Map.Entry<Integer, Integer> seed) {
        // Has to be lowercase since Kubernetes requires pod names to be lowercase
        return getRedisKey(testName, xAxisLabel,seed).toLowerCase();
    }

    private String buildCommand(Map.Entry<Integer, Integer> seed) {
        return buildOrderCommand(seed) + " && " + buildPlanCommand(seed);
    }

    private String buildOrderCommand(Map.Entry<Integer, Integer> seed) {
        return PerformanceDefaults.CLI_TOOL_PATH
            + " " + orderCommand  + " --seed " + seed.getValue()
            + " " + PerformanceDefaults.CLOUD_REDIS_SECRETS_PATH
            + " -o " + PerformanceDefaults.CLOUD_REDIS_URL + getRedisKey(testName, xAxisLabel, seed);
    }

    private String buildPlanCommand(Map.Entry<Integer, Integer> seed) {
        return PerformanceDefaults.CLI_TOOL_PATH
            + " " + planCommand
            + " " + PerformanceDefaults.CLOUD_REDIS_SECRETS_PATH
            + " " + PerformanceDefaults.CLOUD_REDIS_URL + getRedisKey(testName, xAxisLabel, seed);
    }

    private String prepareResultsFolderName(String resultsFolderName) {
        if (!resultsFolderName.endsWith(System.getProperty("file.separator")))
            return resultsFolderName + System.getProperty("file.separator");
        else
            return resultsFolderName;
    }

    private void downloadFromRedis(Map.Entry<Integer, Integer> seed)
        throws RuntimeException, IOException
    {
        String targetDir = PerformanceDefaults.RESULTS_PATH + resultsFolderName;

        String plan = redisClient.getPlan(getRedisKey(testName, xAxisLabel, seed));
        SaveFile.saveContentToFile(plan, targetDir + getPlanFileName(testName, xAxisLabel, seed));

        String order = redisClient.getOrder(getRedisKey(testName, xAxisLabel, seed));
        SaveFile.saveContentToFile(order, targetDir + getOrderFileName(testName, xAxisLabel, seed));
    }

    private void saveCloudCommandResultsReport(int totalRuns) {
        String reportContent = "CloudCommand Report " + xAxisLabel + ": "
            + "Total Runs: " + totalRuns + "; "
            + "Failed Runs: " + failedRuns + "; "
            + "Successful Runs: " + (totalRuns - failedRuns) + ";\n";

        String reportFilePath = PerformanceDefaults.RESULTS_PATH
            + resultsFolderName + "CloudCommand_" + xAxisLabel + "_Report.txt";

        SaveFile.saveContentToFile(reportContent, reportFilePath);
    }

}
