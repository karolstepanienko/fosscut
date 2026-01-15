package com.fosscut.compare.cicd.tekton;

import java.time.Duration;
import java.time.Instant;

public class TektonCICDReportLine {
    public String name;
    public Instant creationTimestamp;
    public Instant completionTimestamp;
    public Duration duration;

    public TektonCICDReportLine(String name, Instant creationTimestamp, Instant completionTimestamp) {
        this.name = name;
        this.creationTimestamp = creationTimestamp;
        this.completionTimestamp = completionTimestamp;
        this.duration = Duration.between(creationTimestamp, completionTimestamp);
    }

    @Override public String toString() {
        int nSpaces = 50 - name.length();
        for (int i = 0; i < nSpaces; i++) {
            name += " ";
        }
        return name + creationTimestamp + "   " + completionTimestamp + "   " + duration;
    }

}
