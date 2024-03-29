import { z } from "zod";

// const REG = /^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+/;
const REG = /^[\p{L}]+$/u;
const isValidName = (s: string) => REG.test(s);

const UserSchemaBase = z.object({
  email: z.string().email().min(2).max(100),
  firstName: z.string().min(1).max(100).refine(isValidName, {
    message: "errors.invalidName",
  }),
  lastName: z.string().min(1).max(100).refine(isValidName, {
    message: "errors.invalidName",
  }),

  phone: z
    .string()
    .regex(/[0-9]+/, "errors.invalidPhone")
    .min(8)
    .max(30),
  username: z
    .string()
    .min(3)
    .max(30)
    .regex(/[a-zA-Z0-9]+/),
});

const HasID = z.object({
  userId: z.number().int().gt(0),
});
const HasURLs = z.object({
  self: z.string().url(),
  reservations: z.string().url(),
  restaurants: z.string().url(),
  comments: z.string().url(),
  ratings: z.string().url(),
  likes: z.string().url(),
});

const HasPasswords = z.object({
  password: z.string().min(8).max(100),
  repeatPassword: z.string().min(8).max(100),
});

// TODO: refactor to remove repetition

export const UserRegisterSchema = UserSchemaBase.merge(HasPasswords).refine(
  (data) => data.password === data.repeatPassword,
  {
    message: "errors.repeatPassword",
    path: ["repeatPassword"],
  },
);

export const UserUpdateSchema = UserSchemaBase.merge(HasPasswords)
  .partial()
  .refine((data) => data.password === data.repeatPassword, {
    message: "errors.repeatPassword",
    path: ["repeatPassword"],
  });

export const UserSchema = UserSchemaBase.merge(HasID).merge(HasURLs);

export const UserLoginSchema = UserSchemaBase.pick({
  email: true,
}).extend({ password: z.string().min(8).max(100) });

export const UserForgotSchema = UserSchemaBase.pick({
  email: true,
});

export const UserResetPasswordSchema = HasPasswords.refine(
  (data) => data.password === data.repeatPassword,
  {
    message: "errors.repeatPassword",
    path: ["repeatPassword"],
  },
);
