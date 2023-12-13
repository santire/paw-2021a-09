import { ReservationService } from "@/api/services/ReservationService";
import { queries } from "@/queries";
import { PageParams } from "@/types/page";
import {
  IReservationCancelMessage,
  IReservationRegister,
} from "@/types/reservation/reservation.models";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useUser } from "./user.hooks";
import { IRestaurant } from "@/types/restaurant/restaurant.models";

export function useCreateReservation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      userId,
      restaurantId,
      params,
    }: {
      userId: number;
      restaurantId: number;
      params: IReservationRegister;
    }) => ReservationService.create(userId, restaurantId, params),
    onSuccess: () => {
      // popular order might change
      queryClient.invalidateQueries({
        queryKey: [...queries.restaurants.list._def, "popular"],
      });
      // update current reservations (no need to change history as those are in the past)
      queryClient.invalidateQueries({
        queryKey: [...queries.reservations.user._def, "current"],
      });
    },
  });
}

export function useGetReservations(
  filter: "history" | "current",
  status: "pending" | "confirmed" | "denied" | "any",
  params: PageParams,
) {
  const user = useUser();
  const userId = user.data?.userId;
  console.log("Getting reservations");

  return useQuery({
    queryKey: queries.reservations.user(filter, status, params).queryKey,
    enabled: user.isSuccess && !user.isStale && !!userId,
    queryFn: () =>
      ReservationService.getUserReservations(userId!, filter, status, params),
  });
}

export function useConfirmReservation(id: number) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (url: string) => ReservationService.confirm(url),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [...queries.reservations.restaurant._def, id, "current"],
      });
      // For pending reservations
      queryClient.invalidateQueries(queries.restaurants.detail(id));
    },
  });
}

export function useGetRestaurantReservations(
  restaurantId: number,
  filter: "current" | "history",
  status: "pending" | "confirmed" | "denied" | "any",
  params: PageParams,
) {
  return useQuery({
    queryKey: queries.reservations.restaurant(
      restaurantId,
      filter,
      status,
      params,
    ).queryKey,
    queryFn: () =>
      ReservationService.getRestaurantReservations(
        restaurantId,
        filter,
        status,
        params,
      ),
  });
}

export function useDenyReservation(id: number) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      url,
      message,
    }: {
      url: string;
      message: IReservationCancelMessage;
    }) => ReservationService.deny(url, message),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [...queries.reservations.restaurant._def, id, "current"],
      });
    },
  });
}

export function useCancelReservation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (self: string) => ReservationService.cancel(self),
    onSuccess: () => {
      // Remove from list
      queryClient.invalidateQueries({
        queryKey: [...queries.reservations.user._def, "current"],
      });
    },
  });
}
