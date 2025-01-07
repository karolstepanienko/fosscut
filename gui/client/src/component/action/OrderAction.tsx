import yaml from 'yaml';

import { getApi } from "../../Config.ts";
import Order from "../../type/Order.ts";
import Input, { getNoIdInputs } from "../../type/Input.ts";
import Output, { getNoIdOutputs } from "../../type/Output.ts";
import InputList from "../list/InputList.tsx";
import OutputList from "../list/OutputList.tsx";
import ActionCookieProps from "../../type/ActionCookieProps.ts";

type SetInputsFunction = (inputs: Input[]) => void;
type SetOutputsFunction = (inputs: Output[]) => void;

type OrderActionProps = ActionCookieProps & {
  inputs: Input[],
  setInputs: SetInputsFunction
  outputs: Output[],
  setOutputs: SetOutputsFunction
}

const OrderAction: React.FC<OrderActionProps> = ({inputs, setInputs, outputs, setOutputs, cookies, setCookie}) => {
  const api = getApi();

  const saveOrder = async () => {
    let orderIdentifier: string = ""

    if (!cookies.fosscut_orderIdentifier) {
      orderIdentifier = await sendGetIdentifierRequest()
      setCookie("fosscut_orderIdentifier", orderIdentifier)
    } else {
      orderIdentifier = cookies.fosscut_orderIdentifier
    }

    sendSaveOrderRequest(orderIdentifier)
  }

  const sendGetIdentifierRequest = async () => {
    const identifierResponse = await api.get("/redis/get/identifier")
    return identifierResponse.data
  }

  const sendSaveOrderRequest = async (orderIdentifier: string) => {
    const order: Order = {
        inputs: getNoIdInputs(inputs),
        outputs: getNoIdOutputs(outputs)
    }

    await api.put("/redis/save/order", {
      identifier: orderIdentifier,
      order: yaml.stringify(order)
    })
  }

  return (
    <div className="action-container">
      <div className="action-content-container">
        <InputList inputs={inputs} setInputs={setInputs} />
        <OutputList outputs={outputs} setOutputs={setOutputs} />
      </div>
      <button className="btn btn-secondary fosscut-button save-button" onClick={() => saveOrder()}>
      Save</button>
    </div>
  );
}

export default OrderAction;
