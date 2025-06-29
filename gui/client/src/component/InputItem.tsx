import Input from "../type/Input.ts";

type InputFunction = (id: number) => void;

type InputItemProps = {
  input: Input,
  deleteInput: InputFunction,
}

const InputItem: React.FC<InputItemProps> = ({input, deleteInput}) => {

  const renderCount = () => {
    if (input.count !== null && input.count !== undefined) {
      return <p className="item-name">Count: {input.count}</p>;
    }
    return null;
  }

  const renderCost = () => {
    if (input.cost !== null && input.cost !== undefined) {
      return <p className="item-name">Cost: {input.cost}</p>;
    }
    return null;
  }

  return (
  <div className="item">
    <div className="item-name-container">
      <p className="item-name">Length: {input.length}</p>
      {renderCount()}
      {renderCost()}
    </div>
    <button className="btn btn-secondary fosscut-button item-button"
      type="button" onClick={() => deleteInput(input.id)}>
      X
    </button>
  </div>
  );
}

export default InputItem;
