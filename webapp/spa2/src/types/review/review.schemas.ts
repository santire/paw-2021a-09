import { z } from "zod";

const ReviewSchemaBase = z.object({
  userComment: z.string().min(1).max(144),
});

const HasID = z.object({
  id: z.number().int().gt(0),
  username: z.string(),
  date: z.string(),
});

const HasURLs = z.object({
  user: z.string().url(),
  restaurant: z.string().url(),
});

export const ReviewSchema = ReviewSchemaBase.merge(HasID).merge(HasURLs);
export const ReviewRegisterSchema = ReviewSchemaBase;
