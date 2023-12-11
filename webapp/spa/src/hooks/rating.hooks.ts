import { IRestaurant } from "@/types/restaurant/restaurant.models";
import { Page } from "@/types/page";
import { useUser } from "./user.hooks";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { queries } from "@/queries";
import { IClientRating } from "@/types/rating/rating.model";
import { RatingService } from "@/api/services/RatingService";

export function useGetRating({ self, id }: IRestaurant) {
  const user = useUser();
  const rateUrl = user.data?.ratings;

  return useQuery({
    queryKey: queries.ratings.detail(self).queryKey,
    queryFn: async () => {
      const clientRating: IClientRating = { rated: false };
      if (!rateUrl) {
        return clientRating;
      }
      const [resp] = await RatingService.getByRestaurants(rateUrl, [id]);
      if (resp) {
        clientRating.rated = true;
        clientRating.rating = resp;
      }
      return clientRating;
    },
    enabled: user.isSuccess && !user.isStale && !!rateUrl,
  });
}

export function useRateRestaurant({ self, id }: IRestaurant) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      url,
      restaurantId,
      rating,
    }: {
      url: string;
      restaurantId: number;
      rating: number;
    }) => {
      const ratingCache = queryClient.getQueryData<IClientRating>(
        queries.ratings.detail(self).queryKey,
      );
      // If rating exists update
      if (ratingCache?.rating) {
        return RatingService.updateRating(ratingCache.rating.self, rating);
      }
      // Otherwise create rating
      return RatingService.rate(url, restaurantId, rating);
    },
    onSuccess: (rating) => {
      queryClient.invalidateQueries(queries.restaurants.detail(id));
      return queryClient.setQueryData(queries.ratings.detail(self).queryKey, {
        rated: true,
        rating,
      });
    },
  });
}
