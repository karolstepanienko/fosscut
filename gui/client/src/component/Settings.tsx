import { useEffect, useState } from "react";
import { useCookies } from 'react-cookie';
import { Accordion } from 'react-bootstrap';
import RadioButton from "./RadioButton.tsx";
import Algorithm from "../enum/Algorithm.ts";
import { SetSettingsExtendedFunction } from "../type/SettingsExtended.ts";
import Backend, { SetBackendFunction } from "../enum/Backend.ts";
import LinearSolver from "../enum/LinearSolver.ts";
import IntegerSolver from "../enum/IntegerSolver.ts";
import OptimizationCriterion from "../enum/OptimizationCriterion.ts";

type SettingsProps = {
  settingsExtended: boolean,
  setSettingsExtended: SetSettingsExtendedFunction,
  backend : string,
  setBackend: SetBackendFunction
}

const Settings: React.FC<SettingsProps> = ({settingsExtended, setSettingsExtended, backend, setBackend}) => {
  const fosscutSettingsCookieName = 'fosscut_settings';
  const [algorithm, setAlgorithm] = useState<string>(Algorithm.FFD);
  const [linearSolver, setLinearSolver] = useState<string>(LinearSolver.GLOP);
  const [integerSolver, setIntegerSolver] = useState<string>(IntegerSolver.SCIP);
  const [optimizationCriterion, setOptimizationCriterion] = useState<string>(OptimizationCriterion.MIN_WASTE);
  const [relaxCost, setRelaxCost] = useState<number>(0.0);
  const [relaxEnabled, setRelaxEnabled] = useState<boolean>(false);
  const [cookies, setCookie] = useCookies([fosscutSettingsCookieName]);

  useEffect(() => { updateSettingsCookie() }, [algorithm, linearSolver, integerSolver, optimizationCriterion, relaxEnabled, relaxCost]);
  useEffect(() => { loadSettingsFromCookie() }, []);

  const updateSettingsCookie = () => {
    setCookie(fosscutSettingsCookieName, generateCookieString());
  }

  const generateCookieString = () => {
    return JSON.stringify({
      algorithm,
      linearSolver,
      integerSolver,
      optimizationCriterion,
      relaxCost,
      relaxEnabled
    });
  }

  const loadSettingsFromCookie = () => {
    if (cookies['fosscut_settings']) {
      setAlgorithm(cookies['fosscut_settings'].algorithm);
      setLinearSolver(cookies['fosscut_settings'].linearSolver);
      setIntegerSolver(cookies['fosscut_settings'].integerSolver);
      setOptimizationCriterion(cookies['fosscut_settings'].optimizationCriterion);
      setRelaxCost(cookies['fosscut_settings'].relaxCost);
      setRelaxEnabled(cookies['fosscut_settings'].relaxEnabled);
    }
  };

  const getActiveKey = () => {
    if (settingsExtended) return "0";
    else return "";
  }

  const renderLinearSolver = () => { 
    if (algorithm === Algorithm.CG) {
      return (
        <div className="settings-item" >
          <label className="btn settings-item-text">Linear solver:</label>
          <RadioButton
            currentValue={linearSolver}
            setCurrentValue={setLinearSolver}
            values={Object.values(LinearSolver)}
            keyPrefix="linearSolver"
          />
          <label className="btn settings-item-text-invisible">Linear solver:</label>
        </div>
      );
    } else return null;
  }

  const renderIntegerSolver = () => {
    if (algorithm === Algorithm.CG || algorithm === Algorithm.GREEDY) {
      return (
        <div className="settings-item" >
          <label className="btn settings-item-text">Integer solver:</label>
          <RadioButton
            currentValue={integerSolver}
            setCurrentValue={setIntegerSolver}
            values={Object.values(IntegerSolver)}
            keyPrefix="integerSolver"
          />
          <label className="btn settings-item-text-invisible">Integer solver:</label>
        </div>
      );
    } else return null;
  }

  return (
      <div className="settings-container">
        <Accordion defaultActiveKey={getActiveKey()}>
          <Accordion.Item eventKey="0">
            <Accordion.Header>Settings</Accordion.Header>
            <Accordion.Body onEntering={() => setSettingsExtended(true)} onExited={() => setSettingsExtended(false)}>

              <div className="settings-item" >
                <label className="btn settings-item-text">Backend:</label>
                <RadioButton
                  currentValue={backend}
                  setCurrentValue={setBackend}
                  values={Object.values(Backend)}
                  keyPrefix="backend"
                />
                <label className="btn settings-item-text-invisible">Backend:</label>
              </div>

              <div className="settings-item" >
                <label className="btn settings-item-text">Algorithm:</label>
                <RadioButton
                  currentValue={algorithm}
                  setCurrentValue={setAlgorithm}
                  values={Object.values(Algorithm)}
                  keyPrefix="algorithm"
                />
                <label className="btn settings-item-text-invisible">Algorithm:</label>
              </div>

              {renderLinearSolver()}
              {renderIntegerSolver()}

              <div className="settings-item" >
                <label className="btn settings-item-text">Optimization criterion:</label>
                <RadioButton
                  currentValue={optimizationCriterion}
                  setCurrentValue={setOptimizationCriterion}
                  values={Object.values(OptimizationCriterion)}
                  keyPrefix="optimizationCriterion"
                />
                <label className="btn settings-item-text-invisible">Optimization criterion:</label>
              </div>

              <div className="settings-item">
                <label className="btn settings-item-text">Relaxation cost:</label>
                <input
                  type="number"
                  className="button-group form-control"
                  min={0}
                  value={relaxCost}
                  onChange={(e) => {
                  const value = parseFloat(e.target.value);
                  setRelaxCost(isNaN(value) ? 0.0 : Math.max(0.0, value));
                  }}
                />
                <label className="btn settings-item-text-invisible">Relaxation cost:</label>
              </div>

              <div className="settings-item">
                <label className="btn settings-item-text">Relaxation enabled:</label>
                <div className="form-check form-switch button-group">
                  <input className="switch-button form-check-input"
                    type="checkbox" role="switch"
                    checked={relaxEnabled}
                    onChange={(e) => setRelaxEnabled(e.target.checked)}
                  />
                </div>
                <label className="btn settings-item-text-invisible">Relaxation enabled:</label>
              </div>

            </Accordion.Body>
          </Accordion.Item>
        </Accordion>
      </div>
  );
}

export default Settings;
