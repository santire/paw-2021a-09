import { RestaurantService } from "@/api/services/RestaurantService";
import { RestaurantFilterParams } from "@/types/filters";
import { PageParams } from "@/types/page";
import { createQueryKeys } from "@lukemorales/query-key-factory";
export const restaurants = createQueryKeys("restaurants", {
  all: null,
  list: (filters: RestaurantFilterParams & PageParams) => ({
    queryKey: [{ filters }],
    queryFn: () => RestaurantService.getFilteredBy(filters),
  }),
  detail: (id: number) => ({
    queryKey: [id],
    queryFn: () => RestaurantService.get(id),
  }),
});
