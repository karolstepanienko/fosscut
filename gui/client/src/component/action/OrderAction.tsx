import yaml from 'yaml';

import { getApi } from "../../Config.ts";
import Order from "../../type/Order.ts";
import Input, { getNoIdInputs, SetInputsFunction } from "../../type/Input.ts";
import Output, { getNoIdOutputs, SetOutputsFunction } from "../../type/Output.ts";
import InputList from "../list/InputList.tsx";
import OutputList from "../list/OutputList.tsx";
import ActionCookieProps from "../../type/ActionCookieProps.ts";
import SetIdFunction from "../../type/SetIdFunction.ts";
import { ChangeEvent, useEffect, useState } from "react";
import LinkButton from "../LinkButton.tsx";
import { objectToUrl } from "../../YamlUtils.ts";

type OrderActionProps = ActionCookieProps & {
  inputId: number,
  setInputId: SetIdFunction,
  inputs: Input[],
  setInputs: SetInputsFunction,
  outputId: number,
  setOutputId: SetIdFunction,
  outputs: Output[],
  setOutputs: SetOutputsFunction,
}

const OrderAction: React.FC<OrderActionProps> = ({
    inputId, setInputId,
    inputs, setInputs,
    outputId, setOutputId,
    outputs, setOutputs,
    cookies, setCookie
  }) => {
  const api = getApi();

  const [orderFileName, setOrderFileName] = useState<string>('No file chosen');
  const [orderFileContent, setOrderFileContent] = useState<string | undefined>(undefined);

  const [cuttingOrderUrl, setCuttingOrderUrl] = useState<string>('#');

  const [warningVisible, setWarningVisible] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string>('');

  useEffect(() => {
    try {
      if (orderFileContent !== undefined) {
        const parsedOrder = yaml.parse(orderFileContent) as Order;
        const newOrder = new Order(parsedOrder);
        setInputs(newOrder.getInputs() || []);
        setOutputs(newOrder.getOutputs() || []);
        setInputId(inputId + newOrder.inputs.length);
        setOutputId(outputId + newOrder.outputs.length);
        setWarningVisible(false);
      }
    } catch (error) {
      setWarningVisible(true);
      setErrorMessage(error instanceof Error ? error.message : "Unknown error");
    }
  }, [orderFileContent]);

  useEffect(() => {
    const noIdInputs = getNoIdInputs(inputs);
    const noIdOutputs = getNoIdOutputs(outputs);
    const url = objectToUrl({ inputs: noIdInputs, outputs: noIdOutputs });
    setCuttingOrderUrl(url);
  }, [inputs, outputs]);

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
    const orderToSave = {
      inputs: getNoIdInputs(inputs),
      outputs: getNoIdOutputs(outputs)
    };

    await api.put("/redis/save/order", {
      identifier: orderIdentifier,
      order: yaml.stringify(orderToSave)
    })
  }

  const handleOrderFileLoad = (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    setOrderFileName(file.name);

    const reader = new FileReader();
    reader.readAsText(file);
    reader.onload = (e) => {
      const content = e.target?.result as string;
      setOrderFileContent(content);
    };
  };

  const renderWarning = () => {
    if (!warningVisible) return null;

    return (
      <div className="warning-container">
        <p className="warning">{errorMessage}</p>
      </div>
    );
  };

  return (
    <div className="action-container">
      <div>
        <div className="button-container">
          <label
            htmlFor="order-file-load"
            className="btn btn-secondary fosscut-button button-group button-in-container">
              Load order
          </label>
          <input
            className="btn btn-secondary fosscut-button button-group"
            style={{ display: 'none' }}
            id="order-file-load"
            type="file"
            onChange={handleOrderFileLoad}
            accept=".yaml"
          />
          <p className="btn btn-secondary fosscut-button button-group">Order file: {orderFileName}</p>
          {renderWarning()}
        </div>
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
        <div className="button-container">
          <button className="btn btn-secondary fosscut-button button-group"
            type="button" onClick={() => saveOrder()}>
            Save
          </button>
          <LinkButton
            href={cuttingOrderUrl}
            download="cutting_order.yaml"
            enabled={cuttingOrderUrl != '#'}>
            Download
          </LinkButton>
        </div>
      </div>
    </div>
  );
}

export default OrderAction;
