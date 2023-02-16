import axios from "axios";
import { TokenProvider } from "../utils/TokenProvider";
export const apiClient = () => {
  const instance = axios.create({
    baseURL: process.env.REACT_APP_HOST_API || "http://localhost:8080",
    paramsSerializer: {
      indexes: null,
    },
    headers: {
      common: {
        ...authHeader(),
        "Content-Type": "application/json",
      },
    },
  });
  return instance;
};

// apiClient.defaults.headers.common["Content-Type"] =
// apiClient.defaults.headers.common["Authorization"] = authHeader().Authorization;

export function authHeader() {
  const token = TokenProvider.getInstance().getToken();

  if (token) {
    return { Authorization: "Bearer " + token };
  } else {
    return { Authorization: "" };
  }
}
