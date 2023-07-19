import React, { createContext, useState, useEffect } from "react";
import { apiClient } from "../api/client";
import { useQueryClient } from "react-query";

interface AuthContextType {
  isAuthenticated: boolean;
  setCredentials: (token: string, userId: number) => void;
  logout: () => void;
  userId: number;
}

const initialAuthContext: AuthContextType = {
  isAuthenticated: false,
  setCredentials: () => {},
  logout: () => {},
  userId: NaN,
};

export const AuthContext = createContext<AuthContextType>(initialAuthContext);

type Props = { children: React.ReactNode };
export function AuthContextProvider({ children }: Props) {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(
    initialAuthContext.isAuthenticated
  );
  const [userId, setUserId] = useState(initialAuthContext.userId);
  const queryClient = useQueryClient();

  const setCredentials = (token: string, userId: number) => {
    // Set the token and userId in localStorage
    localStorage.setItem("token", token);
    localStorage.setItem("userId", "" + userId);

    // Update client headers
    apiClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;

    setIsAuthenticated(true);
    setUserId(userId);
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    setIsAuthenticated(false);
    setUserId(NaN);
    queryClient.clear();
  };

  useEffect(() => {
    // Check authentication status on component mount
    const checkAuthStatus = () => {
      // console.log("Checking auth status");
      try {
        const token = localStorage.getItem("token");
        const userId = parseInt(localStorage.getItem("userId") || "");

        // Throw error if invalid token
        if (!userId || !token || !isTokenValid(token) || isNaN(userId)) {
          throw new Error("Token is not valid");
        }

        apiClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;

        setIsAuthenticated(true);
        setUserId(userId);
      } catch (error) {
        // Handle token error
        logout();
      }
    };

    checkAuthStatus();
    // Check auth every 10 minutes
    const interval = setInterval(() => checkAuthStatus(), 0.2 * 60 * 1000);
    return () => {
      clearInterval(interval);
    };
  }, []);

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, setCredentials, logout, userId }}
    >
      {children}
    </AuthContext.Provider>
  );
}

function isTokenValid(jwtToken: string): boolean {
  try {
    const jwt = JSON.parse(atob(jwtToken.split(".")[1]));
    // If token couldn't be parsed, return invalid
    if (!jwt) {
      return false;
    }
    // multiply by 1000 to convert seconds into milliseconds
    const expirationDate = new Date(jwt && jwt.exp && jwt.exp * 1000);

    // let x = 0.15;
    // const expirationDate = new Date(
    //   (jwt && jwt.exp && jwt.exp * 1000) - (60 - x) * 5 * 60 * 1000
    // );
    // console.log("expiration date: ", expirationDate);
    return new Date() < expirationDate;
  } catch (e) {
    // DEBUG: force token validity to x mins
    console.log("Invalid token");
    return false;
  }
}