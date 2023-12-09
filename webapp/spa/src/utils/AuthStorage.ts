const AUTH_TOKEN_KEY = "GOURMETABLE-AUTH-TOKEN"
const REFRESH_TOKEN_KEY = "GOURMETABLE-REFRESH-TOKEN"
const USER_ID_KEY = "GOURMETABLE-USER"


export function getAuthToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY)
}

export function getRefreshToken() {
  return localStorage.getItem(REFRESH_TOKEN_KEY);
}

export function getUserId() {
  const userIdString = localStorage.getItem(USER_ID_KEY);
  return userIdString ? parseInt(userIdString) : null;
}

export function setAuthToken(token: string) {
  return localStorage.setItem(AUTH_TOKEN_KEY, token)
}

export function setRefreshToken(token: string) {
  return localStorage.setItem(REFRESH_TOKEN_KEY, token);
}

export function setUserId(id: number) {
  return localStorage.setItem(USER_ID_KEY, "" + id);
}

export function clearItems() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
  localStorage.removeItem(USER_ID_KEY);
}
