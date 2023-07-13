import { useQuery } from "react-query";
import { RestaurantService } from "../api/services/RestaurantService";
import { Page, PageParams } from "../types/page";
import { useAuth } from "./useAuth";
import { IRestaurant } from "../types/restaurant/restaurant.models";
import { UserService } from "../api/services/UserService";

const restaurantKeys = {
  all: ["restaurants"] as const,
  lists: () => [...restaurantKeys.all, "list"] as const,
  list: (filters: string) => [...restaurantKeys.lists(), { filters }] as const,
  details: () => [...restaurantKeys.all, "detail"] as const,
  detail: (id: number) => [...restaurantKeys.details(), id] as const,
};

export function useGetRestaurants(pageParams: PageParams) {
  const { isAuthenticated, userId } = useAuth();
  return useQuery<Page<IRestaurant[]>>({
    queryKey: restaurantKeys.lists(),
    queryFn: async () => {
      const { data, meta } = await RestaurantService.getAll(pageParams);
      // If user is authenticated, stiches likedBy status
      if (isAuthenticated) {
        const ids = data.map((r) => r.id);
        const likes = await UserService.getLikesByRestaurants(userId, ids);
        const newData = data.map((r) => ({
          ...r,
          likedByUser:
            likes.find((l) => l.restaurantId === r.id)?.liked || false,
        }));
        return { data: newData, meta };
      }

      const newData = data.map((r) => ({ ...r, likedByUser: false }));
      return { data: newData, meta };
    },
  });
}
