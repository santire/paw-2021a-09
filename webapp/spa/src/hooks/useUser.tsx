import { useQuery } from "react-query";
import {Restaurant, User} from "../types";
import {getUserById} from "../api/services/UserService";

export function useUser(userId: string) {
    return useQuery<User, Error>(["user", userId], async () =>
        getUserById(userId)
    );
}
