import NoIdOutput from "./NoIdOutput.ts";

type SetOutputsFunction = (outputs: Output[]) => void;

type Output = NoIdOutput & {
  id: number
}

const getNoIdOutputs = (outputs: Output[]): NoIdOutput[] => {
    const noIdOutputs: NoIdOutput[] = []
    outputs.map((output: Output) => {
        noIdOutputs.push({
            length: output.length,
            number: output.number,
            maxRelax: output.maxRelax
        })
    })
    return noIdOutputs;
}

export { getNoIdOutputs };
export type { SetOutputsFunction };

export default Output;
