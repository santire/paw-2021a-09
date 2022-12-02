import { apiClient } from "../client";
import { Restaurant } from "../../types";

const BASE_PATH = "restaurants";
export async function getRestaurants() {
  const url = `${BASE_PATH}/`;
  const response = await apiClient.get<Restaurant[]>(url);
  return response.data;
}
