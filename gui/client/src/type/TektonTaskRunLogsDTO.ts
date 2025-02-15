type SetTektonTaskRunLogsDTOFunction = (tektonTaskRunLogsDTO: TektonTaskRunLogsDTO | undefined) => void;

type TektonTaskRunLogsDTO = {
  status: string,
  reason: string,
  logs: string
}

export type { SetTektonTaskRunLogsDTOFunction };

export default TektonTaskRunLogsDTO;
