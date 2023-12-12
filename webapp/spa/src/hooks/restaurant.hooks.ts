import { LikeService } from "@/api/services/LikeService";
import { RestaurantService } from "@/api/services/RestaurantService";
import { queries } from "@/queries";
import {
  QueryClient,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import { useUser } from "./user.hooks";
import {
  IRestaurant,
  IRestaurantRegister,
} from "@/types/restaurant/restaurant.models";
import { IClientLike } from "@/types/like/like.models";
import { isAuthed } from "@/utils/AuthStorage";
import { useRestaurantFilterAndPage } from "@/context/RestaurantFilterAndPageContext";

export function useGetRestaurant(id: number) {
  return useQuery(queries.restaurants.detail(id));
}
export function useGetRestaurants() {
  const { filterParams, pageParams } = useRestaurantFilterAndPage();
  const page = pageParams?.page ?? 1;
  const params = { ...filterParams, ...pageParams, page };
  const queryClient = useQueryClient();
  const user = useUser();
  const query = useQuery({
    queryKey: queries.restaurants.list(params).queryKey,
    queryFn: async () => {
      const resp = await RestaurantService.getFilteredBy({
        ...params,
        pageAmount: 6,
      });
      // push Restaurant detail state
      resp.data.forEach((r) => {
        queryClient.setQueriesData(queries.restaurants.detail(r.id), r);
      });

      if (!user.isPaused && user.isSuccess && user.data) {
        await updateLikesCache(queryClient, user.data.likes, resp.data);
      }
      return resp;
    },
  });
  return query;
}

export function useCreateRestaurant() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      userId,
      params,
    }: {
      userId: number;
      params: IRestaurantRegister;
    }) => RestaurantService.create(userId, params),
    onSuccess: (data) => {
      // Might change browsing lists, invalidate all
      queryClient.invalidateQueries({
        queryKey: queries.restaurants.list._def,
      });
      // Set restaurant data
      return queryClient.setQueryData(
        queries.restaurants.detail(data.id).queryKey,
        data,
      );
    },
  });
}

export function useDeleteRestaurant({ id }: IRestaurant) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: RestaurantService.remove,
    onSuccess: () => {
      // Might change browsing lists, invalidate all
      queryClient.invalidateQueries({
        queryKey: queries.restaurants.list._def,
      });
      // Clear restaurant data
      return queryClient.invalidateQueries(queries.restaurants.detail(id));
    },
  });
}

async function updateLikesCache(
  queryClient: QueryClient,
  url: string,
  restaurants: IRestaurant[],
) {
  if (!isAuthed()) {
    return;
  }
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
