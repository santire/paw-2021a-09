import { useQuery } from "react-query";
import {
  getRestaurantById,
} from "../api/services";
import { Restaurant } from "../types";

export function useRestaurant(restaurantId: string) {
  return useQuery<Restaurant, Error>(["restaurant", restaurantId], async () =>
    getRestaurantById(restaurantId)
  );
}
