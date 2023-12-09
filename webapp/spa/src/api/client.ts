import axios from "axios";
import { clearItems, getAuthToken, getRefreshToken, setAuthToken, setRefreshToken } from "@/utils/AuthStorage";

const BASE_CONFIG = {
  baseURL: import.meta.env.VITE_HOST_API || "http://localhost:8080/app/api",
  paramsSerializer: {
    indexes: null,
  },
  headers: {
    common: {
      "Content-Type": "application/json",
    },
  },
};

const apiClient = axios.create(BASE_CONFIG);

apiClient.interceptors.request.use(
  (config) => {
    const token = getAuthToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(

  (response) => {
    // If a response contains Auth headers, update Auth Storage
    const authTokenHeader: string = response.headers['x-auth-token'];
    const refreshTokenHeader: string = response.headers['x-refresh-token'];

    if (authTokenHeader && authTokenHeader.startsWith("Bearer ")) {
      setAuthToken(authTokenHeader.replace(/^Bearer\s+/i, ""))
    }

    if (refreshTokenHeader && refreshTokenHeader.startsWith("Bearer ")) {
      setRefreshToken(refreshTokenHeader.replace(/^Bearer\s+/i, ""))
    }

    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    // If the error status is 401 and there is no originalRequest._retry flag,
    // it means the token has expired and we need to refresh it
    if (error.response.status === 401) {

      if (!originalRequest._retry) {
        originalRequest._retry = true;
        const refreshToken = getRefreshToken();

        // If refresh token is not found, error falls through for the app to handle
        if (refreshToken) {
          // Retry the original request with the new token
          originalRequest.headers['X-Refresh-Token'] = `Bearer ${refreshToken}`;
          const resp = await axios(originalRequest);

          const authTokenHeader: string = resp.headers['x-auth-token'];
          const refreshTokenHeader: string = resp.headers['x-refresh-token'];

          if (authTokenHeader && authTokenHeader.startsWith("Bearer ")) {
            setAuthToken(authTokenHeader.replace(/^Bearer\s+/i, ""))
          }

          if (refreshTokenHeader && refreshTokenHeader.startsWith("Bearer ")) {
            setRefreshToken(refreshTokenHeader.replace(/^Bearer\s+/i, ""))
          }
          return resp;
        }
      } else {
        // This means refresh failed so clear AuthStorage
        clearItems();
      }
    }

    return Promise.reject(error);
  }
);

function isServerErrorResponse(object: any): object is ServerErrorResponse {
  return "status" in object && "type" in object && "message" in object;
}

export function isServerError(object: any): object is ServerError {
  return "status" in object && "code" in object;
}

export function apiErrorHandler(
  error: any,
  mappers?: {
    [key: string]: {
      code?: string;
      status?: string;
      errors?: ValidationError[];
    };
  }
): ServerError {
  if (
    axios.isAxiosError(error) &&
    error.response &&
    isServerErrorResponse(error.response.data)
  ) {
    const data = error.response?.data as ServerErrorResponse;
    const mapperOut = mappers && mappers[data.type];
    let errors: ValidationError[] = [];

    if (data.type === "ConstraintViolationException" && data.errors) {
      errors = data.errors.map((e) => {
        const [subject, message] = e.split(":");
        return {
          subject,
          message,
        };
      });
    }
    return {
      code: mapperOut?.code || error.response.statusText || "500",
      status: mapperOut?.status || "" + error.response.status,
      errors: mapperOut?.errors || errors,
    };
  }
  console.log("API HANDLER ERROR: ", error);
  return {
    code: "unknown_error",
    status: "500",
  };
}

interface ValidationError {
  subject: string;
  message: string;
}

export interface ServerErrorResponse {
  status: string;
  type: string;
  message: string;
  errors?: string[];
}

export interface ServerError {
  status: string;
  code: string;
  errors?: ValidationError[];
}


export { apiClient };
