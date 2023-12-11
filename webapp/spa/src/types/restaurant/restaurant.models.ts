import { z } from "zod";
import {
  RestaurantRegisterSchema,
  RestaurantSchema,
  RestaurantUdpdateSchema,
} from "./restaurant.schemas";

export type IRestaurant = z.infer<typeof RestaurantSchema>;
export type IRestaurantRegister = z.infer<typeof RestaurantRegisterSchema>;
export type IRestaurantUpdate = z.infer<typeof RestaurantUdpdateSchema>;
