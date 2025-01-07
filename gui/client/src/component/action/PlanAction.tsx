import { useEffect, useState } from 'react';
import yaml from 'yaml';

import { getApi } from "../../Config.ts";
import PlanTableRow, { getPlanTableDataFromCuttingPlan } from "../../type/PlanTableRow.ts";
import CuttingPlan from "../../type/CuttingPlan.ts";
import PlanTable from "../PlanTable.tsx";
import { AxiosError, HttpStatusCode, isAxiosError } from "axios";

const PlanAction = () => {
  const api = getApi();
  const [cuttingPlan, setCuttingPlan] = useState<CuttingPlan>(undefined);
  const [planTableData, setPlanTableData] = useState<PlanTableRow[]>([]);
  const [errorMessage, setErrorMessage] = useState<string>('');

  useEffect(() => { getPlan() }, [])
  useEffect(() => { refreshPlanTableData() }, [cuttingPlan])

  const getPlan = async () => {
    const planString = await sendGetPlanRequest()
    if (planString != undefined) {
      setCuttingPlan(yaml.parse(planString) as CuttingPlan)
      setErrorMessage('')
    }
  }

  const sendGetPlanRequest = async () => {
    // identifier sent through cookies
    let planResponse = undefined
    try {
      planResponse = await api.get("/redis/get/plan")
    } catch (error: unknown | AxiosError) {
      if (isAxiosError(error) && error.response?.status == HttpStatusCode.NotFound) {
        handlePlanNotFoundError()
      } else {
        handleGetPlanError(error)
      }
    }
    return planResponse?.data
  }

  const handlePlanNotFoundError = () => {
    setErrorMessage("Plan not found. Please try generating it.")
  }

  const handleGetPlanError = (error: unknown) => {
    console.log("Unknown error:", error)
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
    } else return (<></>);
  }

  return (
    <div className="action-container">
      {renderError()}
      <div className="action-content-container">
        <PlanTable planTableData={planTableData}/>
      </div>
    </div>
  );
}

export default PlanAction;
