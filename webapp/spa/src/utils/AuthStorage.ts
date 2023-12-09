import { decodeJwt } from "jose";

const AUTH_TOKEN_KEY = "GOURMETABLE-AUTH-TOKEN";
const REFRESH_TOKEN_KEY = "GOURMETABLE-REFRESH-TOKEN";

export function getAuthToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY);
}

export function getRefreshToken() {
  return localStorage.getItem(REFRESH_TOKEN_KEY);
}

export function getUserEmail() {
  const authToken = getAuthToken();
  if (authToken) {
    return getSubFromToken(authToken);
  }
  const refreshToken = getRefreshToken();
  if (refreshToken) {
    return getSubFromToken(refreshToken);
  }
}

function getSubFromToken(token: string) {
  try {
    const jwtToken = decodeJwt(token);
    return jwtToken?.sub;
  } catch (error) {
    console.error("Token is malformed");
  }
}

export function setAuthToken(token: string) {
  return localStorage.setItem(AUTH_TOKEN_KEY, token);
}

export function setRefreshToken(token: string) {
  return localStorage.setItem(REFRESH_TOKEN_KEY, token);
}

export function isAuthed() {
  // Is authed if it has either an auth or refresh token
  return !!getAuthToken() || !!getRefreshToken();
}

export function clearItems() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
}
