import { z } from "zod";

const facebookRegex = /^(https?:\/\/)?facebook\.com\/.*$/;
const instagramRegex = /^(https?:\/\/)?instagram\.com\/.*$/;
const twitterRegex = /^(https?:\/\/)?twitter\.com\/.*$/;

const RestaurantSchemaBase = z.object({
  name: z.string().min(1).max(100),
  address: z.string().min(6).max(100),
  phoneNumber: z
    .string()
    .min(8)
    .max(30)
    .regex(/[0-9]+/),
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
  image: z.string().url(),
  reviews: z.string().url(),
  menu: z.string().url(),
  owner: z.string().url(),
});

const HasComputedProps = z.object({
  likes: z.number().gte(0),
  rating: z.number().gte(0).lte(5),
});
const HasClientProps = z.object({
  likedByUser: z.boolean(),
});

export const RestaurantResponseSchema = RestaurantSchemaBase.merge(HasID)
  .merge(HasComputedProps)
  .merge(HasURLs);

export const RestaurantSchema = RestaurantSchemaBase.merge(HasID)
  .merge(HasComputedProps)
  .merge(HasURLs)
  .merge(HasClientProps);

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
