import Output from "./Output.ts";

type PlanOutput = {
    id: number,
    count: number
}

type Pattern = {
    count: number
    patternDefinition: PlanOutput[]
}

type PlanInput = {
    length: number,
    patterns: Pattern[]
}

type CuttingPlan = {
    inputs: PlanInput[],
    outputs: Output[]
}

export type { PlanInput, PlanOutput, Pattern };

export default CuttingPlan;
