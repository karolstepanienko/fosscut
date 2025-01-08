import yaml from 'yaml';

import { getApi } from "../../Config.ts";
import Order from "../../type/Order.ts";
import Input, { getNoIdInputs, SetInputsFunction } from "../../type/Input.ts";
import Output, { getNoIdOutputs, SetOutputsFunction } from "../../type/Output.ts";
import InputList from "../list/InputList.tsx";
import OutputList from "../list/OutputList.tsx";
import ActionCookieProps from "../../type/ActionCookieProps.ts";
import SetIdFunction from "../../type/SetIdFunction.ts";

type OrderActionProps = ActionCookieProps & {
  inputId: number,
  setInputId: SetIdFunction,
  inputs: Input[],
  setInputs: SetInputsFunction,
  outputId: number,
  setOutputId: SetIdFunction,
  outputs: Output[],
  setOutputs: SetOutputsFunction
}

const OrderAction: React.FC<OrderActionProps> = ({
    inputId, setInputId,
    inputs, setInputs,
    outputId, setOutputId,
    outputs, setOutputs,
    cookies, setCookie
  }) => {
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
        <InputList
          inputId={inputId} setInputId={setInputId}
          inputs={inputs} setInputs={setInputs}
        />
        <OutputList
          outputId={outputId} setOutputId={setOutputId}
          outputs={outputs} setOutputs={setOutputs}
        />
      </div>
      <button className="btn btn-secondary fosscut-button button-group" onClick={() => saveOrder()}>
      Save</button>
    </div>
  );
}

export default OrderAction;
