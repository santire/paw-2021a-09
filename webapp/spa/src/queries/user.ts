import { UserService } from "@/api/services/UserService";
import { getUserEmail } from "@/utils/AuthStorage";
import { createQueryKeys } from "@lukemorales/query-key-factory";
export const users = createQueryKeys("users", {
  all: null,
  detail: () => ({
    queryKey: ["email"],
    // queryFn: () => UserService.getByEmail(getUserEmail() ?? ""),
  }),
});
