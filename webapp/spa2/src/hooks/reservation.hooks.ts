import { useMutation, useQueryClient } from "react-query";
import { ServerError, isServerError } from "../api/client";
import { ReservationService } from "../api/services/ReservationService";
import { PageParams } from "../types/page";

interface QueryOptions {
  onSuccess?: () => void;
  onError?: (error: ServerError) => void;
}

const reservationKeys = {
  all: ["reservations"] as const,
  lists: () => [...reservationKeys.all, "list"] as const,
  list: (restaurantId: number) =>
    [...reservationKeys.lists(), restaurantId] as const,
  userList: (filterType: string, filters?: PageParams) =>
    [...reservationKeys.lists(), filterType, { filters }] as const,
};

export function useCreateReservation(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ReservationService.create,
    onSuccess: () => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(reservationKeys.lists());
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
