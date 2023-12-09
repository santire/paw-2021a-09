import { IUser, IUserLogin } from "@/types/user/user.models";
import { apiClient, apiErrorHandler } from "../client";

const PATH = "/users";

export interface AuthParams {
  username: string;
  password: string;
}

interface IUserService {
  getByEmail(email: string, auth?: IUserLogin): Promise<IUser>;

  // create(user: IUserRegister): Promise<IUser>;

  // activate(url: string, token: string): Promise<void>;
  // requestPasswordReset(email: string): Promise<void>;
  // resetPassword(url: string, token: string, params: IUserResetPassword): Promise<void>;

  // update(url: string, user: IUserUpdate): Promise<void>;
}

module UserServiceImpl {
  export async function getByEmail(email: string, auth?: IUserLogin) {
    const response = await apiClient.get<IUser>(PATH, {
      auth: auth
        ? { username: auth.email, password: auth.password }
        : undefined,
      params: { email },
    });
    return response.data;
  }
}

const UserService: IUserService = UserServiceImpl;
export { UserService };
