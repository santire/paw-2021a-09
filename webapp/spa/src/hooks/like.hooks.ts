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
      try {
        const resp = await LikeService.get(`${likeUrl}/${id}`);
        clientLike.liked = true;
        clientLike.like = resp;
      } finally {
        return clientLike;
      }
    },
    enabled: !!likeUrl,
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
    onSuccess: (like) => {
      queryClient.setQueriesData(queries.likes.detail(self), {
        liked: true,
        like,
      });
    },
  });
}

export function useDislikeRestaurant({ self }: IRestaurant) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ url }: { url: string }) => LikeService.dislike(url),
    onSuccess: () => {
      queryClient.invalidateQueries(queries.likes.detail(self));
    },
  });
}
