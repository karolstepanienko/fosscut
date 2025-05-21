package com.fosscut.api.type;

public class JenkinsJobLogsDTO {

    int httpStatusCode; 
    String jobNumberIdentifier;
    String status;
    String logs;

    public JenkinsJobLogsDTO(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        this.jobNumberIdentifier = null;
        this.status = null;
        this.logs = null;
    }

    public JenkinsJobLogsDTO(int httpStatusCode, String jobNumberIdentifier, String status, String logs) {
        this.httpStatusCode = httpStatusCode;
        this.jobNumberIdentifier = jobNumberIdentifier;
        this.status = status;
        this.logs = logs;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getJobNumberIdentifier() {
        return jobNumberIdentifier;
    }

    public String getStatus() {
        return status;
    }

    public String getLogs() {
        return logs;
    }

}
