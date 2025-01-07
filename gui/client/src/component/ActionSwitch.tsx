import { useState } from "react";
import { CookiesProvider, useCookies } from 'react-cookie';
import RadioButton from "./RadioButton.tsx";
import OrderAction from "./action/OrderAction.tsx";
import GenerateAction from "./action/GenerateAction.tsx";
import PlanAction from "./action/PlanAction.tsx";

function ActionSwitch() {
  const [action, setAction] = useState<string>('Order');
  const [cookies, setCookie] = useCookies(['fosscut_orderIdentifier']);

  const renderAction = () => {
    if (action === 'Order')
      return <OrderAction cookies={cookies} setCookie={setCookie}/>
    else if (action === 'Generate')
      return <GenerateAction />
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
