import { PageParams } from "@/types/page";
import { createQueryKeys } from "@lukemorales/query-key-factory";
export const users = createQueryKeys("users", {
  all: null,
  detail: () => ({
    queryKey: ["email"],
    // queryFn: () => UserService.getByEmail(getUserEmail() ?? ""),
  }),
  restaurants: (params: PageParams) => ({
    queryKey: [{ ...params }],
  }),
});
