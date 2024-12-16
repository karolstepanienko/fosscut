import Input from "../type/Input.ts";

type InputFunction = (id: number) => void;

type InputItemProps = {
  input: Input,
  deleteInput: InputFunction,
}

const InputItem: React.FC<InputItemProps> = ({input, deleteInput}) => {
  return (
  <div className="item">
    <p className="item-name">Length: {input.length}</p>
    <button className="item-button" onClick={() => deleteInput(input.id)}>
    x
    </button>
  </div>
  )
}

export default InputItem
