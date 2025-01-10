import CuttingPlan, { PlanInput, PlanOutput, Pattern } from "./CuttingPlan.ts";
import PlanTableOutputRow from "./PlanTableOutputRow.ts";

type PlanTableRow = {
    patternId: number,
    inputLength: number,
    count: number,
    outputs: PlanTableOutputRow[]
}

const getPlanTableDataFromCuttingPlan = (cuttingPlan: CuttingPlan): PlanTableRow[] => {
  let localId = 0;
  const planTableData: PlanTableRow[] = []
  cuttingPlan.inputs.map((planInput: PlanInput) => {
    planInput.patterns.map((pattern: Pattern) => {

      const localOutputs: PlanTableOutputRow[] = []
      pattern.patternDefinition.map((planOutput: PlanOutput) => {
        localOutputs.push({
          id: planOutput.id,
          length: cuttingPlan.outputs[planOutput.id].length,
          count: planOutput.count
        })
      })

      planTableData.push({
        patternId: localId,
        inputLength: planInput.length,
        count: pattern.count,
        outputs: localOutputs
      })
      localId += 1
    })
  })
  return planTableData;
}

export { getPlanTableDataFromCuttingPlan };

export default PlanTableRow;
