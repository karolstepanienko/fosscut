import TektonTaskRunLogsDTO from "./TektonTaskRunLogsDTO.ts";

type SetTektonTaskRunLogsFunction = (tektonTaskRunLogs: TektonTaskRunLogs) => void;

class TektonTaskRunLogs {
  status: string = '';
  reason: string = '';
  logs: string = '';

  constructor(tektonTaskRunLogsDTO?: TektonTaskRunLogsDTO) {
    if (tektonTaskRunLogsDTO) {
      this.status = tektonTaskRunLogsDTO.status;
      this.reason = tektonTaskRunLogsDTO.reason;
      this.logs = tektonTaskRunLogsDTO.logs;
    }
  }

  isInitialized(): boolean {
    return this.status !== '' || this.reason !== '' || this.logs !== '';
  }
}

export type { SetTektonTaskRunLogsFunction };

export default TektonTaskRunLogs;
