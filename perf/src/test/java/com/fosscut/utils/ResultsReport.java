package com.fosscut.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fosscut.shared.util.save.SaveFile;

// Creates a report.txt file with list of all missing test runs that failed
// with a command to easily rerun them
// and list all duplicated test run files with commands to remove them
public class ResultsReport extends ResultsFilesAfter {

    private List<String> ignoredXAxisLabels;

    private LinkedHashMap<String, LinkedHashMap<String, String>> missingRuns;
    private LinkedHashMap<String, LinkedHashMap<String, String>> timeoutRuns;
    private LinkedHashMap<Integer, Integer> finalSeedsMap;
    private LinkedList<String> expectedPlanFileNames;

    public ResultsReport(String testName,
        List<String> ignoredXAxisLabels,
        LinkedHashMap<Integer, Integer> seeds) {
        super(testName);
        this.ignoredXAxisLabels = ignoredXAxisLabels;
        this.finalSeedsMap = seeds;
    }

    public ResultsReport(String testName,
        List<String> ignoredXAxisLabels,
        LinkedList<Integer> seeds) {
        super(testName);
        this.ignoredXAxisLabels = ignoredXAxisLabels;
        this.finalSeedsMap = generateFinalSeedsMap(seeds, 1);
    }

    public ResultsReport(String testName,
        List<String> ignoredXAxisLabels,
        LinkedList<Integer> seeds, int nRunsInit) {
        super(testName);
        this.ignoredXAxisLabels = ignoredXAxisLabels;
        this.finalSeedsMap = generateFinalSeedsMap(seeds, nRunsInit);
    }

    public ResultsReport(String testName,
        List<String> ignoredXAxisLabels,
        LinkedList<Integer> seeds,
        int nRunsInit, int eachSeedRunsStart, int eachSeedRunsEnd) {
        super(testName);
        this.ignoredXAxisLabels = ignoredXAxisLabels;
        this.finalSeedsMap = generateFinalSeedsMap(seeds, nRunsInit, eachSeedRunsStart, eachSeedRunsEnd);
    }

    public void generateReport() {
        findMissingRuns();
        List<String> incorrectFileNames = findIncorrectFileNames();
        StringBuilder reportContent = new StringBuilder();
        reportContent.append(generateRerunCommands(missingRuns));
        reportContent.append(generateRemoveCommands(incorrectFileNames));
        SaveFile.saveContentToFile(reportContent.toString(),
            resultsFolder + System.getProperty("file.separator")
            + "report.sh");
    }

    public void findMissingRuns() {
        expectedPlanFileNames = new LinkedList<>();
        missingRuns = new LinkedHashMap<>();
        timeoutRuns = new LinkedHashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            if (ignoredXAxisLabels.contains(xAxisLabel)) { continue; }
            for (Map.Entry<Integer, Integer> entry : finalSeedsMap.entrySet()) {
                String expectedPlanFileName = getPlanFileName(testName, "x" + xAxisLabel, entry);
                expectedPlanFileNames.add(expectedPlanFileName);

                boolean planFileExists = false;
                boolean isTimeout = false;
                for (File file : planFiles) {
                    if (file.getName().equals(expectedPlanFileName)) {
                        planFileExists = true;
                        try {
                            String content = Files.readString(file.getAbsoluteFile().toPath());
                            if (content.contains("planStatus: \"TIMEOUT\"")) {
                                isTimeout = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!planFileExists || isTimeout) {
                    // missing run found
                    String run = String.valueOf(entry.getKey());
                    String seed = String.valueOf(entry.getValue());
                    if (missingRuns.containsKey(xAxisLabel)) {
                        missingRuns.get(xAxisLabel).put(run, seed);
                    } else {
                        missingRuns.put(xAxisLabel, new LinkedHashMap<String, String>() {{
                            put(run, seed);
                        }});
                    }
                    // timeout run found
                    if (isTimeout) {
                        if (timeoutRuns.containsKey(xAxisLabel)) {
                            timeoutRuns.get(xAxisLabel).put(run, seed);
                        } else {
                            timeoutRuns.put(xAxisLabel, new LinkedHashMap<String, String>() {{
                                put(run, seed);
                            }});
                        }
                    }
                }
            }
        }
    }

    public List<String> findIncorrectFileNames() {
        List<String> incorrectFileNames = new ArrayList<>();
        for (File file : planFiles) {
            String fileName = file.getName();
            if (!expectedPlanFileNames.contains(fileName)) {
                // incorrect file found
                boolean isIgnored = false;
                for (String ignoredXAxisLabel : ignoredXAxisLabels) {
                    if (fileName.contains("x" + ignoredXAxisLabel)) {
                        isIgnored = true;
                        break; // skip ignored xAxisLabels
                    }
                }
                if (!isIgnored) {
                    incorrectFileNames.add(fileName);
                }
            }
        }
        return incorrectFileNames;
    }

    public String generateRerunCommands(LinkedHashMap<String, LinkedHashMap<String, String>> missingRuns) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, LinkedHashMap<String, String>> xAxisEntry : missingRuns.entrySet()) {
            String xAxisLabel = xAxisEntry.getKey();
            sb.append("# xAxisLabel: ").append(xAxisLabel).append(", line 1: missing runs (including timeout runs), line 2: only timeout runs").append("\n");

            sb = generateRerunCommand(sb, xAxisEntry.getValue());
            if (timeoutRuns.containsKey(xAxisLabel)) {
                sb = generateRerunCommand(sb, timeoutRuns.get(xAxisLabel));
            }
        }
        return sb.toString();
    }

    private StringBuilder generateRerunCommand(StringBuilder sb,
        LinkedHashMap<String, String> runsMap) {
        sb.append("# assertTrue(cmd.run(LinkedHashMap_of(");
        for (Map.Entry<String, String> runEntry : runsMap.entrySet()) {
            String run = runEntry.getKey();
            String seed = runEntry.getValue();
            sb.append(run).append(", ").append(seed).append(", ");
        }
        // remove last comma and space if present
        int len = sb.length();
        if (len >= 2 && sb.substring(len - 2, len).equals(", ")) {
            sb.delete(len - 2, len);
        }
        sb.append(")));\n");
        return sb;
    }

    public String generateRemoveCommands(List<String> incorrectFileNames) {
        StringBuilder sb = new StringBuilder();
        for (String fileName : incorrectFileNames) {
            // remove order command
            sb.append("rm ").append(fileName.replace(
                PerformanceDefaults.RESULTS_PLAN_SUFFIX,
                PerformanceDefaults.RESULTS_ORDER_SUFFIX)
            ).append("\n");
            // remove plan command
            sb.append("rm ").append(fileName).append("\n");
        }
        return sb.toString();
    }

}
