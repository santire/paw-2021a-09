import { z } from "zod";

const UserSchemaBase = z.object({
  email: z.string().email().min(2).max(100),
  firstName: z.string().min(1).max(100),
  lastName: z.string().min(1).max(100),
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
  reservations: z.string().url(),
  restaurants: z.string().url(),
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
  }
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
  }
);