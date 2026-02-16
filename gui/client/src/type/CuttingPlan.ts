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

type PlanMetadata = {
    elapsedTimeMilliseconds: number,
    timestamp: string,
    inputCount: number,
    outputCount: number,
    inputTypeCount: number,
    outputTypeCount: number,
    totalWaste: number,
    totalNeededInputLength: number,
    planStatus: string
    memoryUsagePeakBytes: number,
}

type CuttingPlan = {
    inputs: PlanInput[],
    outputs: Output[],
    metadata: PlanMetadata
}

export type { PlanInput, PlanOutput, Pattern, PlanMetadata };

export default CuttingPlan;
