import axios from "axios";

const BASE_CONFIG = {
  baseURL:
    import.meta.env.REACT_APP_HOST_API || "http://localhost:8080/app/api",
  paramsSerializer: {
    indexes: null,
  },
  headers: {
    common: {
      "Content-Type": "application/json",
    },
  },
};

// TODO: Interceptor for general error handling and/or refresh token

const apiClient = axios.create(BASE_CONFIG);

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // Let each service handle API response errors
      return Promise.reject(error);

      // Catch and log other errors TODO: set context state for specific cases? (CORS, API down, etc..)
    } else if (error.request) {
      // The request was made but no response was received
      // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
      // http.ClientRequest in node.js
      console.log("Error Request", error.request);
    } else {
      // Something happened in setting up the request that triggered an Error
      console.log("Error", error.message);
    }
    console.log("Error Unknown:", error);
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
    [key: string]: { code?: string; status?: string; errors?: string[] };
  }
): ServerError {
  if (
    axios.isAxiosError(error) &&
    error.response &&
    isServerErrorResponse(error.response.data)
  ) {
    const data = error.response?.data as ServerErrorResponse;
    const mapperOut = mappers && mappers[data.type];
    return {
      code: mapperOut?.code || error.response.statusText || "500",
      status: mapperOut?.status || "" + error.response.status,
      ...(mapperOut?.errors || {}),
    };
  }
  console.log("API HANDLER ERROR: ", error);
  return {
    code: "unknown_error",
    status: "500",
  };
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
  errors?: string[];
}

export { apiClient };
