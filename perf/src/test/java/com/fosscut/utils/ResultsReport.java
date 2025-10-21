package com.fosscut.utils;

import java.io.File;
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

    private LinkedHashMap<Integer, Integer> finalSeedsMap;
    private LinkedList<String> expectedPlanFileNames;

    public ResultsReport(String testName,
        List<String> ignoredXAxisLabels,
        LinkedHashMap<Integer, Integer> seeds) {
        super(testName);
        this.ignoredXAxisLabels = ignoredXAxisLabels;
        this.finalSeedsMap = generateFinalSeedsMap(seeds, 1, 1, 1);
    }

    public ResultsReport(String testName,
        List<String> ignoredXAxisLabels,
        LinkedHashMap<Integer, Integer> seeds,
        int eachSeedRuns, int eachSeedRunsStart, int eachSeedRunsEnd) {
        super(testName);
        this.ignoredXAxisLabels = ignoredXAxisLabels;
        this.finalSeedsMap = generateFinalSeedsMap(seeds, eachSeedRuns, eachSeedRunsStart, eachSeedRunsEnd);
    }

    public void generateReport() {
        LinkedHashMap<String, LinkedHashMap<String, String>> missingRuns = findMissingRuns();
        List<String> incorrectFileNames = findIncorrectFileNames();
        StringBuilder reportContent = new StringBuilder();
        reportContent.append(generateRerunCommands(missingRuns));
        reportContent.append(generateRemoveCommands(incorrectFileNames));
        SaveFile.saveContentToFile(reportContent.toString(),
            resultsFolder + System.getProperty("file.separator")
            + "report.sh");
    }

    public LinkedHashMap<String, LinkedHashMap<String, String>> findMissingRuns() {
        expectedPlanFileNames = new LinkedList<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> missingRuns = new LinkedHashMap<>();
        for (String xAxisLabel : xAxisLabels) {
            if (ignoredXAxisLabels.contains(xAxisLabel)) { continue; }
            for (Map.Entry<Integer, Integer> entry : finalSeedsMap.entrySet()) {
                String expectedPlanFileName = getPlanFileName(testName, "x" + xAxisLabel, entry);
                expectedPlanFileNames.add(expectedPlanFileName);
                boolean planFileExists = planFiles.stream().anyMatch(file -> file.getName().equals(expectedPlanFileName));
                if (!planFileExists) {
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
                }
            }
        }
        return missingRuns;
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
            sb.append("# Missing runs for xAxisLabel: ").append(xAxisLabel).append("\n");
            sb.append("assertTrue(cmd.run(LinkedHashMap_of(");
            LinkedHashMap<String, String> runsMap = xAxisEntry.getValue();
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
        }
        return sb.toString();
    }

    public String generateRemoveCommands(List<String> incorrectFileNames) {
        StringBuilder sb = new StringBuilder();
        for (String fileName : incorrectFileNames) {
            sb.append("rm ").append(fileName).append("\n");
        }
        return sb.toString();
    }

}
