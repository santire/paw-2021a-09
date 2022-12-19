import axios from "axios";
export const apiClient = axios.create({
  baseURL: process.env.REACT_APP_HOST_API || "http://localhost:8080",
});

apiClient.defaults.headers.common["Content-Type"] = "application/json";
apiClient.defaults.headers.common["Authorization"] = authHeader().Authorization;

function authHeader() {
  const authStr = localStorage.getItem("auth");
  let auth = null;
  if (authStr) auth = JSON.parse(authStr);

  if (auth && auth.token) {
    return { Authorization: "Bearer " + auth.token };
  } else {
    return { Authorization: "" };
  }
}
