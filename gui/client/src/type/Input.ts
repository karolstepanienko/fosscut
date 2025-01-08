import NoIdInput from './NoIdInput.ts'

type SetInputsFunction = (inputs: Input[]) => void;

type Input = NoIdInput & {
  id: number
};

const getNoIdInputs = (inputs: Input[]): NoIdInput[] => {
    const noIdInputs: NoIdInput[] = []
    inputs.map((input: Input) => {
        noIdInputs.push({
            length: input.length
        })
    })
    return noIdInputs;
}

export { getNoIdInputs };
export type { SetInputsFunction };

export default Input;
