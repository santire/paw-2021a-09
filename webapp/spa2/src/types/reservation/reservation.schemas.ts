import { z } from "zod";

const ReservationSchemaBase = z.object({
  date: z.date(),
  quantity: z.number().gte(1).lte(15),
});

const HasID = z.object({
  reservationId: z.number().int().gt(0),
});

const HasURLs = z.object({
  user: z.string().url(),
  restaurant: z.string().url(),
});

const HasDateTimeString = z.object({
  date: z.string(),
  time: z.string(),
});

export const ReservationRegisterSchema = ReservationSchemaBase.omit({
  date: true,
}).merge(HasDateTimeString);
export const ReservationSchema = ReservationSchemaBase.merge(HasID)
  .merge(HasURLs)
  .merge(z.object({ confirmed: z.boolean() }));
