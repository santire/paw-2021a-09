import { z } from "zod";
import {
  CancelMessageSchema,
  ReservationRegisterSchema,
  ReservationSchema,
} from "./reservation.schemas";

export type IReservation = z.infer<typeof ReservationSchema>;
export type IReservationRegister = z.infer<typeof ReservationRegisterSchema>;
export type IReservationCancelMessage = z.infer<typeof CancelMessageSchema>;
