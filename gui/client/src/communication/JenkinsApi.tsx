import { useEffect, useState } from "react";
import { useCookies } from 'react-cookie';

import { getApi } from "../Config.ts";
import JenkinsJobLogs, { SetJenkinsJobLogsFunction } from "../type/jenkins/JenkinsJobLogs.ts";
import { AxiosError } from "axios";
import JenkinsJobLogsDTO from "../type/jenkins/JenkinsJobLogsDTO.ts";

type JenkinsApiProps = {
  jenkinsJobLogs: JenkinsJobLogs,
  setJenkinsJobLogs: SetJenkinsJobLogsFunction,
}

const JenkinsApi = ({ jenkinsJobLogs, setJenkinsJobLogs }: JenkinsApiProps) => {
  const api = getApi();
  const [_, setQueueItemCookie] = useCookies(['fosscut_queueItemIdentifier']);
  const [__, setJobNumberCookie] = useCookies(['fosscut_jobNumberIdentifier']);
  const [ticking, setTicking] = useState<boolean>(false);
  const [count, setCount] = useState<number>(0);

  useEffect(() => { runTimer() }, [count, ticking]);

  const runTimer = () => {
    const timer = setTimeout(() => ticking && setCount(count + 1), 1e3);
    if (ticking) getLogs();
    return () => clearTimeout(timer);
  };

  const sendJobRunRequest = async () => {
    try {
      setJenkinsJobLogs(new JenkinsJobLogs());
      const response = await api.post('/jenkins/job/run');
      setTicking(true);
      setQueueItemCookie('fosscut_queueItemIdentifier', response.data)
      setJobNumberCookie('fosscut_jobNumberIdentifier', -1);
    } catch (error: unknown | AxiosError) {
      console.error("Error starting Jenkins job:", error);
      setTicking(false);
    }
  };

  const getLogs = () => {
    sendGetLogsRequest();
    if (jenkinsJobLogs && jenkinsJobLogs.isFinished()) {
      // if success or failure
      setTicking(false);
    }
  };

  const sendGetLogsRequest = async () => {
    try {
      const data = (await api.get("/jenkins/job/logs")).data;
      const logs = JenkinsJobLogsDTO.parse(data);
      setJenkinsJobLogs(new JenkinsJobLogs(logs));
      setJobNumberCookie('fosscut_jobNumberIdentifier', logs.jobNumberIdentifier);
    } catch (error: unknown | AxiosError) {
      console.error("Error fetching logs:", error);
    }
  };

  const renderStatus = () => {
    if (jenkinsJobLogs && jenkinsJobLogs.status !== null) {
      return <p>Status: {jenkinsJobLogs.status}</p>;
    }
    return null;
  };

  const renderResult = () => {
    if (jenkinsJobLogs && jenkinsJobLogs.result !== null) {
      return <p>Result: {jenkinsJobLogs.result}</p>;
    }
    return null;
  };

  const renderFinishing = () => {
    if (jenkinsJobLogs && jenkinsJobLogs.status === null
      && jenkinsJobLogs.building === "true" && jenkinsJobLogs.result === null
    ) {
      return <p>Jenkins job running...</p>;
    }
    return null;
  };

  const renderSummary = () => {
    return (
      <div className="summary-container">
        {renderFinishing()}
        {renderStatus()}
        {renderResult()}
      </div>
    );
  };

  const renderLogs = () => {
    if (jenkinsJobLogs && jenkinsJobLogs.logs) {
      return (
        <div className="logs-container">
          <p className="logs">{jenkinsJobLogs.logs.trim()}</p>
        </div>
      );
    }
    return null;
  };

  return {
    sendJobRunRequest,
    getLogs,
    sendGetLogsRequest,
    renderSummary: renderSummary,
    renderLogs: renderLogs
  };
}

export default JenkinsApi;
