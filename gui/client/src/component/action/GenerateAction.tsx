import { useEffect, useState } from "react";
import { getApi } from "../../Config.ts";
import TektonTaskRunLogsDTO from "../../type/TektonTaskRunLogsDTO.ts";
import TektonTaskRunLogs, { SetTektonTaskRunLogsFunction } from "../../type/TektonTaskRunLogs.ts";
import { AxiosError, HttpStatusCode, isAxiosError } from "axios";
import Settings from "../Settings.tsx";
import { SetSettingsExtendedFunction } from "../../type/SettingsExtended.ts";

type GenerateActionProps = {
  tektonTaskRunLogs: TektonTaskRunLogs,
  setTektonTaskRunLogs: SetTektonTaskRunLogsFunction,
  settingsExtended: boolean,
  setSettingsExtended: SetSettingsExtendedFunction
}

const GenerateAction: React.FC<GenerateActionProps>
  = ({tektonTaskRunLogs, setTektonTaskRunLogs, settingsExtended, setSettingsExtended}) => {
  const api = getApi();
  const [orderAvailable, setOrderAvailable] = useState<boolean>(false);
  const [taskRunToBeDeleted, setTaskRunToBeDeleted] = useState<boolean>(false);
  const [taskRunToBeCreated, setTaskRunToBeCreated] = useState<boolean>(false);
  const [ticking, setTicking] = useState<boolean>(false);
  const [count, setCount] = useState<number>(0);

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
    setTektonTaskRunLogs(new TektonTaskRunLogs())
    setTaskRunToBeDeleted(true)
  }

  const sendDeleteTaskRunRequest = async () => {
    try {
      await api.get("/tekton/taskRun/delete").then (() => {
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
    if (tektonTaskRunLogs && tektonTaskRunLogs.isInitialized() && tektonTaskRunLogs.status !== "Unknown") {
      // if success or failure
      setTicking(false)
    }
  }

  const sendGetLogsRequest = async () => {
    let tl = new TektonTaskRunLogs(undefined);
    try {
      const data = (await api.get("/tekton/taskRun/get/logs")).data
      const dto = TektonTaskRunLogsDTO.parse(data)
      tl = new TektonTaskRunLogs(dto)
    } catch (error: unknown) { console.log(error) }
    if (tl) setTektonTaskRunLogs(tl)
  }

  const renderOrderUnavailableMessage = () => {
    if (!orderAvailable) {
      return (
        <p className="warning">Order was not found. Please try saving it.</p>
      );
    } else return (<div><div/></div>);
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

  return (
    <div className="action-container">
      <Settings settingsExtended={settingsExtended} setSettingsExtended={setSettingsExtended} />
      <button className="btn btn-secondary fosscut-button button-group"
        type="button" disabled={!orderAvailable} onClick={() => generatePlan()}>
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
