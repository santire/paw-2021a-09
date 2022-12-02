import axios from "axios";
export const apiClient = axios.create({
  baseURL: process.env.REACT_APP_HOST_API || "http://localhost:8080",
});
