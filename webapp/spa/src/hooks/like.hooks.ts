import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { queries } from "@/queries";
import { LikeService } from "@/api/services/LikeService";
import { IRestaurant } from "@/types/restaurant/restaurant.models";
import { useUser } from "./user.hooks";
import { IClientLike } from "@/types/like/like.models";

export function useGetLike({ self, id }: IRestaurant) {
  const user = useUser();
  const likeUrl = user.data?.likes;

  return useQuery({
    queryKey: queries.likes.detail(self).queryKey,
    queryFn: async () => {
      const clientLike: IClientLike = { liked: false };
      if (!likeUrl) {
        return clientLike;
      }
      // const resp = await LikeService.get(`${likeUrl}/${id}`);
      const [resp] = await LikeService.getByRestaurants(likeUrl, [id]);
      if (resp) {
        clientLike.liked = true;
        clientLike.like = resp;
      }
      return clientLike;
    },
    enabled: user.isSuccess && !user.isStale && !!likeUrl,
  });
}

export function useLikeRestaurant({ self }: IRestaurant) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      url,
      restaurantId,
    }: {
      url: string;
      restaurantId: number;
    }) => LikeService.like(url, restaurantId),
    onSuccess: (like, { restaurantId }) => {
      queryClient.invalidateQueries({
        queryKey: [...queries.restaurants.list._def, "popular"],
      });
      queryClient.invalidateQueries(queries.restaurants.detail(restaurantId));
      return queryClient.setQueryData(queries.likes.detail(self).queryKey, {
        liked: true,
        like,
      });
    },
  });
}

export function useDislikeRestaurant({ self, id }: IRestaurant) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ url }: { url: string }) => LikeService.dislike(url),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [...queries.restaurants.list._def, "popular"],
      });
      queryClient.invalidateQueries(queries.restaurants.detail(id));
      return queryClient.setQueryData(queries.likes.detail(self).queryKey, {
        liked: false,
      });
    },
  });
}
