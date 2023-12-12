import {
  IReservation,
  IReservationRegister,
} from "@/types/reservation/reservation.models";
import { apiClient } from "../client";
import { Page, PageParams } from "@/types/page";
import { pagedResponse } from "../utils";
const PATH = "/reservations";

interface IReservationService {
  create(
    userId: number,
    restaurantId: number,
    params: IReservationRegister,
  ): Promise<IReservation>;

  // confirm(params: ActionReservationParams): Promise<void>;
  // deny(params: DenyReservationParams): Promise<void>;
  cancel(url: string): Promise<void>;

  getUserReservations(
    userId: number,
    filterType: string,
    params: PageParams,
  ): Promise<Page<IReservation[]>>;

  getRestaurantReservations(
    restaurantId: number,
    filterType: string,
    params: PageParams,
  ): Promise<Page<IReservation[]>>;
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

  export async function getUserReservations(
    userId: number,
    filterType: string,
    params: PageParams,
  ) {
    const response = await apiClient.get<IReservation[]>(PATH, {
      params: {
        madeBy: userId,
        type: filterType,
        ...params,
      },
    });
    return pagedResponse(response);
  }

  export async function getRestaurantReservations(
    restaurantId: number,
    filterType: string,
    params: PageParams,
  ) {
    const response = await apiClient.get<IReservation[]>(PATH, {
      params: {
        madeTo: restaurantId,
        type: filterType.toLowerCase(),
        ...params,
      },
    });
    return pagedResponse(response);
  }

  export async function cancel(url: string) {
    const response = await apiClient.delete(url);
    return response.data;
  }
}
const ReservationService: IReservationService = ReservationServiceImpl;
export { ReservationService };
