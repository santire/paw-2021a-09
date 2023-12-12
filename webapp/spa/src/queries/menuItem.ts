import { MenuItemService } from "@/api/services/MenuService";
import { PageParams } from "@/types/page";
import { createQueryKeys } from "@lukemorales/query-key-factory";

export const menuItems = createQueryKeys("menuItems", {
  all: null,
  list: (url: string, params: PageParams) => ({
    queryKey: [url, { params }],
    queryFn: () => MenuItemService.getAll(url, params),
  }),
});
