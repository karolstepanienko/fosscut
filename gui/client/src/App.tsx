import "./App.css";
import "./component/list/List.tsx"
import List from "./component/list/List.tsx";
import ItemTypes from "./enum/ItemTypes.ts";

function App() {
  return (
    <>
      <div className="card">
        <List type={ItemTypes.In} />
        <List type={ItemTypes.Out} />
      </div>
    </>
  );
}

export default App;
