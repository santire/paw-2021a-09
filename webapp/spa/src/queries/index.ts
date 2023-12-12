import { mergeQueryKeys } from "@lukemorales/query-key-factory";
import { users } from "./user";
import { restaurants } from "./restaurant";
import { likes } from "./like";
import { ratings } from "./rating";
import { comments } from "./comments";

export const queries = mergeQueryKeys(users, restaurants, likes, ratings, comments);
