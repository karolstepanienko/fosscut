import NoIdInput from "./NoIdInput.ts"
import NoIdOutput from "./NoIdOutput.ts"

type Order = {
    inputs: NoIdInput[],
    outputs: NoIdOutput[]
}

export default Order;
