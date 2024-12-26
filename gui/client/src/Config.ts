import axios, { AxiosInstance } from "axios";

const getBaseUrl = () : string => {
  return globalThis.ENV.api.protocol
    + "://" + globalThis.ENV.api.host
    + ":" + globalThis.ENV.api.port
    + globalThis.ENV.api.path;
}

const getApi = () : AxiosInstance => {
  return axios.create({
    baseURL: getBaseUrl()
  });
}

export { getApi };
