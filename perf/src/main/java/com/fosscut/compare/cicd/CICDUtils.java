package com.fosscut.compare.cicd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fosscut.shared.util.save.SaveFile;
import com.fosscut.shared.util.save.YamlDumper;
import com.fosscut.utils.PerformanceDefaults;

public class CICDUtils {

    private String testName;
    private String RUN_ID;
    private int NUM_PARTS;

    public CICDUtils(String testName, String RUN_ID, int NUM_PARTS) {
        this.testName = testName;
        this.RUN_ID = RUN_ID;
        this.NUM_PARTS = NUM_PARTS;
    }

    public List<String> generateIdentifiers() {
        List<String> identifiers = new ArrayList<>();
        for (int part = 0; part < NUM_PARTS; part++) {
            identifiers.add("run-" + RUN_ID + "-part-" + part);
        }
        return identifiers;
    }

    public void saveStartTimestampToFile() {
        String startTimestamp = Instant.now().toString();
        SaveFile.saveContentToFile(startTimestamp,
            getStartTimestampFilePath()
        );
    }

    public Instant loadStartTimestampFromFile() throws IOException {
        String startTimestampYaml = Files.readString(Paths.get(getStartTimestampFilePath()));
        return Instant.parse(startTimestampYaml);
    }

    public void saveReport(CICDReport cicdReport) {
        YamlDumper yamlDumper = new YamlDumper();
        String reportYamlString = yamlDumper.dump(cicdReport);
        String reportFilePath = PerformanceDefaults.getResultsFolder(testName)
            + System.getProperty("file.separator")
            + "x" + NUM_PARTS + "-" + RUN_ID + "-report.yaml";
        SaveFile.saveContentToFile(reportYamlString, reportFilePath);
    }

    private String getStartTimestampFilePath() {
        return PerformanceDefaults.getResultsFolder(testName)
            + System.getProperty("file.separator")
            + "x" + NUM_PARTS + "-" + RUN_ID + "-startTimestamp.txt";
    }

}
