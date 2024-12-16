import "./App.css";
import "./component/list/InputList.tsx"
import InputList from "./component/list/InputList.tsx";
import OutputList from "./component/list/OutputList.tsx";

function App() {
  return (
    <>
      <div className="card">
        <InputList/>
        <OutputList/>
      </div>
    </>
  )
}

export default App
