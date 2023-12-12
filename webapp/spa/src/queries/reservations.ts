import { createQueryKeys } from "@lukemorales/query-key-factory";

export const reservations = createQueryKeys("reservations", {
  detail: (url: string) => ({
    queryKey: [url],
  }),
  user: (str: "current" | "history") => ({
    queryKey: ["user", str],
  }),
  restaurant: (restaurantId: number, str: "current | history") => ({
    queryKey: [restaurantId, str],
  }),
});
