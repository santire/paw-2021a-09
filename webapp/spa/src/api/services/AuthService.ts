import { User } from "../../types";
import { TokenProvider } from "../../utils/TokenProvider";
import { apiClient } from "../client";

const tokenProvider = TokenProvider.getInstance();
const PATH = "/users";

export async function activateUser(token: string) {
  const response = await apiClient().put(PATH, null, {
    params: { type: "activation", token },
    headers: {
      "Content-Type": "application/json",
    },
  });
  return response.data;
}
export async function resetUser(email: string) {
  const response = await apiClient().post(PATH, null, {
    params: { email },
    headers: {
      "Content-Type": "application/json",
    },
  });
  return response.data;
}

export async function resetPassword(token: string, password: string) {
  const response = await apiClient().put(
    PATH,
    { password, repeatPassword: password },
    {
      params: {
        type: "reset",
        token,
      },
    }
  );
  return response.data;
}

export async function login(email: string, password: string) {
  const response = await apiClient().get<User>(PATH, {
    auth: { username: email, password: password },
    params: { email },
  });
  if (
    "authorization" in response.headers &&
    response.headers["authorization"]?.startsWith("Bearer")
  ) {
    const token = response.headers["authorization"].slice(7);
    tokenProvider.setToken(token);
    tokenProvider.setUser(JSON.stringify(response.data));
  }
}

export async function register(user: User) {
  const response = await apiClient().post(PATH, {
    ...user,
    repeatPassword: user.password,
  });
  return response.data;
}

export function logout() {
  tokenProvider.setToken("");
  tokenProvider.setUser("null");
}
