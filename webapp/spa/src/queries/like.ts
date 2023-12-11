import { createQueryKeys } from "@lukemorales/query-key-factory";
export const likes = createQueryKeys("likes", {
  detail: (restaurant: string) => ({
    queryKey: [restaurant],
  }),
});
