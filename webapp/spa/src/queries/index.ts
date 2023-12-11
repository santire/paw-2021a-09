import { mergeQueryKeys } from "@lukemorales/query-key-factory";
import { users } from "./user";
import { restaurants } from "./restaurant";
import { likes } from "./like";

export const queries = mergeQueryKeys(users, restaurants, likes);
