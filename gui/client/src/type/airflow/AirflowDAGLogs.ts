import AirflowDAGLogsDTO from "./AirflowDAGLogsDTO.ts";

type SetAirflowDAGLogsFunction = (airflowDAGLogs: AirflowDAGLogs) => void;

class AirflowDAGLogs {
  status: string = '';
  logs: string = '';

  constructor(airflowDAGLogsDTO?: AirflowDAGLogsDTO) {
    if (airflowDAGLogsDTO) {
      this.status = airflowDAGLogsDTO.status;
      this.logs = airflowDAGLogsDTO.logs;
    }
  }

  isInitialized(): boolean {
    return this.status !== '';
  }

  isFinished(): boolean {
    return this.status === 'success' || this.status === 'failed';
  }

  logsPresent(): boolean {
    return this.logs.trim() !== '';
  }
}

export type { SetAirflowDAGLogsFunction };

export default AirflowDAGLogs;
