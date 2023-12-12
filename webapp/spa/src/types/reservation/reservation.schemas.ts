import { z } from "zod";

const ReservationSchemaBase = z.object({
  date: z.date(),
  quantity: z.number().gte(1).lte(15),
});

const HasID = z.object({
  id: z.number().int().gt(0),
});

const HasURLs = z.object({
  self: z.string().url(),
  user: z.string().url(),
  restaurant: z.string().url(),
});

export const ReservationRegisterSchema = ReservationSchemaBase;
export const ReservationSchema = ReservationSchemaBase.merge(HasID)
  .merge(HasURLs)
  .merge(z.object({ status: z.enum(["PENDING", "CONFIRMED", "DENIED"]) }));
