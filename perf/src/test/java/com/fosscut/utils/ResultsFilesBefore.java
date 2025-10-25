package com.fosscut.utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class ResultsFilesBefore {

    public static LinkedHashMap<Integer, Integer> generateFinalSeedsMap(
        LinkedList<Integer> seeds, int nRunsInit
    ) {
        LinkedHashMap<Integer, Integer> newSeedsMap = new LinkedHashMap<>();
        int run = nRunsInit;
        for (Integer seed : seeds) {
            newSeedsMap.put(run, seed);
            run++;
        }
        return newSeedsMap;
    }

    public static LinkedHashMap<Integer, Integer> generateFinalSeedsMap(
        LinkedList<Integer> seeds, int nRunsInit,
        int eachSeedRunsStart, int eachSeedRunsEnd
    ) {
        LinkedHashMap<Integer, Integer> newSeedsMap = new LinkedHashMap<>();
        int run = nRunsInit;
        for (Integer seed : seeds) {
            run += eachSeedRunsStart;
            for (int i = eachSeedRunsStart; i <= eachSeedRunsEnd; i++) {
                newSeedsMap.put(run, seed);
                run++;
            }
            run += nRunsInit - eachSeedRunsEnd - 1;
        }
        return newSeedsMap;
    }

    protected String getRedisKey(String testName, String xAxisLabel, Map.Entry<Integer, Integer> seed) {
        return testName + xAxisLabel + getRunIdentifier(seed);
    }

    protected String getRunIdentifier(Map.Entry<Integer, Integer> seed) {
        return PerformanceDefaults.RESULTS_RUN_PREFIX + seed.getKey() + "-seed-" + seed.getValue();
    }

    protected String getOrderFileName(String testName, String xAxisLabel, Map.Entry<Integer, Integer> seed) {
        return getRedisKey(testName, xAxisLabel, seed) + PerformanceDefaults.RESULTS_ORDER_SUFFIX;
    }

    protected String getPlanFileName(String testName, String xAxisLabel, Map.Entry<Integer, Integer> seed) {
        return getRedisKey(testName, xAxisLabel, seed) + PerformanceDefaults.RESULTS_PLAN_SUFFIX;
    }

}
