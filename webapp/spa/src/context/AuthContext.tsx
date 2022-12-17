import { AxiosPromise, AxiosRequestConfig } from "axios";
import { createContext, useContext, useEffect, useState } from "react";
import { apiClient } from "../api/client";
import { User } from "../types";
import { TokenProvider } from "../utils/TokenProvider";

interface AuthValue {
  user: User | null;
  authed: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  authedAxios: (options: AxiosRequestConfig) => AxiosPromise<any>;
}

// Initializing context with default values
const AuthContext = createContext<AuthValue>({
  user: null,
  authed: false,
  login: async (_1, _2) => { },
  logout: async () => { },
  authedAxios: (_) => new Promise(() => { }),
});

const tokenProvider = TokenProvider.getInstance();

export const useAuth = () => {
  const [authed, setAuthed] = useState(tokenProvider.isLoggedIn());
  const user = JSON.parse(tokenProvider.getUser()) as User | null;

  useEffect(() => {
    const listener = (newIsAuthed: boolean) => {
      setAuthed(newIsAuthed);
    };

    tokenProvider.subscribe(listener);
    return () => {
      tokenProvider.unsubscribe(listener);
    };
  }, []);

  return {
    user,
    authed,
    async login(email: string, password: string) {
      const response = await apiClient.post("/login", { email, password });
      if ("token" in response.data) {
        tokenProvider.setToken(response.data.token);
      }
      if ("user" in response.data) {
        tokenProvider.setUser(JSON.stringify(response.data.user));
      }
    },

    async logout() {
      tokenProvider.setToken("");
      tokenProvider.setUser("null");
    },

    authedAxios(options: AxiosRequestConfig) {
      const config: AxiosRequestConfig = {
        ...options,
        headers: { Authorization: tokenProvider.getToken() },
      };
      return apiClient(config);
    },
  };
};

type Props = { children: React.ReactNode };
export function AuthProvider({ children }: Props) {
  const auth = useAuth();

  return <AuthContext.Provider value={auth}>{children}</AuthContext.Provider>;
}

export default function AuthConsumer() {
  return useContext(AuthContext);
}
