import Input from "../type/Input.ts";

type InputFunction = (id: number) => void;

type InputItemProps = {
  input: Input,
  deleteInput: InputFunction,
}

const InputItem: React.FC<InputItemProps> = ({input, deleteInput}) => {
  return (
  <div className="item">
    <div className="item-name-container">
      <p className="item-name">Length: {input.length}</p>
    </div>
    <button className="btn btn-secondary fosscut-button item-button"
      type="button" onClick={() => deleteInput(input.id)}>
      X
    </button>
  </div>
  );
}

export default InputItem;
