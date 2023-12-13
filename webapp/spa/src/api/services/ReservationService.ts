import {
  IReservation,
  IReservationCancelMessage,
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

  cancel(url: string): Promise<void>;
  confirm(url: string): Promise<IReservation>;
  deny(url: string, message: IReservationCancelMessage): Promise<IReservation>;

  getUserReservations(
    userId: number,
    filterType: string,
    status: string,
    params: PageParams,
  ): Promise<Page<IReservation[]>>;

  getRestaurantReservations(
    restaurantId: number,
    filterType: string,
    status: string,
    params: PageParams,
  ): Promise<Page<IReservation[]>>;
}

const formatDate = (currentDate: Date) => {
  // Get components
  const day = currentDate.getDate().toString().padStart(2, "0");
  const month = (currentDate.getMonth() + 1).toString().padStart(2, "0"); // Note: Month is zero-based
  const year = currentDate.getFullYear();
  const hours = currentDate.getHours().toString().padStart(2, "0");
  const minutes = currentDate.getMinutes().toString().padStart(2, "0");
  const seconds = currentDate.getSeconds().toString().padStart(2, "0");

  // Create the formatted string
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
};

module ReservationServiceImpl {
  export async function create(
    userId: number,
    restaurantId: number,
    params: IReservationRegister,
  ) {
    const response = await apiClient.post<IReservation>(PATH, {
      userId,
      restaurantId,
      date: formatDate(params.date),
      quantity: params.quantity,
    });

    return response.data;
  }

  export async function getUserReservations(
    userId: number,
    filterType: string,
    status: string,
    params: PageParams,
  ) {
    const response = await apiClient.get<IReservation[]>(PATH, {
      params: {
        madeBy: userId,
        type: filterType,
        status: status !== "any" ? status.toLowerCase() : undefined,
        ...params,
      },
    });
    return pagedResponse(response);
  }

  export async function getRestaurantReservations(
    restaurantId: number,
    filterType: string,
    status: string,
    params: PageParams,
  ) {
    const response = await apiClient.get<IReservation[]>(PATH, {
      params: {
        madeTo: restaurantId,
        type: filterType.toLowerCase(),
        status: status !== "any" ? status.toLowerCase() : undefined,
        ...params,
      },
    });
    return pagedResponse(response);
  }

  export async function cancel(url: string) {
    const response = await apiClient.delete(url);
    return response.data;
  }
  export async function confirm(url: string) {
    const response = await apiClient.patch<IReservation>(url, {
      status: "confirmed",
    });
    return response.data;
  }
  export async function deny(
    url: string,
    { message }: IReservationCancelMessage,
  ) {
    const response = await apiClient.patch(url, {
      status: "denied",
      message: message,
    });
    return response.data;
  }
}
const ReservationService: IReservationService = ReservationServiceImpl;
export { ReservationService };
