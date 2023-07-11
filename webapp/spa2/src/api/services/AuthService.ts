import { IUser, IUserLogin, IUserRegister } from "../../types/user/user.models";
import { apiClient, apiErrorHandler } from "../client";

const PATH = "/users";
interface IAuthService {
  login(data: IUserLogin): Promise<{ token: string; userId: number }>;
  activate(token: string): Promise<void>;
  requestPasswordReset(email: string): Promise<void>;
  resetPassword(token: string, password: string): Promise<void>;
}

module AuthServiceImp {
  export async function activate(token: string): Promise<void> {
    try {
      const response = await apiClient.put(PATH, null, {
        params: { type: "activation", token },
      });

      if (response.status == 204) {
        return response.data;
      } else {
        // Only successful case should be 204, there shouldn't be any other 2XX
        throw new Error("Unexpected 2XX response", {
          cause: {
            code: "unknown_error",
            status: "" + response.status,
          },
        });
      }
    } catch (error) {
      throw new Error("Unexpected error", { cause: apiErrorHandler(error) });
    }
  }

  export async function requestPasswordReset(email: string): Promise<void> {
    try {
      const response = await apiClient.post(PATH, null, {
        params: { email },
      });

      if (response.status == 204) {
        return response.data;
      } else {
        // Only successful case should be 204, there shouldn't be any other 2XX
        throw new Error("Unexpected 2XX response", {
          cause: {
            code: "unknown_error",
            status: "" + response.status,
          },
        });
      }
    } catch (error) {
      throw new Error("Unexpected error", { cause: apiErrorHandler(error) });
    }
  }

  export async function resetPassword(
    token: string,
    password: string
  ): Promise<void> {
    try {
      const response = await apiClient.put(
        PATH,
        { password, repeatPassword: password },
        {
          params: {
            type: "reset",
            token,
          },
        }
      );
      if (response.status == 204) {
        return response.data;
      } else {
        // Only successful case should be 204, there shouldn't be any other 2XX
        throw new Error("Unexpected 2XX response", {
          cause: {
            code: "unknown_error",
            status: "" + response.status,
          },
        });
      }
    } catch (error) {
      throw new Error("Unexpected error", { cause: apiErrorHandler(error) });
    }
  }

  export async function login({
    email,
    password,
  }: IUserLogin): Promise<{ token: string; userId: number }> {
    try {
      const response = await apiClient.get<IUser>("/users", {
        // Perform BASIC AUTH in header to get both the token
        // and the user info of the user with specific email
        auth: { username: email, password: password },
        params: { email },
      });

      if (
        "authorization" in response.headers &&
        response.headers["authorization"]?.startsWith("Bearer")
      ) {
        // Retrieve the JWT token from the response headers
        const token = response.headers["authorization"].slice(7);
        const userId = response.data.userId;

        return {
          token,
          userId,
        };
      } else {
        throw new Error("Token Error", {
          cause: {
            code:
              "authorization" in response.headers
                ? "invalid_jwt_token"
                : "missing_authorization_header",
            status: "" + response.status,
          },
        });
      }
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
        }),
      });
    }
  }

  export async function register(user: IUserRegister) {
    const response = await apiClient.post(PATH, { user });
    return response.data;
  }
}

const AuthService: IAuthService = AuthServiceImp;
export { AuthService };
