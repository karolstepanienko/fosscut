import { useEffect, useState } from "react";
import { getApi } from "../../Config.ts";
import TektonTaskRunLogs, { SetTektonTaskRunLogsFunction } from "../../type/tekton/TektonTaskRunLogs.ts";
import Settings from "../Settings.tsx";
import { SetSettingsExtendedFunction } from "../../type/SettingsExtended.ts";
import TektonApi from "../../communication/TektonApi.tsx";
import Backend from "../../enum/Backend.ts";
import AirflowApi from "../../communication/AirflowApi.tsx";
import AirflowDAGLogs, { SetAirflowDAGLogsFunction } from "../../type/airflow/AirflowDAGLogs.ts";

type GenerateActionProps = {
  airflowDAGLogs: AirflowDAGLogs,
  setAirflowDAGLogs: SetAirflowDAGLogsFunction,
  tektonTaskRunLogs: TektonTaskRunLogs,
  setTektonTaskRunLogs: SetTektonTaskRunLogsFunction,
  settingsExtended: boolean,
  setSettingsExtended: SetSettingsExtendedFunction
}

const GenerateAction: React.FC<GenerateActionProps>
  = ({airflowDAGLogs, setAirflowDAGLogs, tektonTaskRunLogs, setTektonTaskRunLogs, settingsExtended, setSettingsExtended}) => {
  const api = getApi();
  const airflowApi = AirflowApi({airflowDAGLogs, setAirflowDAGLogs});
  const tektonApi = TektonApi({tektonTaskRunLogs, setTektonTaskRunLogs});
  const [orderAvailable, setOrderAvailable] = useState<boolean>(false);
  const [backend, setBackend] = useState<string>(Backend.TEKTON);
  useEffect(() => { checkOrderAvailable() }, [])

  const checkOrderAvailable = async () => {
    const oa = await sendCheckOrderAvailableRequest()
    if (oa) setOrderAvailable(oa)
  }

  const sendCheckOrderAvailableRequest = async () => {
    const orderSavedResponse = await api.get("/redis/check/order/saved")
    return orderSavedResponse.data
  }

  const generatePlan = () => {
    if (backend === Backend.AIRFLOW) {
      airflowApi.sendDagRunRequest()
    } else if (backend === Backend.TEKTON) {
      tektonApi.sendDagRunRequest()
    }
  }

  const renderOrderUnavailableMessage = () => {
    if (!orderAvailable) {
      return (
        <p className="warning">Order was not found. Please try saving it.</p>
      );
    } else return (<div><div/></div>);
  }

  const renderSummary = () => {
    if (backend === Backend.AIRFLOW) {
      return airflowApi.renderSummary()
    } else if (backend === Backend.TEKTON) {
      return tektonApi.renderSummary()
    }
  }

  const renderLogs = () => {
    if (backend === Backend.AIRFLOW) {
      return airflowApi.renderLogs();
    } else if (backend === Backend.TEKTON) {
      return tektonApi.renderLogs();
    }
  }

  return (
    <div className="action-container">
      <Settings settingsExtended={settingsExtended} setSettingsExtended={setSettingsExtended}
        backend={backend} setBackend={setBackend} />
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
