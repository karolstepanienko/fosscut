import Task from "../type/Task.ts";

type TaskFunction = (id: number) => void;

type ItemProps = {
  task: Task,
  deleteTask: TaskFunction,
}

const Item: React.FC<ItemProps> = ({task, deleteTask}) => {
  return (
  <div className="todo-item">
    <p className="todo-item-name">{task.text}</p>
    <button className="todo-item-button" onClick={() => deleteTask(task.id)}>
    x
    </button>
  </div>
  );
}

export default Item;
