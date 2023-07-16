import React, { createContext, useState, useEffect } from "react";
import { apiClient } from "../api/client";

interface AuthContextType {
  isAuthenticated: boolean;
  setCredentials: (token: string, userId: number) => void;
  logout: () => void;
  userId: number;
}

const initialAuthContext: AuthContextType = {
  isAuthenticated: !!localStorage.getItem("token"),
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
  };

  useEffect(() => {
    // Check authentication status on component mount
    const checkAuthStatus = async () => {
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
    const expirationDate = new Date(jwt && jwt.exp && jwt.exp * 1000);
    // multiply by 1000 to convert seconds into milliseconds
    return new Date() < expirationDate;
    // DEBUG: force token validity to x mins
    // let x = 0.15;
    // return (jwt && jwt.exp && jwt.exp * 1000) - (60 - x) * 60 * 1000 || null;
  } catch (e) {
    console.log("Invalid token");
    return false;
  }
}
