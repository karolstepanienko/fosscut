import { useState } from "react";
import Item from "../InputItem.tsx"
import Input from "../../type/Input.ts";

const InputList = () => {
  const [inputs, setInputs] = useState<Input[]>([
    { id: 0, length: 100 }
  ])

  const [id, setId] = useState<number>(1)
  const [length, setLength] = useState<number>("")

  const [warning, setWarning] = useState<string>("")
  const [warningVisible, setWarningVisible] = useState<boolean>(false)

  function addInput() {
    if (length === "") {
      setWarning("Length cannot be empty")
      setWarningVisible(true)
    } else if (length < 1) {
      setWarning("Length has to be a positive integer")
      setWarningVisible(true)
    } else {
      setWarningVisible(false)
      const newInput: Input = {
        id: id,
        length: length
      };
      setInputs([...inputs, newInput])
      setId(id + 1)
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
    if (warningVisible)
    return <p className="warning text">{warning}</p>
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
