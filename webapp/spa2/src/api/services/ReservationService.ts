import { MAX_PAGE_AMOUNT, Page, PageParams } from "../../types/page";
import {
  IReservation,
  IReservationRegister,
} from "../../types/reservation/reservation.models";
import { apiClient, apiErrorHandler } from "../client";
import { pagedResponse } from "../utils";

const PATH = (restaurantId: number) =>
  `/restaurants/${restaurantId}/reservations`;

interface CreateReservationParams {
  restaurantId: number;
  reservation: IReservationRegister;
}

interface ActionReservationParams {
  restaurantId: number;
  reservationId: number;
}

interface DenyReservationParams extends ActionReservationParams {
  message: string;
}

interface IReservationService {
  create(params: CreateReservationParams): Promise<IReservation>;
  confirm(params: ActionReservationParams): Promise<void>;
  deny(params: DenyReservationParams): Promise<void>;
  cancel(params: ActionReservationParams): Promise<void>;

  getUserReservations(
    userId: number,
    filterType: string,
    params: PageParams
  ): Promise<Page<IReservation[]>>;

  getRestaurantReservations(
    restaurantId: number,
    filterType: string,
    params: PageParams
  ): Promise<Page<IReservation[]>>;
}

module ReservationServiceImpl {
  export async function create({
    restaurantId,
    reservation,
  }: CreateReservationParams) {
    try {
      const response = await apiClient.post<IReservation>(
        PATH(restaurantId),
        reservation
      );
      return response.data;
    } catch (error) {
      throw new Error("Response Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          ConstraintViolationException: { code: "validation_error" },
        }),
      });
    }
  }
  export async function confirm({
    restaurantId,
    reservationId,
  }: ActionReservationParams) {
    try {
      const response = await apiClient.put(
        `${PATH(restaurantId)}/${reservationId}`,
        { message: "ok" },
        { params: { action: "confirm" } }
      );
      return response.data;
    } catch (error) {
      throw new Error("Response Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          ConstraintViolationException: { code: "validation_error" },
        }),
      });
    }
  }
  export async function deny({
    restaurantId,
    reservationId,
    message,
  }: DenyReservationParams) {
    try {
      const response = await apiClient.put(
        `${PATH(restaurantId)}/${reservationId}`,
        { message },
        { params: { action: "deny" } }
      );
      return response.data;
    } catch (error) {
      throw new Error("Response Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          ConstraintViolationException: { code: "validation_error" },
        }),
      });
    }
  }
  export async function cancel({
    restaurantId,
    reservationId,
  }: ActionReservationParams) {
    try {
      const response = await apiClient.delete(
        `${PATH(restaurantId)}/${reservationId}`
      );
      return response.data;
    } catch (error) {
      throw new Error("Response Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          ConstraintViolationException: { code: "validation_error" },
        }),
      });
    }
  }

  export async function getUserReservations(
    userId: number,
    filterType: string,
    params: PageParams
  ) {
    if ((params?.pageAmount ?? 0) > MAX_PAGE_AMOUNT) {
      params.pageAmount = MAX_PAGE_AMOUNT;
    }
    try {
      const response = await apiClient.get<IReservation[]>(
        `/users/${userId}/reservations`,
        {
          params: {
            filterBy: filterType,
            page: params.page,
            pageAmount: params.pageAmount,
          },
        }
      );
      return pagedResponse(response);
    } catch (error) {
      throw new Error("Get Reservations Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
        }),
      });
    }
  }

  export async function getRestaurantReservations(
    restaurantId: number,
    filterType: string,
    params: PageParams
  ) {
    if ((params?.pageAmount ?? 0) > MAX_PAGE_AMOUNT) {
      params.pageAmount = MAX_PAGE_AMOUNT;
    }
    try {
      const response = await apiClient.get<IReservation[]>(
        `/restaurants/${restaurantId}/reservations`,
        {
          params: {
            filterBy: filterType,
            page: params.page,
            pageAmount: params.pageAmount,
          },
        }
      );
      return pagedResponse(response);
    } catch (error) {
      throw new Error("Get Reservations Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
        }),
      });
    }
  }
}
const ReservationService: IReservationService = ReservationServiceImpl;
export { ReservationService };
