import { z } from "zod";
import { LikeSchema } from "./like.schema";
export type ILike = z.infer<typeof LikeSchema>;
export interface IClientLike {
  liked: boolean;
  like?: ILike;
}
