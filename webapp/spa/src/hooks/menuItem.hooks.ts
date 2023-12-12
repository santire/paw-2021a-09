import { MenuItemService } from "@/api/services/MenuService";
import { queries } from "@/queries";
import { IMenuItemRegister } from "@/types/menuItem/menuItem.models";
import { PageParams } from "@/types/page";
import { IRestaurant } from "@/types/restaurant/restaurant.models";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

export function useGetRestaurantMenu({self, menu}: IRestaurant, params: PageParams) {
  return useQuery({
    queryKey: queries.menuItems.list(self, params).queryKey,
    queryFn: () => MenuItemService.getAll(menu, params),
  });
}

export function useCreateMenuItem() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ url, item }: { url: string; item: IMenuItemRegister }) =>
      MenuItemService.create(url, item),
    // If successful, refresh comments query for that restaurant
    onSuccess: (item) => {
      queryClient.invalidateQueries({
        queryKey: [...queries.menuItems.list._def, item.restaurant],
      });
    },
  });
}

export function useDeleteMenuItem(restaurantUrl: string) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ url }: { url: string }) => MenuItemService.remove(url),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [...queries.menuItems.list._def, restaurantUrl],
      });
    },
  });
}
