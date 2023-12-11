import { LikeService } from "@/api/services/LikeService";
import { RestaurantService } from "@/api/services/RestaurantService";
import { queries } from "@/queries";
import { RestaurantFilterParams } from "@/types/filters";
import { PageParams } from "@/types/page";
import {
  QueryClient,
  keepPreviousData,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import { useUser } from "./user.hooks";
import { IRestaurant } from "@/types/restaurant/restaurant.models";
import { IClientLike } from "@/types/like/like.models";

export function useGetRestaurants(params: PageParams & RestaurantFilterParams) {
  const queryClient = useQueryClient();
  const user = useUser();
  const query = useQuery({
    queryKey: queries.restaurants.list(params).queryKey,
    queryFn: async () => {
      const resp = await RestaurantService.getFilteredBy({
        ...params,
        pageAmount: 6,
      });

      if (!user.isPaused && user.data) {
        await updateLikesCache(queryClient, user.data.likes, resp.data);
      }
      return resp;
    },
  });
  return query;
}

async function updateLikesCache(
  queryClient: QueryClient,
  url: string,
  restaurants: IRestaurant[],
) {
  const restaurantIds = restaurants.map((r) => r.id);
  const likes = await LikeService.getByRestaurants(url, restaurantIds);
  const restaurantSelfs = restaurants.map((r) => r.self);
  restaurantSelfs.forEach((r) => {
    const clientLike: IClientLike = { liked: false };
    const isLiked = likes.find((s) => s.restaurant === r);
    if (isLiked) {
      clientLike.liked = true;
      clientLike.like = isLiked;
    }

    queryClient.setQueryData(queries.likes.detail(r).queryKey, clientLike);
  });
}
