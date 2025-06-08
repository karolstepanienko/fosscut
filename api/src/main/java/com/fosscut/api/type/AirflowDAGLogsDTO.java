package com.fosscut.api.type;

public class AirflowDAGLogsDTO {

    private String status;
    private String logs;

    public AirflowDAGLogsDTO() {}

    public AirflowDAGLogsDTO(String status, String logs) {
        this.status = status;
        this.logs = logs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

}
