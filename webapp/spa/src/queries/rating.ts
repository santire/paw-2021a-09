
import { createQueryKeys } from "@lukemorales/query-key-factory";
export const ratings = createQueryKeys("ratings", {
  detail: (restaurant: string) => ({
    queryKey: [restaurant],
  }),
});
