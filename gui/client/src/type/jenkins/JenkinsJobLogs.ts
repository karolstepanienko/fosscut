import JenkinsJobLogsDTO from "./JenkinsJobLogsDTO.ts";

type SetJenkinsJobLogsFunction = (logs: JenkinsJobLogs) => void;

class JenkinsJobLogs {
  httpStatusCode: number = 0;
  jobNumberIdentifier: number = 0;
  status: string | null = null;
  building: string | null = null;
  result: string | null = null;
  logs: string | null = null;

  constructor(jenkinsJobLogsDTO?: JenkinsJobLogsDTO) {
    if (jenkinsJobLogsDTO) {
      this.httpStatusCode = jenkinsJobLogsDTO.httpStatusCode;
      this.jobNumberIdentifier = jenkinsJobLogsDTO.jobNumberIdentifier;
      this.status = jenkinsJobLogsDTO.status;
      this.building = jenkinsJobLogsDTO.building;
      this.result = jenkinsJobLogsDTO.result;
      this.logs = jenkinsJobLogsDTO.logs;
    }
  }

  renderSummary(): boolean {
    return this.status !== null || this.result !== null;
  }

  isFinished(): boolean {
    return this.building === 'false';
  }
}

export type { SetJenkinsJobLogsFunction };

export default JenkinsJobLogs;
