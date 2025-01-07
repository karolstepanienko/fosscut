import Output from "./Output.ts";

type PlanOutput = {
    id: number,
    number: number
}

type Pattern = {
    number: number
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
