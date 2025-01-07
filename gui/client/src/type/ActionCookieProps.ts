type setCookieFunction = (name: string, value: string) => void;

type FosscutCookies = {
  fosscut_orderIdentifier: string
}

type ActionCookieProps = {
  cookies: FosscutCookies,
  setCookie: setCookieFunction
}

export default ActionCookieProps;
