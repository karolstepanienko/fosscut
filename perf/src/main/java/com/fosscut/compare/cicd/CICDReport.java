package com.fosscut.compare.cicd;

import com.fosscut.utils.ResultsReport;
import java.util.List;

public class CICDReport {

    private List<CICDReportLine> reportLines;
    private CICDReportMetadata metadata;

    public CICDReport(List<CICDReportLine> reportLines) {
        this.reportLines = reportLines;
        this.metadata = new CICDReportMetadata(reportLines);
    }

    public void saveReport(String testName) {
        String reportString = "";
        reportString += metadata.toString();
        reportString += "\n" + String.join("\n", reportLines.stream().map(Object::toString).toList());
        ResultsReport.saveReportToFile(reportString, testName);
    }

}
