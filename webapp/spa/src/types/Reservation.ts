import { Restaurant } from "./Restaurant";

export interface Reservation{
  restaurant: Restaurant;
  quantity: number;
  id?: string;
  date?: string;
  confirmed?: boolean;
}
