import { ReservationService } from "@/api/services/ReservationService";
import { queries } from "@/queries";
import { IReservationRegister } from "@/types/reservation/reservation.models";
import { useMutation, useQueryClient } from "@tanstack/react-query";

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
      queryClient.invalidateQueries(queries.reservations.user("current"));
    },
  });
}
