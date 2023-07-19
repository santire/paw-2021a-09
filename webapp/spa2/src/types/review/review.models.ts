import { z } from "zod";
import { ReviewRegisterSchema, ReviewSchema } from "./review.schemas";
export type IReview = z.infer<typeof ReviewSchema>;
export type IReviewRegister = z.infer<typeof ReviewRegisterSchema>;
