import { useEffect, useState } from "react";
import { useCookies } from 'react-cookie';

import { getApi } from "../Config.ts";
import { AxiosError } from "axios";
import AirflowDAGLogs, { SetAirflowDAGLogsFunction } from "../type/airflow/AirflowDAGLogs.ts";
import AirflowDAGLogsDTO from "../type/airflow/AirflowDAGLogsDTO.ts";

type AirflowApiProps = {
  airflowDAGLogs: AirflowDAGLogs,
  setAirflowDAGLogs: SetAirflowDAGLogsFunction
}

const AirflowApi = ({airflowDAGLogs, setAirflowDAGLogs}: AirflowApiProps) => {
  const api = getApi();
  const [_, setCookie] = useCookies(['fosscut_dagRunID']);
  const [ticking, setTicking] = useState<boolean>(false);
  const [count, setCount] = useState<number>(0);

  useEffect(() => { runTimer() }, [count, ticking])

  const runTimer = () => {
    const timer = setTimeout(() => ticking && setCount(count+1), 1e3)
    if (ticking) getLogs()
    return () => clearTimeout(timer)
  }

  const sendDagRunRequest = async () => {
    try {
      setAirflowDAGLogs(new AirflowDAGLogs())
      const response = await api.post('/airflow/dag/run')
      setTicking(true)
      setCookie('fosscut_dagRunID', response.data)
    } catch (error: unknown | AxiosError) {
      console.error("Error starting DAG run:", error)
      setTicking(false)
    }
  }

  const getLogs = () => {
    sendGetLogsRequest()
    if (airflowDAGLogs && airflowDAGLogs.isFinished()) {
      // if success or failure
      setTicking(false)
    }
  }

  const sendGetLogsRequest = async () => {
    try {
      const data = (await api.get("/airflow/dag/logs")).data
      const dto = AirflowDAGLogsDTO.parse(data)
      const al = new AirflowDAGLogs(dto)
      setAirflowDAGLogs(al)
    } catch (error: unknown | AxiosError) {
      console.error("Error fetching logs:", error);
    }
  }

  const renderSummary = () => {
    if (airflowDAGLogs && airflowDAGLogs.isInitialized()) {
      return (
        <div className="summary-container">
          <p>Status: {airflowDAGLogs.status}</p>
        </div>
      )
    } else if (ticking) {
      // no summary for Airflow, logs are returned directly
      return (
        <div className="summary-container">
          <p>DAG run started. Waiting for logs...</p>
        </div>
      )
    }
  }

  const renderLogs = () => {
    if (airflowDAGLogs && airflowDAGLogs.logsPresent()) {
      return (
        <div className="logs-container">
          <p className="logs">{airflowDAGLogs.logs}</p>
        </div>
      );
    }
  }

  return {
    sendDagRunRequest: sendDagRunRequest,
    renderSummary: renderSummary,
    renderLogs: renderLogs
  }
}

export default AirflowApi;
