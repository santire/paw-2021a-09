import { z } from "zod";
import { MenuItemRegisterSchema, MenuItemSchema } from "./menuItem.schemas";
export type IMenuItem = z.infer<typeof MenuItemSchema>;
export type IMenuItemRegister = z.infer<typeof MenuItemRegisterSchema>;
