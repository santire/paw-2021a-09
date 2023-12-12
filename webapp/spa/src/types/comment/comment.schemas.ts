import { z } from "zod";

const CommentSchemaBase = z.object({
  message: z.string().min(1).max(144),
});

const HasID = z.object({
  id: z.number().int().gt(0),
  username: z.string(),
  date: z.string(),
});

const HasURLs = z.object({
  self: z.string(),
  user: z.string().url(),
  restaurant: z.string().url(),
});

export const CommentSchema = CommentSchemaBase.merge(HasID).merge(HasURLs);
export const CommentRegisterSchema = CommentSchemaBase;
