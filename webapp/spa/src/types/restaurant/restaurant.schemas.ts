import { z } from "zod";

const facebookRegex = /^(https?:\/\/)?facebook\.com\/.*$/;
const instagramRegex = /^(https?:\/\/)?instagram\.com\/.*$/;
const twitterRegex = /^(https?:\/\/)?twitter\.com\/.*$/;

const RestaurantSchemaBase = z.object({
  name: z.string().min(1).max(100),
  address: z.string().min(6).max(100),
  phoneNumber: z
    .string()
    .regex(/[0-9]+/, "errors.invalidPhone")
    .min(8)
    .max(30),
  facebook: z.string().max(100).optional(),
  instagram: z.string().max(100).optional(),
  twitter: z.string().max(100).optional(),
  tags: z.string().array().min(1),
});

const HasID = z.object({
  id: z.number().int().gt(0),
});

const HasImageObj = z.object({
  image: z.any(),
});

const HasURLs = z.object({
  self: z.string().url(),
  image: z.string().url(),
  comments: z.string().url(),
  menu: z.string().url(),
  owner: z.string().url(),
});

const HasComputedProps = z.object({
  reservationsCount: z.number().gte(0),
  likes: z.number().gte(0),
  rating: z.number().gte(0).lte(5),
});

export const RestaurantSchema = RestaurantSchemaBase.merge(HasID)
  .merge(HasComputedProps)
  .merge(HasURLs);

export const RestaurantRegisterSchema = RestaurantSchemaBase.merge(HasImageObj)
  .refine(({ facebook }) => !facebook || facebookRegex.test(facebook), {
    message: "errors.facebookRegex",
    path: ["facebook"],
  })
  .refine(({ twitter }) => !twitter || twitterRegex.test(twitter), {
    message: "errors.twitterRegex",
    path: ["twitter"],
  })
  .refine(({ instagram }) => !instagram || instagramRegex.test(instagram), {
    message: "errors.instagramRegex",
    path: ["instagram"],
  });

export const RestaurantUdpdateSchema = RestaurantSchemaBase.merge(HasImageObj)
  .partial()
  .refine(({ facebook }) => !facebook || facebookRegex.test(facebook), {
    message: "errors.facebookRegex",
    path: ["facebook"],
  })
  .refine(({ twitter }) => !twitter || twitterRegex.test(twitter), {
    message: "errors.twitterRegex",
    path: ["twitter"],
  })
  .refine(({ instagram }) => !instagram || instagramRegex.test(instagram), {
    message: "errors.instagramRegex",
    path: ["instagram"],
  });
