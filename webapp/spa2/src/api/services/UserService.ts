/* eslint-disable @typescript-eslint/no-namespace */
/* eslint-disable @typescript-eslint/prefer-namespace-keyword */
import { ILike } from "../../types/like";
import {
  IUser,
  IUserRegister,
  IUserUpdate,
} from "../../types/user/user.models";
import { apiClient, apiErrorHandler } from "../client";

const PATH = "/users";
interface IUserService {
  getById(userId: number): Promise<IUser>;
  create(user: IUserRegister): Promise<IUser>;
  update(userId: number, user: IUserUpdate): Promise<IUser>;
  getLikesByRestaurants(
    userId: number,
    restaurantIds: number[]
  ): Promise<ILike[]>;
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

  export async function create(user: IUserRegister) {
    try {
      const response = await apiClient.post<IUser>(PATH, {
        ...user,
      });

      return response.data;
    } catch (error) {
      throw new Error("Response Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          ConstraintViolationException: { code: "validation_error" },
          PersistenceException: {
            code: "validation_error",
            errors: [{ subject: "username", message: "username in use" }],
          },
          UsernameInUseException: {
            code: "validation_error",
            errors: [{ subject: "username", message: "username in use" }],
          },
        }),
      });
    }
  }

  export async function update(userId: number, user: IUserUpdate) {
    try {
      const response = await apiClient.put(`${PATH}/${userId}`, {
        ...user,
      });

      return response.data;
    } catch (error) {
      throw new Error("Response Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          ConstraintViolationException: { code: "validation_error" },
          PersistenceException: {
            code: "validation_error",
            errors: [{ subject: "username", message: "username in use" }],
          },
          UsernameInUseException: {
            code: "validation_error",
            errors: [{ subject: "username", message: "username in use" }],
          },
        }),
      });
    }
  }

  export async function getLikesByRestaurants(
    userId: number,
    restaurantIds: number[]
  ) {
    try {
      const response = await apiClient.get<ILike[]>(`${PATH}/${userId}/likes`, {
        params: {
          restaurantId: restaurantIds,
        },
      });
      return response.data;
    } catch (error) {
      throw new Error("General Error");
    }
  }
}

const UserService: IUserService = UserServiceImpl;
export { UserService };
