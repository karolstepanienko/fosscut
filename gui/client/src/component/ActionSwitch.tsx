import { useState } from "react";
import RadioButton from "./RadioButton.tsx";
import Order from "./action/Order.tsx";
import Generate from "./action/Generate.tsx";
import Plan from "./action/Plan.tsx";

function ActionSwitch() {
  const [action, setAction] = useState<string>('Order');

  const renderAction = () => {
    if (action === 'Order')
      return <Order />
    else if (action === 'Generate')
      return <Generate />
    else if (action === 'Plan')
      return <Plan />
  }

  return (
    <div className="action-switch-container">
      <RadioButton action={action} setAction={setAction} />
      {renderAction()}
    </div>
  );
}

export default ActionSwitch;
