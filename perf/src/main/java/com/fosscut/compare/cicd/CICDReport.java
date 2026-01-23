package com.fosscut.compare.cicd;

import java.time.Instant;
import java.util.List;

public class CICDReport {

    private CICDReportMetadata metadata;
    private List<CICDReportLine> reportLines;

    public CICDReport() {}

    public CICDReport(List<CICDReportLine> reportLines, Instant startTimestamp) {
        this.reportLines = reportLines;
        this.metadata = new CICDReportMetadata(reportLines, startTimestamp);
    }

    public CICDReportMetadata getMetadata() {
        return metadata;
    }

    public List<CICDReportLine> getReportLines() {
        return reportLines;
    }

}
