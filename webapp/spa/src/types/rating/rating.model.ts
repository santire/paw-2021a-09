import { z } from "zod";
import { RatingSchema } from "./rating.schemas";
export type IRating = z.infer<typeof RatingSchema>;
export interface IClientRating {
  rated: boolean;
  rating?: IRating;
}
