import NoIdInput from './NoIdInput.tsx'

type Input = NoIdInput & {
  id: number
};

const getNoIdInputs = (inputs: Input[]): NoIdInput[] => {
    var noIdInputs: NoIdInput[] = []
    inputs.map((input: Input) => {
        noIdInputs.push({
            length: input.length
        })
    })
    return noIdInputs;
}

export { getNoIdInputs };

export default Input;
