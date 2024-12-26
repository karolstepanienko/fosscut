import { useState } from 'react';
import { CookiesProvider, useCookies } from 'react-cookie';
import yaml from 'yaml';

import { getApi } from "../../Config.ts";
import Order from "../../type/Order.ts";
import Input, { getNoIdInputs } from "../../type/Input.ts";
import Output, { getNoIdOutputs } from "../../type/Output.ts";
import InputList from "../list/InputList.tsx";
import OutputList from "../list/OutputList.tsx";

function OrderAction() {
  const api = getApi();
  const [inputs, setInputs] = useState<Input[]>([
    { id: 0, length: 100 }
  ])
  const [outputs, setOutputs] = useState<Output[]>([
    { id: 0, length: 30, number: 2, maxRelax: 0 }
  ])

  const [cookies, setCookie] = useCookies(['fosscut_orderIdentifier']);

  const saveOrder = async () => {
    var orderIdentifier: string = ""

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
    var order: Order = {
        inputs: getNoIdInputs(inputs),
        outputs: getNoIdOutputs(outputs)
    }

    api.put("/redis/save/order", {
      identifier: orderIdentifier,
      order: yaml.stringify(order)
    })
  }

  return (
    <CookiesProvider>
      <div className="action-container">
        <div className="action-content-container">
          <InputList inputs={inputs} setInputs={setInputs} />
          <OutputList outputs={outputs} setOutputs={setOutputs} />
        </div>
        <button className="btn btn-secondary fosscut-button save-button" onClick={() => saveOrder()}>
        Save</button>
      </div>
    </CookiesProvider>
  );
}

export default OrderAction;
