// @ts-types="npm:@types/react-bootstrap/ButtonGroup"
import { ButtonGroup } from "npm:react-bootstrap/ButtonGroup";
// @ts-types="npm:@types/react-bootstrap/ToggleButton"
import { ToggleButton } from "npm:react-bootstrap/ToggleButton";

type SetCurrentValueFunction = (currentValue: string) => void;

type RadioButtonProps = {
  currentValue: string,
  setCurrentValue: SetCurrentValueFunction,
  values: string[],
  keyPrefix: string
}

const RadioButton: React.FC<RadioButtonProps>
  = ({currentValue, setCurrentValue, values, keyPrefix}) => {

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCurrentValue(e.currentTarget.value);
  }

  return (
    <ButtonGroup className="button-group">
      {values.map((value, idx) => (
        <ToggleButton
          key={keyPrefix + "-" + idx}
          id={`${keyPrefix}-radio-button-${idx}`}
          type="radio"
          variant="secondary"
          name={`${keyPrefix}-radio`}
          value={value}
          checked={currentValue === value}
          onChange={onChange}
        >
          {value}
        </ToggleButton>
      ))}
    </ButtonGroup>
  );
}

export default RadioButton;
