import { useEffect, useState } from "react";
import { getApi } from "../../Config.ts";
import TektonTaskRunLogsDTO from "../../type/TektonTaskRunLogsDTO.ts";
import { AxiosError, HttpStatusCode, isAxiosError } from "axios";

const GenerateAction = () => {
  const api = getApi();
  const [orderAvailable, setOrderAvailable] = useState<boolean>(false);
  const [taskRunToBeDeleted, setTaskRunToBeDeleted] = useState<boolean>(false);
  const [taskRunToBeCreated, setTaskRunToBeCreated] = useState<boolean>(false);
  const [ticking, setTicking] = useState<boolean>(false);
  const [count, setCount] = useState<number>(0);
  const [tektonTaskRunLogsDTO, setTektonTaskRunLogsDTO] = useState<TektonTaskRunLogsDTO>(undefined);

  useEffect(() => { checkOrderAvailable() }, [])
  useEffect(() => { runTimer() }, [count, ticking])
  useEffect(() => { if (taskRunToBeDeleted) sendDeleteTaskRunRequest() }, [taskRunToBeDeleted])
  useEffect(() => { if (taskRunToBeCreated) sendCreateTaskRunRequest() }, [taskRunToBeCreated])

  const runTimer = () => {
    const timer = setTimeout(() => ticking && setCount(count+1), 1e3)
    if (ticking) getLogs()
    return () => clearTimeout(timer)
  }

  const checkOrderAvailable = async () => {
    const oa = await sendCheckOrderAvailableRequest()
    if (oa) setOrderAvailable(oa)
  }

  const sendCheckOrderAvailableRequest = async () => {
    const orderSavedResponse = await api.get("/redis/check/order/saved")
    return orderSavedResponse.data
  }

  const generatePlan = () => {
    setTektonTaskRunLogsDTO(undefined)
    setTaskRunToBeDeleted(true)
  }

  const sendDeleteTaskRunRequest = async () => {
    try {
      await api.get("/tekton/taskRun/delete").then (() => {
        setTaskRunToBeDeleted(false)
        setTaskRunToBeCreated(true)
      })
    } catch (error: unknown | AxiosError) {
      setTaskRunToBeDeleted(true)
      if (isAxiosError(error) && error.response?.status != HttpStatusCode.Conflict) {
        console.log("Unknown error:", error)
      }
    }
  }

  const sendCreateTaskRunRequest = async () => {
    try {
      await api.get("/tekton/taskRun/create").then(() => {
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
    if (tektonTaskRunLogsDTO && tektonTaskRunLogsDTO.status !== "Unknown") {
      // if success or failure
      setTicking(false)
    }
  }

  const sendGetLogsRequest = async () => {
    let tl = undefined
    try {
      tl = (await api.get("/tekton/taskRun/get/logs")).data as TektonTaskRunLogsDTO
    } catch (error: unknown) { console.log(error) }
    if (tl) setTektonTaskRunLogsDTO(tl)
  }

  const renderOrderUnavailableMessage = () => {
    if (!orderAvailable) {
      return (
        <p className="warning">Order was not found. Please try saving it.</p>
      );
    } else return (<></>);
  }

  const renderSummary = () => {
    if (tektonTaskRunLogsDTO) {
      return (
        <div className="summary-container">
          <p>Status: {tektonTaskRunLogsDTO.status}</p>
          <p>Reason: {tektonTaskRunLogsDTO.reason}</p>
        </div>
      );
    }
  }

  const renderLogs = () => {
    if (tektonTaskRunLogsDTO && tektonTaskRunLogsDTO?.logs !== "") {
      return (
        <div className="logs-container">
          <p className="logs">{tektonTaskRunLogsDTO.logs}</p>
        </div>
      );
    } else return (<></>);
  }

  return (
    <div className="action-container">
      <button className="btn btn-secondary fosscut-button button-group"
        disabled={!orderAvailable} onClick={() => generatePlan()}>
        Generate
      </button>
      {renderOrderUnavailableMessage()}
      {renderSummary()}
      <div className="action-content-container">
        {renderLogs()}
      </div>
    </div>
  );
}

export default GenerateAction;
