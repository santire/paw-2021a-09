import { apiClient, apiErrorHandler } from "../client";

const PATH = (id: number) => `/restaurants/${id}/likes`;

interface ILikeService {
  like(restaurantId: number): Promise<void>;
  dislike(restaurantId: number): Promise<void>;
}

module LikeServiceImpl {
  export async function like(restaurantId: number) {
    try {
      const response = await apiClient.post(PATH(restaurantId), null, {
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

  export async function dislike(restaurantId: number) {
    try {
      const response = await apiClient.delete(PATH(restaurantId));

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
