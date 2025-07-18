import { useEffect, useState } from "react";
import { getApi } from "../../Config.ts";
import TektonTaskRunLogs, { SetTektonTaskRunLogsFunction } from "../../type/tekton/TektonTaskRunLogs.ts";
import Settings from "../Settings.tsx";
import { SetSettingsExtendedFunction } from "../../type/SettingsExtended.ts";
import TektonApi from "../../communication/TektonApi.tsx";
import Backend, { SetBackendFunction } from "../../enum/Backend.ts";
import AirflowApi from "../../communication/AirflowApi.tsx";
import AirflowDAGLogs, { SetAirflowDAGLogsFunction } from "../../type/airflow/AirflowDAGLogs.ts";
import JenkinsApi from "../../communication/JenkinsApi.tsx";
import JenkinsJobLogs, { SetJenkinsJobLogsFunction } from "../../type/jenkins/JenkinsJobLogs.ts";

type GenerateActionProps = {
  backend : string,
  setBackend: SetBackendFunction,
  airflowDAGLogs: AirflowDAGLogs,
  setAirflowDAGLogs: SetAirflowDAGLogsFunction,
  jenkinsJobLogs: JenkinsJobLogs,
  setJenkinsJobLogs: SetJenkinsJobLogsFunction,
  tektonTaskRunLogs: TektonTaskRunLogs,
  setTektonTaskRunLogs: SetTektonTaskRunLogsFunction,
  settingsExtended: boolean,
  setSettingsExtended: SetSettingsExtendedFunction
}

const GenerateAction: React.FC<GenerateActionProps>
  = ({backend, setBackend, airflowDAGLogs, setAirflowDAGLogs,
    jenkinsJobLogs, setJenkinsJobLogs, tektonTaskRunLogs, setTektonTaskRunLogs,
    settingsExtended, setSettingsExtended
  }) => {
  const api = getApi();
  const airflowApi = AirflowApi({airflowDAGLogs, setAirflowDAGLogs});
  const tektonApi = TektonApi({tektonTaskRunLogs, setTektonTaskRunLogs});
  const jenkinsApi = JenkinsApi({jenkinsJobLogs, setJenkinsJobLogs});
  const [orderAvailable, setOrderAvailable] = useState<boolean>(false);
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
    } else if (backend === Backend.JENKINS) {
      jenkinsApi.sendJobRunRequest()
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
    } else if (backend === Backend.JENKINS) {
      return jenkinsApi.renderSummary()
    } else if (backend === Backend.TEKTON) {
      return tektonApi.renderSummary()
    }
  }

  const renderLogs = () => {
    if (backend === Backend.AIRFLOW) {
      return airflowApi.renderLogs();
    } else if (backend === Backend.JENKINS) {
      return jenkinsApi.renderLogs();
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
