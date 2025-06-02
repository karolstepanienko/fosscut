type SetTektonTaskRunLogsDTOFunction = (tektonTaskRunLogsDTO: TektonTaskRunLogsDTO) => void;

class TektonTaskRunLogsDTO {
  status: string = '';
  reason: string = '';
  logs: string = '';

  isInitialized(): boolean {
    return this.status !== '' || this.reason !== '' || this.logs !== '';
  }
}

export type { SetTektonTaskRunLogsDTOFunction };

export default TektonTaskRunLogsDTO;
