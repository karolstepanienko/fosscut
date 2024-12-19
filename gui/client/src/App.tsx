import "./App.css";

import NavigationBar from "./component/NavigationBar.tsx"
import InputList from "./component/list/InputList.tsx";
import OutputList from "./component/list/OutputList.tsx";

function App() {
  return (
    <>
      <NavigationBar/>
      <div className="card">
        <InputList/>
        <OutputList/>
      </div>
    </>
  );
}

export default App;
