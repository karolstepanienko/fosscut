import { useState } from "react";
import Output, { SetOutputsFunction } from "../../type/Output.ts";
import OutputItem from "../OutputItem.tsx";
import SetIdFunction from "../../type/SetIdFunction.ts";

type OutputListProps = {
  outputId: number,
  setOutputId: SetIdFunction,
  outputs: Output[],
  setOutputs: SetOutputsFunction,
}

const OutputList: React.FC<OutputListProps> = ({outputId, setOutputId, outputs, setOutputs}) => {
  const [length, setLength] = useState<string>("")
  const [count, setCount] = useState<string>("")
  const [maxRelax, setMaxRelax] = useState<string>("")

  const [warning, setWarning] = useState<string>("")
  const [warningVisible, setWarningVisible] = useState<boolean>(false)

  const addOutput = () => {
    if (length === "" || count === "" || maxRelax === "") {
      setWarning("All fields need to be filled")
      setWarningVisible(true)
    } else if (parseInt(length) < 1 || parseInt(count) < 1 || parseInt(maxRelax) < 0) { 
      setWarning("All values need to be positive integers")
      setWarningVisible(true)
    } else {
      setWarningVisible(false)
      const newOutput: Output = {
        id: outputId,
        length: parseInt(length),
        count: parseInt(count),
        maxRelax: parseInt(maxRelax)
      }

      setOutputs([...outputs, newOutput])
      setOutputId(outputId + 1)
      setLength("")
      setCount("")
      setMaxRelax("")
    }
  }

  function deleteOutput(id: number) {
    setOutputs(outputs.filter((output: Output) => output.id !== id))
  }

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') { addOutput() }
  }

  const renderWarning = () => {
    if (warningVisible)
    return <p className="warning text">{warning}</p>
  }

  return (
    <div className="list">
      {renderWarning()}
      <div className="input-container">
        <div className="inputs-in-container">
          <input type="number" min="1" step="1" value={length?.toString()}
            className="styled-input"
            placeholder="Output length..."
            onChange={e => setLength(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
          <input type="number" min="1" step="1" value={count?.toString()}
            className="styled-input"
            placeholder="Output count..."
            onChange={e => setCount(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
          <input type="number" min="0" value={maxRelax?.toString()}
            className="styled-input"
            placeholder="Maximum relaxation ..."
            onChange={e => setMaxRelax(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
        </div>
        <button type="button" className="btn btn-secondary fosscut-button input-button" onClick={() => addOutput()}>
          Add
        </button>
      </div>
      {outputs.map((output: Output) => (
        <OutputItem
            key={output.id}
            output={output}
            deleteOutput={deleteOutput}
        />
      ))}
    </div>
  );
}

export default OutputList;
