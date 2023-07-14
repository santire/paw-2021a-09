import { useMutation, useQuery, useQueryClient } from "react-query";
import { RestaurantService } from "../api/services/RestaurantService";
import { Page, PageParams } from "../types/page";
import { useAuth } from "./useAuth";
import {
  IRestaurant,
  IRestaurantResponse,
} from "../types/restaurant/restaurant.models";
import { UserService } from "../api/services/UserService";
import { LikeService } from "../api/services/LikeService";
import { RestaurantFilterParams } from "../types/filters";

const NO_FILTER = {};
const restaurantKeys = {
  all: ["restaurants"] as const,
  lists: () => [...restaurantKeys.all, "list"] as const,
  list: (filters: RestaurantFilterParams) =>
    [...restaurantKeys.lists(), { ...filters }] as const,
  popular: () => [...restaurantKeys.lists(), "popular"] as const,
  hot: () => [...restaurantKeys.lists(), "hot"] as const,
  details: () => [...restaurantKeys.all, "detail"] as const,
  detail: (id: number) => [...restaurantKeys.details(), id] as const,
};

export function useGetRestaurants(
  pageParams: PageParams,
  filters?: RestaurantFilterParams
) {
  const { isAuthenticated, userId } = useAuth();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.list(filters ?? NO_FILTER),
    queryFn: async () =>
      withLikes(
        () => RestaurantService.getAll(pageParams, filters),
        isAuthenticated,
        userId
      ),
  });
}

export function useGetPopularRestaurants() {
  const { isAuthenticated, userId } = useAuth();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.popular(),
    queryFn: async () =>
      withLikes(() => RestaurantService.getPopular(), isAuthenticated, userId),
  });
}

export function useGetHotRestaurants() {
  const { isAuthenticated, userId } = useAuth();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.hot(),
    queryFn: async () =>
      withLikes(() => RestaurantService.getHot(), isAuthenticated, userId),
  });
}

export function useLikeRestaurant() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: LikeService.like,
    onSuccess: (_, restaurantId, _2) => {
      queryClient.setQueriesData<Page<IRestaurant[]>>(
        restaurantKeys.all,
        (prev) => ({
          meta: prev!.meta,
          data: prev!.data.map((r) =>
            r.id === restaurantId
              ? { ...r, likes: r.likes + 1, likedByUser: true }
              : r
          ),
        })
      );
    },
  });
}

export function useDislikeRestaurant() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: LikeService.dislike,
    onSuccess: (_, restaurantId, _2) => {
      queryClient.setQueriesData<Page<IRestaurant[]>>(
        restaurantKeys.all,
        (prev) => ({
          meta: prev!.meta,
          data: prev!.data.map((r) =>
            r.id === restaurantId
              ? { ...r, likes: r.likes - 1, likedByUser: false }
              : r
          ),
        })
      );
    },
  });
}

async function withLikes(
  getter: () => Promise<Page<IRestaurantResponse[]>>,
  isAuthenticated: boolean,
  userId: number
) {
  const { data, meta } = await getter();
  // If user is authenticated, stiches likedBy status
  if (isAuthenticated) {
    const ids = data.map((r) => r.id);
    const likes = await UserService.getLikesByRestaurants(userId, ids);
    const newData = data.map((r) => ({
      ...r,
      likedByUser: likes.find((l) => l.restaurantId === r.id)?.liked || false,
    }));
    return { data: newData, meta };
  }

  const newData = data.map((r) => ({ ...r, likedByUser: false }));
  return { data: newData, meta };
}
