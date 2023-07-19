import { apiClient, apiErrorHandler } from "../client";

const RESTAURANT_PATH = (id: number) => `/restaurants/${id}/ratings`;
const USER_PATH = (userId: number) => `/users/${userId}/ratings`;

interface RateParams {
  restaurantId: number;
  rating: number;
}

interface IRatingService {
  rate(params: RateParams): Promise<void>;
  getById(userId: number, restaurantId: number): Promise<{ rating: number }>;
}

module RatingServiceImpl {
  export async function rate({ restaurantId, rating }: RateParams) {
    try {
      const response = await apiClient.post(
        RESTAURANT_PATH(restaurantId),
        {
          rating,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
        }),
      });
    }
  }

  export async function getById(userId: number, restaurantId: number) {
    try {
      const response = await apiClient.get<{ rating: number }>(
        USER_PATH(userId),
        {
          params: { restaurantId },
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
        }),
      });
    }
  }
}

const RatingService: IRatingService = RatingServiceImpl;
export { RatingService };
