import { useState } from "react";
import RadioButton from "./RadioButton.tsx";
import OrderAction from "./action/OrderAction.tsx";
import GenerateAction from "./action/GenerateAction.tsx";
import PlanAction from "./action/PlanAction.tsx";

function ActionSwitch() {
  const [action, setAction] = useState<string>('Order');

  const renderAction = () => {
    if (action === 'Order')
      return <OrderAction />
    else if (action === 'Generate')
      return <GenerateAction />
    else if (action === 'Plan')
      return <PlanAction />
  }

  return (
    <div className="action-switch-container">
      <RadioButton action={action} setAction={setAction} />
      {renderAction()}
    </div>
  );
}

export default ActionSwitch;
