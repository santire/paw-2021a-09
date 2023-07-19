import { z } from "zod";

const MenuItemSchemaBase = z.object({
  price: z.number().gte(1).lte(100000),
  name: z.string().min(1).max(100),
  description: z.string().min(1).max(100),
});

const HasID = z.object({
  id: z.number().int().gt(0),
});

export const MenuItemSchema = MenuItemSchemaBase.merge(HasID);
export const MenuItemRegisterSchema = MenuItemSchemaBase;
