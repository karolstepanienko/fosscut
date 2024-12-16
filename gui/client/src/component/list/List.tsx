import { useState } from "react";
import Item from "../Item.tsx"
import Task from "../../type/Task.ts";
import ItemTypes from "../../enum/ItemTypes.ts";

type ListProps = {
  type: ItemTypes
}

const List: React.FC<ListProps> = ({type}) => {
  const [tasks, setTasks] = useState<Task[]>([
    { id: 1, text: 'Task 1', completed: false },
    { id: 2, text: 'Longer Task 2', completed: true },
  ]);

  const [text, setText] = useState<string>('');

  function addTask(text: string) {
    const newTask: Task = {
      id: Date.now(),
      text,
      completed: false
    };
    setTasks([...tasks, newTask]);
    setText('');
  }

  function deleteTask(id: number) {
    setTasks(tasks.filter((task: Task) => task.id !== id));
  }

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') { addTask(text); }
  };

  const renderTitle = () => {
    if (type === ItemTypes.In) return <p>Inputs</p>
    if (type === ItemTypes.Out) return <p>Outputs</p>
  }

  const getPlaceholder = () => {
    const el = " element length..."
    if (type === ItemTypes.In) return "Input" + el
    if (type === ItemTypes.Out) return "Output" + el
  }

   return (
    <div className="todo-list">
      {renderTitle()}
      <div className="input-container">
        <input type="text" value={text}
          className="styled-input"
          placeholder={getPlaceholder()}
          onChange={e => setText(e.target.value)}
          onKeyDown={e => handleKeyDown(e)}
        />
        <button className="input-button" onClick={() => addTask(text)}>
          Add</button>
      </div>
      {tasks.map((task: Task) => (
        <Item
            key={task.id} 
            task={task}
            deleteTask={deleteTask}
        />
      ))}
    </div>
  );
}

export default List;
