import { ILike } from "@/types/like/like.models";
import { apiClient } from "../client";

interface ILikeService {
  get(url: string): Promise<ILike>;
  getByRestaurants(url: string, restaurantIds: number[]): Promise<ILike[]>;

  like(url: string, restaurantId: number): Promise<ILike>;
  dislike(url: string): Promise<void>;
}

module LikeServiceImpl {
  export async function get(url: string) {
    const response = await apiClient.get<ILike>(url);
    return response.data;
  }

  export async function getByRestaurants(url: string, restaurantIds: number[]) {
    const response = await apiClient.get<ILike[]>(url, {
      params: {
        restaurantId: restaurantIds,
      },
    });
    return response.data;
  }

  export async function like(url: string, restaurantId: number) {
    const response = await apiClient.post(
      url,
      { restaurantId },
      {
        headers: {
          "Content-Type": "application/json",
        },
      },
    );

    return response.data;
  }

  export async function dislike(url: string) {
    const response = await apiClient.delete(url);

    return response.data;
  }
}

const LikeService: ILikeService = LikeServiceImpl;
export { LikeService };
