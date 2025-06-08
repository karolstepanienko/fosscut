import { useEffect, useState } from "react";
import { useCookies } from 'react-cookie';
import { Accordion } from 'react-bootstrap';
import RadioButton from "./RadioButton.tsx";
import Algorithm from "../enum/Algorithm.ts";
import { SetSettingsExtendedFunction } from "../type/SettingsExtended.ts";
import Backend from "../enum/Backend.ts";

type SettingsProps = {
  settingsExtended: boolean,
  setSettingsExtended: SetSettingsExtendedFunction,
  backend : string,
  setBackend: (backend: string) => void
}

const Settings: React.FC<SettingsProps> = ({settingsExtended, setSettingsExtended, backend, setBackend}) => {
  const fosscutSettingsCookieName = 'fosscut_settings';
  const [algorithm, setAlgorithm] = useState<string>(Algorithm.FFD);
  const [cookies, setCookie] = useCookies([fosscutSettingsCookieName]);

  useEffect(() => { updateSettingsCookie() }, [algorithm, backend]);
  useEffect(() => { loadSettingsFromCookie() }, []);

  const updateSettingsCookie = () => {
    setCookie(fosscutSettingsCookieName, generateCookieString());
  }

  const generateCookieString = () => {
    return JSON.stringify({
      algorithm,
      backend
    });
  }

  const loadSettingsFromCookie = () => {
    if (cookies['fosscut_settings']) {
      setAlgorithm(cookies['fosscut_settings'].algorithm);
      setBackend(cookies['fosscut_settings'].backend);
    }
  };

  const getActiveKey = () => {
    if (settingsExtended) return "0";
    else return "";
  }

  return (
      <div className="settings-container">
        <Accordion defaultActiveKey={getActiveKey()}>
          <Accordion.Item eventKey="0">
            <Accordion.Header>Settings</Accordion.Header>
            <Accordion.Body onEntering={() => setSettingsExtended(true)} onExited={() => setSettingsExtended(false)}>
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
            </Accordion.Body>
          </Accordion.Item>
        </Accordion>
      </div>
  );
}

export default Settings;
