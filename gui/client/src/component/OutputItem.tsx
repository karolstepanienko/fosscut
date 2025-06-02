import Output from "../type/Output.ts";

type OutputFunction = (id: number) => void;

type OutputItemProps = {
  output: Output,
  deleteOutput: OutputFunction,
}

const OutputItem: React.FC<OutputItemProps> = ({output, deleteOutput}) => {
  return (
  <div className="item">
    <div className="item-name-container">
      <p className="item-name">Length: {output.length}</p>
      <p className="item-name">Count: {output.count}</p>
      <p className="item-name">MaxRelax: {output.maxRelax}</p>
    </div>
    <button className="btn btn-secondary fosscut-button item-button"
      type="button" onClick={() => deleteOutput(output.id)}>
      X
    </button>
  </div>
  );
}

export default OutputItem;
