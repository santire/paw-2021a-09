/* eslint-disable @typescript-eslint/no-namespace */
/* eslint-disable @typescript-eslint/prefer-namespace-keyword */
import { apiClient, apiErrorHandler } from "../client";

// const PATH = (id: number) => `/restaurants/${id}/likes`;
const PATH = (userId: number, restaurantId: number) => `/users/${userId}/likes/${restaurantId}`;

interface ILikeService {
  like(userId: number, restaurantId: number): Promise<void>;
  dislike(userId: number, restaurantId: number): Promise<void>;
}

module LikeServiceImpl {
  export async function like(userId: number, restaurantId: number) {
    try {
      const response = await apiClient.post(PATH(userId, restaurantId), null, {
        headers: {
          "Content-Type": "application/json",
        },
      });

      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
          AlreadyLikedException: { code: "already_liked" },
        }),
      });
    }
  }

  export async function dislike(userId: number, restaurantId: number) {
    try {
      const response = await apiClient.delete(PATH(userId, restaurantId));

      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
          NotLikedException: { code: "not_liked_liked" },
        }),
      });
    }
  }
}

const LikeService: ILikeService = LikeServiceImpl;
export { LikeService };
