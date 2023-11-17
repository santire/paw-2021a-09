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
import { RatingService } from "../api/services/RatingService";
import { ratingKeys } from "./ratings.hooks";

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
  details: (userId: number) =>
    [...restaurantKeys.all, "detail", userId] as const,
  detail: (userId: number, id?: number) =>
    [...restaurantKeys.details(userId), id] as const,
};

export function useGetRestaurants(
  params?: PageParams & RestaurantFilterParams
) {
  const { isAuthenticated, userId } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.list(userId, params),
    queryFn: () =>
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

export function useGetRestaurant(restaurantId: number) {
  const { isAuthenticated, userId } = useAuth();
  return useQuery<IRestaurant>({
    queryKey: restaurantKeys.detail(userId, restaurantId),
    queryFn: () =>
      withLike(
        () => RestaurantService.getById(restaurantId),
        isAuthenticated,
        userId
      ),
  });
}

export function useGetOwnedRestaurants(params?: PageParams) {
  const { isAuthenticated, userId } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.owned(userId, params),
    enabled: !!params && !!userId,
    queryFn: () =>
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
    queryFn: () =>
      withLikes(RestaurantService.getPopular, isAuthenticated, userId),
  });
}

export function useGetHotRestaurants() {
  const { isAuthenticated, userId } = useAuth();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.hot(userId),
    queryFn: () =>
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

export function useUpdateRestaurant(options?: QueryOptions) {
  const queryClient = useQueryClient();
  const { userId } = useAuth();

  return useMutation({
    mutationFn: RestaurantService.update,
    onSuccess: (_, { restaurantId }) => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(restaurantKeys.lists(userId));
      queryClient.invalidateQueries(
        restaurantKeys.detail(userId, restaurantId)
      );

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
  const { userId } = useAuth();

  return useMutation({
    mutationFn: (restaurantId: number) => LikeService.like(userId, restaurantId),
    onSuccess: (_, restaurantId, _2) => {
      if (
        queryClient.getQueryData(restaurantKeys.detail(userId, restaurantId))
      ) {
        queryClient.setQueryData<IRestaurant>(
          restaurantKeys.detail(userId, restaurantId),
          (prev) => ({ ...prev!, likes: prev!.likes + 1, likedByUser: true })
        );
      }

      // For all queries of type list, if they have data => update like
      for (const query of queryClient.getQueriesData(
        restaurantKeys.lists(userId)
      )) {
        if (query[1]) {
          queryClient.setQueryData<Page<IRestaurant[]>>(query[0], (prev) => ({
            meta: prev!.meta,
            data: prev!.data.map((r) =>
              r.id === restaurantId
                ? { ...r, likes: r.likes + 1, likedByUser: true }
                : r
            ),
          }));
        }
      }
    },
  });
}

export function useDislikeRestaurant() {
  const queryClient = useQueryClient();
  const { userId } = useAuth();

  return useMutation({
    mutationFn: (restaurantId: number) => LikeService.dislike(userId, restaurantId),
    onSuccess: (_, restaurantId, _2) => {
      if (
        queryClient.getQueryData(restaurantKeys.detail(userId, restaurantId))
      ) {
        queryClient.setQueryData<IRestaurant>(
          restaurantKeys.detail(userId, restaurantId),
          (prev) => ({ ...prev!, likes: prev!.likes - 1, likedByUser: false })
        );
      }

      // For all queries of type list, if they have data => update like
      for (const query of queryClient.getQueriesData(
        restaurantKeys.lists(userId)
      )) {
        if (query[1]) {
          queryClient.setQueryData<Page<IRestaurant[]>>(query[0], (prev) => ({
            meta: prev!.meta,
            data: prev!.data.map((r) =>
              r.id === restaurantId
                ? { ...r, likes: r.likes - 1, likedByUser: false }
                : r
            ),
          }));
        }
      }
    },
  });
}

export function useRateRestaurant(options?: QueryOptions) {
  const queryClient = useQueryClient();
  const { userId } = useAuth();
  return useMutation({
    mutationFn: RatingService.rate,

    onSuccess: (_, { restaurantId }) => {
      // Invalidate because it needs to recalculate rating
      queryClient.invalidateQueries(restaurantKeys.lists(userId));
      queryClient.invalidateQueries(ratingKeys.detail(userId, restaurantId));
      queryClient.invalidateQueries(
        restaurantKeys.detail(userId, restaurantId)
      );

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

async function withLikes(
  getter: () => Promise<Page<IRestaurantResponse[]>>,
  isAuthenticated: boolean,
  userId: number
): Promise<Page<IRestaurant[]>> {
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

async function withLike(
  getter: () => Promise<IRestaurantResponse>,
  isAuthenticated: boolean,
  userId: number
) {
  const restaurant = await getter();
  // If user is authenticated, stiches likedBy status
  if (isAuthenticated && !!userId) {
    const likes = await UserService.getLikesByRestaurants(userId, [
      restaurant.id,
    ]);
    const newData = {
      ...restaurant,
      likedByUser:
        likes.find((l) => l.restaurantId === restaurant.id)?.liked || false,
    };
    return newData;
  }

  const newData = { ...restaurant, likedByUser: false };
  return newData;
}
