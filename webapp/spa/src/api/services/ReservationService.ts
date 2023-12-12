import {
  IReservation,
  IReservationRegister,
} from "@/types/reservation/reservation.models";
import { apiClient } from "../client";
const PATH = "/reservations";

interface IReservationService {
  create(
    userId: number,
    restaurantId: number,
    params: IReservationRegister,
  ): Promise<IReservation>;

  // confirm(params: ActionReservationParams): Promise<void>;
  // deny(params: DenyReservationParams): Promise<void>;
  // cancel(params: ActionReservationParams): Promise<void>;

  // getUserReservations(
  //   userId: number,
  //   filterType: string,
  //   params: PageParams,
  // ): Promise<Page<IReservation[]>>;

  // getRestaurantReservations(
  //   restaurantId: number,
  //   filterType: string,
  //   params: PageParams,
  // ): Promise<Page<IReservation[]>>;
}

module ReservationServiceImpl {
  export async function create(
    userId: number,
    restaurantId: number,
    params: IReservationRegister,
  ) {
    const formattedDate = params.date.toISOString().slice(0, 19);
    const response = await apiClient.post<IReservation>(PATH, {
      userId,
      restaurantId,
      date: formattedDate,
      quantity: params.quantity,
    });

    return response.data;
  }
}
const ReservationService: IReservationService = ReservationServiceImpl;
export { ReservationService };
