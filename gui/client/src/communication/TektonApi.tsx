import { useEffect, useState } from "react";
import { getApi } from "../Config.ts";
import TektonTaskRunLogs, { SetTektonTaskRunLogsFunction } from "../type/tekton/TektonTaskRunLogs.ts";
import TektonTaskRunLogsDTO from "../type/tekton/TektonTaskRunLogsDTO.ts";
import { AxiosError, HttpStatusCode, isAxiosError } from "axios";

type TektonApiProps = {
  tektonTaskRunLogs: TektonTaskRunLogs,
  setTektonTaskRunLogs: SetTektonTaskRunLogsFunction,
}

const TektonApi = ({ tektonTaskRunLogs, setTektonTaskRunLogs }: TektonApiProps) => {
  const api = getApi();
  const [taskRunToBeDeleted, setTaskRunToBeDeleted] = useState<boolean>(false);
  const [taskRunToBeCreated, setTaskRunToBeCreated] = useState<boolean>(false);
  const [ticking, setTicking] = useState<boolean>(false);
  const [count, setCount] = useState<number>(0);

  useEffect(() => { runTimer() }, [count, ticking])
  useEffect(() => { if (taskRunToBeDeleted) sendDeleteTaskRunRequest() }, [taskRunToBeDeleted])
  useEffect(() => { if (taskRunToBeCreated) sendCreateTaskRunRequest() }, [taskRunToBeCreated])

  const runTimer = () => {
    const timer = setTimeout(() => ticking && setCount(count+1), 1e3)
    if (ticking) getLogs()
    return () => clearTimeout(timer)
  }

  const sendDagRunRequest = () => {
    setTektonTaskRunLogs(new TektonTaskRunLogs())
    setTaskRunToBeDeleted(true)
  }

  const sendDeleteTaskRunRequest = async () => {
    try {
      await api.post("/tekton/taskRun/delete").then (() => {
        setTaskRunToBeDeleted(false)
        setTaskRunToBeCreated(true)
      })
    } catch (error: unknown | AxiosError) {
      if (isAxiosError(error)) {
        if (error.response?.status == HttpStatusCode.Conflict) {
          setTaskRunToBeDeleted(false)
          setTaskRunToBeCreated(true)
        } else setTaskRunToBeDeleted(true)
      } else {
        console.log("Unknown error:", error)
        setTaskRunToBeDeleted(true)
      }
    }
  }

  const sendCreateTaskRunRequest = async () => {
    try {
      await api.post("/tekton/taskRun/create").then(() => {
        setTaskRunToBeCreated(false)
        setTicking(true)
      })
    } catch (error: unknown | AxiosError) {
      setTaskRunToBeCreated(true)
      if (isAxiosError(error) && error.response?.status == HttpStatusCode.Conflict) {
        handleCreateTaskRunError(error)
      }
    }
  }

  const handleCreateTaskRunError = (error: unknown) => {
    console.log("Task run could not be created. Unknown error: ", error)
  }

  const getLogs = () => {
    sendGetLogsRequest()
    if (tektonTaskRunLogs
      && tektonTaskRunLogs.isInitialized()
      && tektonTaskRunLogs.status !== "Unknown"
    ) {
      // if success or failure
      setTicking(false)
    }
  }

  const sendGetLogsRequest = async () => {
    try {
      const data = (await api.get("/tekton/taskRun/logs")).data
      const dto = TektonTaskRunLogsDTO.parse(data)
      const tl = new TektonTaskRunLogs(dto)
      if (tl) setTektonTaskRunLogs(tl)
    } catch (error: unknown) { console.log(error) }
  }

  const renderSummary = () => {
    if (tektonTaskRunLogs && tektonTaskRunLogs.isInitialized()) {
      return (
        <div className="summary-container">
          <p>Status: {tektonTaskRunLogs.status}</p>
          <p>Reason: {tektonTaskRunLogs.reason}</p>
        </div>
      );
    }
  }

  const renderLogs = () => {
    if (tektonTaskRunLogs && tektonTaskRunLogs.isInitialized()) {
      return (
        <div className="logs-container">
          <p className="logs">{tektonTaskRunLogs.logs}</p>
        </div>
      );
    } else return (<div><div/></div>);
  }

  return {
    sendDagRunRequest: sendDagRunRequest,
    renderSummary: renderSummary,
    renderLogs: renderLogs
  }
}

export default TektonApi;
