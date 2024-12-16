import Output from "../type/Output.ts";

type OutputFunction = (id: number) => void;

type OutputItemProps = {
  output: Output,
  deleteOutput: OutputFunction,
}

const OutputItem: React.FC<OutputItemProps> = ({output, deleteOutput}) => {
  return (
  <div className="item">
    <div className="todo-item-name-container">
      <p className="item-name">Length: {output.length}</p>
      <p className="item-name">Number: {output.number}</p>
      <p className="item-name">MaxRelax: {output.maxRelax}</p>
    </div>
    <button className="item-button output" onClick={() => deleteOutput(output.id)}>
    x
    </button>
  </div>
  )
}

export default OutputItem
