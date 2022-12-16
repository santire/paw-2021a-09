import { User } from "../../types";
import { apiClient } from "../client";

export async function login(email: string, password: string) {
  const response = await apiClient.post("login", { email, password });
  if (response.data.token) {
    localStorage.setItem("auth", JSON.stringify(response.data));
  }
  return response.data;
}

export function logout() {
  localStorage.removeItem("auth");
}

export function getLoggedUser() {
  const authStr = localStorage.getItem("auth");
  if (authStr) return JSON.parse(authStr).user as User;
  return undefined;
}
