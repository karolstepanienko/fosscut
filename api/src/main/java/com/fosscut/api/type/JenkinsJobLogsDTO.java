package com.fosscut.api.type;

public class JenkinsJobLogsDTO {

    int httpStatusCode;
    Integer jobNumberIdentifier;
    String status;
    String building;
    String result;
    String logs;

    public JenkinsJobLogsDTO() {}

    public JenkinsJobLogsDTO(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        this.jobNumberIdentifier = null;
        this.status = null;
        this.building = null;
        this.result = null;
        this.logs = null;
    }

    public JenkinsJobLogsDTO(int httpStatusCode, Integer jobNumberIdentifier, String status, String building, String result, String logs) {
        this.httpStatusCode = httpStatusCode;
        this.jobNumberIdentifier = jobNumberIdentifier;
        this.status = status;
        this.building = building;
        this.result = result;
        this.logs = logs;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public Integer getJobNumberIdentifier() {
        return jobNumberIdentifier;
    }

    public String getStatus() {
        return status;
    }

    public String getBuilding() {
        return building;
    }

    public String getResult() {
        return result;
    }

    public String getLogs() {
        return logs;
    }

}
