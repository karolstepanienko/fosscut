import NoIdOutput from "./NoIdOutput.ts";

type Output = NoIdOutput & {
  id: number
}

const getNoIdOutputs = (outputs: Output[]): NoIdOutput[] => {
    var noIdOutputs: NoIdOutput[] = []
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

export default Output;
