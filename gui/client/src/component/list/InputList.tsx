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
  const [count, setCount] = useState<string>("")
  const [cost, setCost] = useState<string>("")

  const [warning, setWarning] = useState<string>("")
  const [warningVisible, setWarningVisible] = useState<boolean>(false)

  function addInput() {
    if (!length) {
      setWarning("Length cannot be empty")
      setWarningVisible(true)
    } else if (isNaN(parseInt(length)) || parseInt(length) <= 0) {
      setWarning("Length must be a positive integer")
      setWarningVisible(true)
    } else if (count && (isNaN(parseInt(count)) || parseInt(count) < 0)) {
      setWarning("Count must be a non-negative integer")
      setWarningVisible(true)
    } else if (cost && isNaN(parseFloat(cost)) || parseFloat(cost) < 0) {
      setWarning("Cost must be a non-negative number")
      setWarningVisible(true)
    } else {
      setWarningVisible(false)
      const newInput: Input = {
        id: inputId,
        length: parseInt(length),
        count: parseInt(count) || null,
        cost: parseFloat(cost) || null
      };
      setInputs([...inputs, newInput])
      setInputId(inputId + 1)
      setLength("")
      setCount("")
      setCost("")
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
        <div className="inputs-in-container">
          <input type="number" min="1" step="1" value={length}
            className="styled-input"
            placeholder="Input element length..."
            onChange={e => setLength(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
          <input type="number" min="0" step="1" value={count}
            className="styled-input"
            placeholder="Input element count..."
            onChange={e => setCount(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
          <input type="number" min="0" step="1" value={cost}
            className="styled-input"
            placeholder="Input element cost..."
            onChange={e => setCost(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
        </div>
        <button type="button" className="btn btn-secondary fosscut-button input-button" onClick={() => addInput()}>
          Add
        </button>
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
