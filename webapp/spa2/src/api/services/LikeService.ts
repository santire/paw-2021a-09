/* eslint-disable @typescript-eslint/no-namespace */
/* eslint-disable @typescript-eslint/prefer-namespace-keyword */
import { apiClient, apiErrorHandler } from "../client";

interface ILikeService {
  like(likelocation: string): Promise<void>;
  dislike(likeLocation: string): Promise<void>;
}

module LikeServiceImpl {
  export async function like(likeLocation: string) {
    try {
      const response = await apiClient.post(likeLocation, null, {
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

  export async function dislike(likeLocation: string) {
    try {
      const response = await apiClient.delete(likeLocation);

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
