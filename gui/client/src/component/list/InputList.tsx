import { useState } from "react";
import Item from "../InputItem.tsx"
import Input, { SetInputsFunction } from "../../type/Input.ts";
import SetIdFunction from "../../type/SetIdFunction.ts";


type InputListProps = {
  inputId: number,
  setInputId: SetIdFunction,
  inputs: Input[],
  setInputs: SetInputsFunction,
}

const InputList: React.FC<InputListProps> = ({inputId, setInputId, inputs, setInputs}) => {
  const [length, setLength] = useState<string>("")

  const [warning, setWarning] = useState<string>("")
  const [warningVisible, setWarningVisible] = useState<boolean>(false)

  function addInput() {
    if (!length) {
      setWarning("Length cannot be empty")
      setWarningVisible(true)
    } else {
      setWarningVisible(false)
      const newInput: Input = {
        id: inputId,
        length: parseInt(length)
      };
      setInputs([...inputs, newInput])
      setInputId(inputId + 1)
      setLength("")
    }
  }

  function deleteInput(id: number) {
    setInputs(inputs.filter((input: Input) => input.id !== id));
  }

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') { addInput(); }
  };

  const renderWarning = () => {
    if (warningVisible) return <p className="warning text">{warning}</p>
  }

  return (
    <div className="list">
      {renderWarning()}
      <div className="input-container">
        <input type="number" min="1" step="1" value={length}
          className="styled-input"
          placeholder="Input element length..."
          onChange={e => setLength(e.target.value)}
          onKeyDown={e => handleKeyDown(e)}
        />
        <button className="btn btn-secondary fosscut-button input-button" onClick={() => addInput()}>
          Add</button>
      </div>
      {inputs.map((input: Input) => (
        <Item
            key={input.id}
            input={input}
            deleteInput={deleteInput}
        />
      ))}
    </div>
  );
}

export default InputList;
