function getApi() {
  return globalThis.ENV.api.protocol
    + "://" + globalThis.ENV.api.host
    + ":" + globalThis.ENV.api.port
    + globalThis.ENV.api.path;
}

export { getApi };
