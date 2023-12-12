import { PageParams } from "@/types/page";
import { createQueryKeys } from "@lukemorales/query-key-factory";

export const reservations = createQueryKeys("reservations", {
  detail: (url: string) => ({
    queryKey: [url],
  }),
  user: (str: "current" | "history", params: PageParams) => ({
    queryKey: [str, { ...params }],
  }),
  restaurant: (
    restaurantId: number,
    str: "current | history",
    params: PageParams,
  ) => ({
    queryKey: [restaurantId, str, { ...params }],
  }),
});
