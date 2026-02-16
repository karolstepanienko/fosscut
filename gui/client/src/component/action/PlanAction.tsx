import { useEffect, useState } from 'react';
import { AxiosError, HttpStatusCode, isAxiosError } from "axios";
import yaml from 'yaml';

import { getApi } from "../../Config.ts";
import PlanTableRow, { getPlanTableDataFromCuttingPlan } from "../../type/PlanTableRow.ts";
import CuttingPlan from "../../type/CuttingPlan.ts";
import PlanTable from "../PlanTable.tsx";
import LinkButton from "../LinkButton.tsx";
import { objectToUrl } from "../../YamlUtils.ts";

const PlanAction = () => {
  const api = getApi();
  const [cuttingPlan, setCuttingPlan] = useState<CuttingPlan>();
  const [cuttingPlanUrl, setCuttingPlanUrl] = useState<string>('#');
  const [planTableData, setPlanTableData] = useState<PlanTableRow[]>([]);
  const [errorMessage, setErrorMessage] = useState<string>('');

  useEffect(() => { getPlan() }, [])
  useEffect(() => { refreshPlanTableData() }, [cuttingPlan])

  const getPlan = async () => {
    const planString = await sendGetPlanRequest()
    if (planString != undefined) {
      const cuttingPlan = stringToCuttingPlan(planString);
      setCuttingPlan(cuttingPlan);
      setCuttingPlanUrl(objectToUrl(cuttingPlan));
      setErrorMessage('');
    }
  }

  const sendGetPlanRequest = async () => {
    // identifier sent through cookies
    let planResponse = undefined
    try {
      planResponse = await api.get("/redis/get/plan")
    } catch (error: unknown | AxiosError) {
      if (isAxiosError(error) && error.response?.status == HttpStatusCode.NotFound) {
        handlePlanNotFoundError();
      } else {
        handleGetPlanError();
      }
    }
    return planResponse?.data
  }

  const stringToCuttingPlan = (planString: string): CuttingPlan => {
    return yaml.parse(planString) as CuttingPlan;
  }

  const handlePlanNotFoundError = () => {
    setErrorMessage("Plan not found. Please try generating it.")
  }

  const handleGetPlanError = () => {
    setErrorMessage("Unknown error. Please try again later.")
  }

  const refreshPlanTableData = () => {
    if (cuttingPlan != undefined) {
      setPlanTableData(getPlanTableDataFromCuttingPlan(cuttingPlan))
    }
  }

  const renderError = () => {
    if (errorMessage != '') {
      return (
        <p className="warning">{errorMessage}</p>
      );
    } else return (<div><div/></div>);
  }

  const renderMetadata = () => {
    if (cuttingPlan != undefined) {
      return (
        <div className="metadata-container">
          <h3>Plan metadata</h3>
          <dl>
            <div>
              <dt>Elapsed time</dt>
              <dd>{cuttingPlan.metadata.elapsedTimeMilliseconds} ms</dd>
            </div>
            <div>
              <dt>Timestamp</dt>
              <dd>{cuttingPlan.metadata.timestamp}</dd>
            </div>
            <div>
              <dt>Input count</dt>
              <dd>{cuttingPlan.metadata.inputCount}</dd>
            </div>
            <div>
              <dt>Output count</dt>
              <dd>{cuttingPlan.metadata.outputCount}</dd>
            </div>
            <div>
              <dt>Input types</dt>
              <dd>{cuttingPlan.metadata.inputTypeCount}</dd>
            </div>
            <div>
              <dt>Output types</dt>
              <dd>{cuttingPlan.metadata.outputTypeCount}</dd>
            </div>
            <div>
              <dt>Total waste</dt>
              <dd>{cuttingPlan.metadata.totalWaste}</dd>
            </div>
            <div>
              <dt>Needed input length</dt>
              <dd>{cuttingPlan.metadata.totalNeededInputLength}</dd>
            </div>
            <div>
              <dt>Memory usage peak (megabytes)</dt>
              <dd>{Math.round(cuttingPlan.metadata.memoryUsagePeakBytes / (1024 * 1024))}</dd>
            </div>
            <div>
              <dt>Status</dt>
              <dd className={`status ${cuttingPlan.metadata.planStatus.toLowerCase()}`}>
                {cuttingPlan.metadata.planStatus}
              </dd>
            </div>
          </dl>
        </div>
      );
    }
  }

  return (
    <div className="action-container">
      {renderError()}
      {renderMetadata()}
      <div className="action-content-container">
        <PlanTable planTableData={planTableData}/>
      </div>
      <LinkButton
        href={cuttingPlanUrl}
        download="cutting_plan.yaml"
        enabled={cuttingPlanUrl != '#'}>
        Download
      </LinkButton>
    </div>
  );
}

export default PlanAction;
