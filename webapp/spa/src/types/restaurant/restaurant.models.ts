import { z } from "zod";
import {
  RestaurantRegisterSchema,
  RestaurantResponseSchema,
  RestaurantSchema,
  RestaurantUdpdateSchema,
} from "./restaurant.schemas";

export type IRestaurant = z.infer<typeof RestaurantSchema>;
export type IRestaurantResponse = z.infer<typeof RestaurantResponseSchema>;
export type IRestaurantRegister = z.infer<typeof RestaurantRegisterSchema>;
export type IRestaurantUpdate = z.infer<typeof RestaurantUdpdateSchema>;
