import { Restaurant } from "./Restaurant";
import { User } from "./User";

export interface Reservation{
  restaurant: Restaurant;
  quantity: number;
  id?: string;
  date?: string;
  confirmed?: boolean;
  username?: string
}
