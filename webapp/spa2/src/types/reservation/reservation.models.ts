import { z } from "zod";
import {
  ReservationRegisterSchema,
  ReservationSchema,
} from "./reservation.schemas";

export type IReservation = z.infer<typeof ReservationSchema>;
export type IReservationRegister = z.infer<typeof ReservationRegisterSchema>;
