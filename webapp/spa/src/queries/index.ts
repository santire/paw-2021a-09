import { mergeQueryKeys } from "@lukemorales/query-key-factory";
import { users } from "./user";

export const queries = mergeQueryKeys(users);
