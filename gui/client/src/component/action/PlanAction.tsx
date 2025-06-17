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

  return (
    <div className="action-container">
      {renderError()}
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
