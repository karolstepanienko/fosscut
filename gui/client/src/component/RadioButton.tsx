// @ts-types="npm:@types/react-bootstrap/ButtonGroup"
import { ButtonGroup } from "npm:react-bootstrap/ButtonGroup";
// @ts-types="npm:@types/react-bootstrap/ToggleButton"
import { ToggleButton } from "npm:react-bootstrap/ToggleButton";


type RadioButtonFunction = (action: string) => void;

type RadioButtonProps = {
  action: string,
  setAction: RadioButtonFunction,
}

const RadioButton: React.FC<RadioButtonProps> = ({action, setAction}) => {
  const buttons = [
    { name: 'Order' },
    { name: 'Generate' },
    { name: 'Plan' },
  ];

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAction(e.currentTarget.value)
  }

  return (
    <ButtonGroup className="button-group">
      {buttons.map((button, idx) => (
        <ToggleButton
          key={idx}
          id={`radio-button-${idx}`}
          type="radio"
          variant="secondary"
          name="radio"
          value={button.name}
          checked={action === button.name}
          onChange={onChange}
        >
          {button.name}
        </ToggleButton>
      ))}
    </ButtonGroup>
  );
}

export default RadioButton;
