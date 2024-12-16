import { useState } from "react";
import Output from "../../type/Output.ts";
import OutputItem from "../OutputItem.tsx";

const OutputList = () => {
  const [outputs, setOutputs] = useState<Output[]>([
    { id: 0, length: 30, number: 2, maxRelax: 0 }
  ])

  const [id, setId] = useState<number>(1)
  const [length, setLength] = useState<number>("")
  const [number, setNumber] = useState<number>("")
  const [maxRelax, setMaxRelax] = useState<number>("")

  const [warning, setWarning] = useState<string>("")
  const [warningVisible, setWarningVisible] = useState<boolean>(false)

  const addOutput = () => {
    if (length === "" || number === "" || maxRelax === "") {
      setWarning("All fields need to be filled")
      setWarningVisible(true)
    } else if (length < 1 || number < 1 || maxRelax < 0) {
      setWarning("All values need to be positive integers")
      setWarningVisible(true)
    } else {
      setWarningVisible(false)
      const newOutput: Output = {
        id: id,
        length: length,
        number: number,
        maxRelax: maxRelax
      }

      setOutputs([...outputs, newOutput])
      setId(id + 1)
      setLength("")
      setNumber("")
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
      <p>Outputs</p>
      {renderWarning()}
      <div className="input-container">
        <div className="inputs-in-container">
          <input type="number" min="1" step="1" value={length}
            className="styled-input"
            placeholder="Output length..."
            onChange={e => setLength(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
          <input type="number" min="1" step="1" value={number}
            className="styled-input"
            placeholder="Output number..."
            onChange={e => setNumber(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
          <input type="number" min="0" value={maxRelax}
            className="styled-input"
            placeholder="Maximum relaxation ..."
            onChange={e => setMaxRelax(e.target.value)}
            onKeyDown={e => handleKeyDown(e)}
          />
        </div>
        <button className="input-button" onClick={() => addOutput()}>
          Add</button>
      </div>
      {outputs.map((output: Output) => (
        <OutputItem
            key={output.id}
            output={output}
            deleteOutput={deleteOutput}
        />
      ))}
    </div>
  )
}

export default OutputList
