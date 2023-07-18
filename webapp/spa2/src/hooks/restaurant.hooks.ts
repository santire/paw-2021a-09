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
import { useSearchParams } from "react-router-dom";
import { ServerError, isServerError } from "../api/client";

interface QueryOptions {
  onSuccess?: () => void;
  onError?: (error: ServerError) => void;
}

const restaurantKeys = {
  all: ["restaurants"] as const,
  lists: (userId: number) => [...restaurantKeys.all, "list", userId] as const,
  list: (userId: number, filters?: PageParams & RestaurantFilterParams) =>
    [...restaurantKeys.lists(userId), { filters }] as const,
  owned: (userId: number, pageParams?: PageParams) => [
    ...restaurantKeys.list(userId),
    "owned",
    { pageParams },
  ],
  popular: (userId: number) =>
    [...restaurantKeys.lists(userId), "popular"] as const,
  hot: (userId: number) => [...restaurantKeys.lists(userId), "hot"] as const,
  details: () => [...restaurantKeys.all, "detail"] as const,
  detail: (id: number) => [...restaurantKeys.details(), id] as const,
};

export function useGetRestaurants(
  params?: PageParams & RestaurantFilterParams
) {
  const { isAuthenticated, userId } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.list(userId, params),
    enabled: !!params,
    queryFn: async () =>
      withLikes(
        () => RestaurantService.getAll({ ...params! }),
        isAuthenticated,
        userId
      ),
    onSuccess: (data) => {
      const page = parseInt(searchParams.get("page") || "NaN");
      // If page is invalid resets back to first
      if (page > data.meta.maxPages) {
        searchParams.delete("page");
        setSearchParams(searchParams);
      }
    },
  });
}

export function useGetOwnedRestaurants(params?: PageParams) {
  const { isAuthenticated, userId } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.owned(userId, params),
    enabled: !!params && !!userId,
    queryFn: async () =>
      withLikes(
        () =>
          RestaurantService.getOwnedRestaurants(userId, {
            ...params!,
          }),
        isAuthenticated,
        userId
      ),
    onSuccess: (data) => {
      const page = parseInt(searchParams.get("page") || "NaN");
      // If page is invalid resets back to first
      if (page > data.meta.maxPages) {
        searchParams.delete("page");
        setSearchParams(searchParams);
      }
    },
  });
}

export function useGetPopularRestaurants() {
  const { isAuthenticated, userId } = useAuth();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.popular(userId),
    queryFn: async () =>
      withLikes(() => RestaurantService.getPopular(), isAuthenticated, userId),
  });
}

export function useGetHotRestaurants() {
  const { isAuthenticated, userId } = useAuth();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.hot(userId),
    queryFn: async () =>
      withLikes(() => RestaurantService.getHot(), isAuthenticated, userId),
  });
}

export function useCreateRestaurant(options?: QueryOptions) {
  const queryClient = useQueryClient();
  const { userId } = useAuth();

  return useMutation({
    mutationFn: RestaurantService.create,
    onSuccess: () => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(restaurantKeys.lists(userId));
      // queryClient.setQueriesData<Page<IRestaurant[]>>(
      //   restaurantKeys.all,
      //   (prev) => ({
      //     meta: prev!.meta,
      //     data: [...prev!.data, { ...data, likedByUser: false }],
      //   })
      // );

      if (options?.onSuccess) {
        options.onSuccess();
      }
    },

    onError: ({ cause }) => {
      if (isServerError(cause) && options?.onError) {
        options.onError(cause);
      } else {
        console.log("An error here shouldn't be happening");
      }
    },
  });
}

export function useDeleteRestaurant(options?: QueryOptions) {
  const queryClient = useQueryClient();
  const { userId } = useAuth();

  return useMutation({
    mutationFn: RestaurantService.deleteRestaurant,
    onSuccess: () => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(restaurantKeys.lists(userId));
      if (options?.onSuccess) {
        options.onSuccess();
      }
    },

    onError: ({ cause }) => {
      if (isServerError(cause) && options?.onError) {
        options.onError(cause);
      } else {
        console.log("An error here shouldn't be happening");
      }
    },
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
  if (isAuthenticated && !!userId) {
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
