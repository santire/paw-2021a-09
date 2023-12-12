import { ReservationService } from "@/api/services/ReservationService";
import { queries } from "@/queries";
import { PageParams } from "@/types/page";
import {
  IReservationRegister,
} from "@/types/reservation/reservation.models";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useUser } from "./user.hooks";

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
  params: PageParams,
) {
  const user = useUser();
  const userId = user.data?.userId;

  return useQuery({
    queryKey: queries.reservations.user(filter, params).queryKey,
    enabled: user.isSuccess && !user.isStale && !!userId,
    queryFn: () =>
      ReservationService.getUserReservations(userId!, filter, params),
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
