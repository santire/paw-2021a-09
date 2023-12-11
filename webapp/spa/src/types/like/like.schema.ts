import { z } from "zod";

export const LikeSchema = z.object({
  self: z.string().url(),
  user: z.string().url(),
  restaurant: z.string().url(),
});

