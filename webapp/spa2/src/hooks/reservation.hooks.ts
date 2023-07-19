import { useMutation, useQuery, useQueryClient } from "react-query";
import { ServerError, isServerError } from "../api/client";
import { ReservationService } from "../api/services/ReservationService";
import { Page, PageParams } from "../types/page";
import { useSearchParams } from "react-router-dom";
import { IReservation } from "../types/reservation/reservation.models";
import { useAuth } from "./useAuth";

interface QueryOptions {
  onSuccess?: () => void;
  onError?: (error: ServerError) => void;
}

const reservationKeys = {
  all: ["reservations"] as const,
  lists: (userId: number) => [...reservationKeys.all, userId, "list"] as const,
  list: (userId: number, restaurantId: number) =>
    [...reservationKeys.lists(userId), restaurantId] as const,
  userList: (userId: number, filterType: string, filters?: PageParams) =>
    [...reservationKeys.lists(userId), filterType, { filters }] as const,
  restaurantList: (
    userId: number,
    restaurantId: number,
    filterType: string,
    filters?: PageParams
  ) =>
    [
      ...reservationKeys.lists(userId),
      restaurantId,
      filterType,
      { filters },
    ] as const,
};

export function useGetReservations(filterType: string, params: PageParams) {
  const [searchParams, setSearchParams] = useSearchParams();
  const { userId } = useAuth();
  console.log("filterType: ", filterType);
  return useQuery<Page<IReservation[]>>({
    queryKey: reservationKeys.userList(userId, filterType, params),
    queryFn: () =>
      ReservationService.getUserReservations(userId, filterType, params),
    enabled: !isNaN(userId),
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
export function useGetRestaurantReservation(
  restaurantId: number,
  filterType: string,
  params: PageParams
) {
  const [searchParams, setSearchParams] = useSearchParams();
  const { isAuthenticated, userId } = useAuth();
  return useQuery<Page<IReservation[]>>({
    queryKey: reservationKeys.restaurantList(
      userId,
      restaurantId,
      filterType,
      params
    ),
    queryFn: () =>
      ReservationService.getRestaurantReservations(
        restaurantId,
        filterType,
        params
      ),
    enabled: !!isAuthenticated,
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

export function useCreateReservation(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ReservationService.create,
    onSuccess: () => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(reservationKeys.all);
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
export function useCancelReservation(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ReservationService.cancel,
    onSuccess: () => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(reservationKeys.all);
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

export function useConfirmReservation(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ReservationService.confirm,
    onSuccess: () => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(reservationKeys.all);
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

export function useDenyReservation(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ReservationService.deny,
    onSuccess: () => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(reservationKeys.all);
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
