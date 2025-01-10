import { useState } from "react";
import { CookiesProvider, useCookies } from 'react-cookie';
import RadioButton from "./RadioButton.tsx";
import OrderAction from "./action/OrderAction.tsx";
import GenerateAction from "./action/GenerateAction.tsx";
import PlanAction from "./action/PlanAction.tsx";
import Input from "../type/Input.ts";
import Output from "../type/Output.ts";
import TektonTaskRunLogsDTO from "../type/TektonTaskRunLogsDTO.ts";

function ActionSwitch() {
  const [action, setAction] = useState<string>('Order');
  const [cookies, setCookie] = useCookies(['fosscut_orderIdentifier']);

  const [inputId, setInputId] = useState<number>(1);
  const [inputs, setInputs] = useState<Input[]>([
    { id: 0, length: 100 }
  ]);
  const [outputId, setOutputId] = useState<number>(1);
  const [outputs, setOutputs] = useState<Output[]>([
    { id: 0, length: 30, count: 2, maxRelax: 0 }
  ]);

  const [tektonTaskRunLogsDTO, setTektonTaskRunLogsDTO] = useState<TektonTaskRunLogsDTO>(undefined);

  const renderAction = () => {
    if (action === 'Order')
      return <OrderAction
        inputId={inputId} setInputId={setInputId}
        inputs={inputs} setInputs={setInputs}
        outputId={outputId} setOutputId={setOutputId}
        outputs={outputs} setOutputs={setOutputs}
        cookies={cookies} setCookie={setCookie} />
    else if (action === 'Generate')
      return <GenerateAction
      tektonTaskRunLogsDTO={tektonTaskRunLogsDTO}
      setTektonTaskRunLogsDTO={setTektonTaskRunLogsDTO} />
    else if (action === 'Plan')
      return <PlanAction />
  }

  return (
    <CookiesProvider>
      <div className="action-switch-container">
        <RadioButton action={action} setAction={setAction} />
        {renderAction()}
      </div>
    </CookiesProvider>
  );
}

export default ActionSwitch;
