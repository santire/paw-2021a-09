import { IUser } from "../../types/user/user.models";
import { apiClient, apiErrorHandler } from "../client";

const PATH = "/users";
interface IUserService {
  getById(userId: number): Promise<IUser>;
}

module UserServiceImpl {
  export async function getById(userId: number) {
    try {
      const response = await apiClient.get<IUser>(`${PATH}/${userId}`);
      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
        }),
      });
    }
  }
}

const UserService: IUserService = UserServiceImpl;
export { UserService };
