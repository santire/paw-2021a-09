import { z } from "zod";
import { CommentRegisterSchema, CommentSchema } from "./comment.schemas";
export type IComment = z.infer<typeof CommentSchema>;
export type ICommentRegister = z.infer<typeof CommentRegisterSchema>;
