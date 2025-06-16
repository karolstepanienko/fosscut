import Input from "./Input.ts";
import Output from "./Output.ts";

class Order {
  inputs: Input[] = [];
  outputs: Output[] = [];

  constructor(order: { inputs?: Input[], outputs?: Output[] }) {
    if (order.inputs && order.outputs) {
      this.inputs = this.generateIds(order.inputs) as Input[];
      this.outputs = this.generateIds(order.outputs) as Output[];
    } else {
      if (!order.inputs && !order.outputs) {
        throw new SyntaxError("Incorrect format of order elements. Order must contain both inputs and outputs.");
      } else if (!order.inputs) {
        throw new SyntaxError("Incorrect format of input elements.");
      } else if (!order.outputs) {
        throw new SyntaxError("Incorrect format of output elements.");
      }
    }
  }

  generateIds(elements: Input[]| Output[]): Input[] | Output[] {
    return elements.map((element, i) => ({ ...element, id: i }) as Input | Output);
  }

  getInputs(): Input[] {
    return this.inputs;
  }

  setInputs(inputs: Input[]): void {
    this.inputs = inputs;
  }

  getOutputs(): Output[] {
    return this.outputs;
  }

  setOutputs(outputs: Output[]): void {
    this.outputs = outputs;
  }

}

export default Order;
