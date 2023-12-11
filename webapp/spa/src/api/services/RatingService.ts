import { ILike } from "@/types/like";
import { IRating } from "@/types/rating/rating.model";
import { apiClient } from "../client";

interface IRatingService {
  get(url: string): Promise<IRating>;
  getByRestaurants(url: string, restaurantIds: number[]): Promise<IRating[]>;

  rate(url: string, restaurantId: number, rating: number): Promise<IRating>;
  updateRating(url: string, rating: number): Promise<IRating>;
}

module RatingServiceImpl {
  export async function get(url: string) {
    const response = await apiClient.get<IRating>(url);
    return response.data;
  }

  export async function getByRestaurants(url: string, restaurantIds: number[]) {
    const response = await apiClient.get<IRating[]>(url, {
      params: {
        restaurantId: restaurantIds,
      },
    });
    return response.data;
  }

  export async function rate(
    url: string,
    restaurantId: number,
    rating: number,
  ) {
    const response = await apiClient.post(
      url,
      { restaurantId, rating },
      {
        headers: {
          "Content-Type": "application/json",
        },
      },
    );

    return response.data;
  }

  export async function updateRating(url: string, rating: number) {
    const response = await apiClient.put(
      url,
      { rating },
      {
        headers: {
          "Content-Type": "application/json",
        },
      },
    );

    return response.data;
  }
}

const RatingService: IRatingService = RatingServiceImpl;
export { RatingService };
