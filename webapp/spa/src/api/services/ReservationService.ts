import { Reservation, Restaurant } from "../../types";
import { Page } from "../../types/Page";
import { apiClient } from "../client";
import { FilterParams } from "./RestaurantService";
import { NO_FILTER } from "./UserService";

export interface ReservationForm {
  date: string;
  time: string;
  quantity: number;
}

export async function makeReservation(
  restaurantId: string,
  reservationDto: ReservationForm
) {
  const url = `restaurants/${restaurantId}/reservations`;
  const response = await apiClient().post<any>(url, reservationDto);
  return response;
}

export async function confirmReservation(
  restaurantId: string,
  reservationId: string
) {
  const url = `restaurants/${restaurantId}/reservations/${reservationId}`;
  const response = await apiClient().put(
    url,
    { message: "1" },
    { params: { action: "confirm" } }
  );
  return response.status;
}

export async function cancelReservation(
  restaurantId: string,
  reservationId: string
) {
  const url = `restaurants/${restaurantId}/reservations/${reservationId}`;
  const response = await apiClient().delete(url);
  return response.status;
}

export async function denyReservation(
  restaurantId: string,
  reservationId: string,
  message: string
) {
  const url = `restaurants/${restaurantId}/reservations/${reservationId}`;
  const response = await apiClient().put<any>(
    url,
    { message: message },
    { params: { action: "deny" } }
  );
  return response.status;
}

interface ReservationDto {
  restaurant: string;
  quantity: number;
  reservationId: string;
  date?: string;
  confirmed?: boolean;
}

export async function getUserReservations({
  userId,
  params = NO_FILTER,
}: {
  userId: string;
  params: FilterParams;
}) {
  const url = `users/${userId}/reservations`;
  const response = await apiClient().get<ReservationDto[]>(url, { params });
  const data = [];
  for (let reservation of response.data) {
    const resp = await apiClient().get<Restaurant>(reservation.restaurant);
    let reserv: Reservation = {
      id: reservation.reservationId,
      restaurant: resp.data,
      quantity: reservation.quantity,
      date: reservation.date,
      confirmed: reservation.confirmed,
    };

    data.push(reserv);
  }

  const links = {
    first: 0,
    last: 0,
    next: 0,
    prev: 0,
  };

  response.headers.link?.split(",").forEach((str) => {
    const linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/gi.exec(str);
    if (linkInfo != null) {
      const pageInfo = /page=([^&]*)/gi.exec(linkInfo[1]);
      if (pageInfo != null) {
        // Redudant switch because typesript
        // if (["first", "last", "next", "prev"].includes(linkInfo[2]))
        // links[linkInfo[2]] = parseInt(pageInfo[1]);
        switch (linkInfo[2]) {
          case "first": {
            links["first"] = parseInt(pageInfo[1]);
            break;
          }
          case "last": {
            links["last"] = parseInt(pageInfo[1]);
            break;
          }
          case "next": {
            links["next"] = parseInt(pageInfo[1]);
            break;
          }
          case "prev": {
            links["prev"] = parseInt(pageInfo[1]);
            break;
          }
        }
      }
    }
  });

  const page: Page<Reservation> = {
    data: data,
    meta: {
      perPage: response.data.length,
      maxPages: links?.hasOwnProperty("last") ? links.last : 0,
    },
  };
  return page;
}
