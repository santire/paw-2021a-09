import { PageParams } from "@/types/page";
import { createQueryKeys } from "@lukemorales/query-key-factory";

export const reservations = createQueryKeys("reservations", {
  detail: (url: string) => ({
    queryKey: [url],
  }),
  user: (
    str: "current" | "history",
    status: "pending" | "confirmed" | "denied" | "any",
    params: PageParams,
  ) => ({
    queryKey: [str, status, { ...params }],
  }),
  restaurant: (
    restaurantId: number,
    str: "current" | "history",
    status: "pending" | "confirmed" | "denied" | "any",
    params: PageParams,
  ) => ({
    queryKey: [restaurantId, str, status, { ...params }],
  }),
});
