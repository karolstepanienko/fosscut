import { useEffect, useState } from "react";
import { getApi } from "../../Config.ts";
import TektonTaskRunLogs, { SetTektonTaskRunLogsFunction } from "../../type/TektonTaskRunLogs.ts";
import Settings from "../Settings.tsx";
import { SetSettingsExtendedFunction } from "../../type/SettingsExtended.ts";
import TektonApi from "../../communication/TektonApi.tsx";
import Backend from "../../enum/Backend.ts";

type GenerateActionProps = {
  tektonTaskRunLogs: TektonTaskRunLogs,
  setTektonTaskRunLogs: SetTektonTaskRunLogsFunction,
  settingsExtended: boolean,
  setSettingsExtended: SetSettingsExtendedFunction
}

const GenerateAction: React.FC<GenerateActionProps>
  = ({tektonTaskRunLogs, setTektonTaskRunLogs, settingsExtended, setSettingsExtended}) => {
  const api = getApi();
  const tektonApi = TektonApi({tektonTaskRunLogs, setTektonTaskRunLogs});
  const [orderAvailable, setOrderAvailable] = useState<boolean>(false);
  const [backend, setBackend] = useState<Backend>(Backend.TEKTON);
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
    setTektonTaskRunLogs(new TektonTaskRunLogs())
    tektonApi.setTaskRunToBeDeleted(true)
  }

  const renderOrderUnavailableMessage = () => {
    if (!orderAvailable) {
      return (
        <p className="warning">Order was not found. Please try saving it.</p>
      );
    } else return (<div><div/></div>);
  }

  const renderSummary = () => {
    return tektonApi.renderSummary()
  }

  const renderLogs = () => {
    return tektonApi.renderLogs();
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
