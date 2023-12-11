import { z } from "zod";

export const RatingSchema = z.object({
  rating: z.number(),
  self: z.string().url(),
  user: z.string().url(),
  restaurant: z.string().url(),
});
