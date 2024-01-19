import { useState } from "react";

const TOKEN_KEY = "token";

export function useTokenStorage() {
  const [token, setToken] = useState(localStorage.getItem(TOKEN_KEY) || "");

  const saveToken = (newToken) => {
    localStorage.setItem(TOKEN_KEY, newToken);
    setToken(newToken);
  };

  const clearToken = () => {
    localStorage.removeItem(TOKEN_KEY);
    setToken("");
  };

  const getToken = () => {
    return token;
  };

  return { token, saveToken, clearToken, getToken };
}
