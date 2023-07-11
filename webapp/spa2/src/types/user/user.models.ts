// Models are inferred from schemas to have a single source of truth

import { z } from "zod";
import {
  UserLoginSchema,
  UserRegisterSchema,
  UserSchema,
  UserUpdateSchema,
} from "./user.schemas";

export type IUser = z.infer<typeof UserSchema>;
export type IUserRegister = z.infer<typeof UserRegisterSchema>;
export type IUserUpdate = z.infer<typeof UserUpdateSchema>;
export type IUserLogin = z.infer<typeof UserLoginSchema>;
