import { User } from "../../types";
import { TokenProvider } from "../../utils/TokenProvider";
import { apiClient } from "../client";

const tokenProvider = TokenProvider.getInstance();

export async function activateUser(token: string) {
  const response = await apiClient.post("/activate", { token: token });
  return response.data;
}

export async function login(email: string, password: string) {
  const response = await apiClient.post("/login", { email, password });
  if ("token" in response.data) {
    tokenProvider.setToken(response.data.token);
  }
  if ("user" in response.data) {
    tokenProvider.setUser(JSON.stringify(response.data.user));
  }
}

export async function register(user: User) {
  const response = await apiClient.post("register", { ...user });
  return response.data;
}

export function logout() {
  tokenProvider.setToken("");
  tokenProvider.setUser("null");
}
