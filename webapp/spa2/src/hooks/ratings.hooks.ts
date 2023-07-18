import { useQuery } from "react-query";
import { useAuth } from "./useAuth";
import { RatingService } from "../api/services/RatingService";

export const ratingKeys = {
  all: ["ratings"] as const,
  detail: (userId: number, restaurantId: number) =>
    [...ratingKeys.all, userId, restaurantId] as const,
};

export function useGetRating(restaurantId: number) {
  const { userId } = useAuth();

  return useQuery({
    queryKey: ratingKeys.detail(userId, restaurantId),
    queryFn: () => RatingService.getById(userId, restaurantId),
    staleTime: Infinity,
    enabled: !!userId,
  });
}
