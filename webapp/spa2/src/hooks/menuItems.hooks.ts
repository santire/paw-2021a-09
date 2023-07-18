import { useSearchParams } from "react-router-dom";
import { ServerError, isServerError } from "../api/client";
import { Page, PageParams } from "../types/page";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { IMenuItem } from "../types/menuItem/menuItem.models";
import { MenuItemService } from "../api/services/MenuItemService";
import { useAuth } from "./useAuth";

interface QueryOptions {
  onSuccess?: () => void;
  onError?: (error: ServerError) => void;
}

const menuKeys = {
  all: ["menuItems"] as const,
  lists: (restaurantId: number) =>
    [...menuKeys.all, "list", restaurantId] as const,
  list: (restaurantId: number, filters?: PageParams) =>
    [...menuKeys.lists(restaurantId), { filters }] as const,
};

export function useGetMenuItems(restaurantId: number, params?: PageParams) {
  const [searchParams, setSearchParams] = useSearchParams();
  return useQuery<Page<IMenuItem[]>>({
    queryKey: menuKeys.list(restaurantId, params),
    enabled: !!params,
    queryFn: () => MenuItemService.getAll(restaurantId, params!),
    onSuccess: (data) => {
      const page = parseInt(searchParams.get("page") || "NaN");
      // If page is invalid resets back to first
      if (page > data.meta.maxPages) {
        searchParams.delete("page");
        setSearchParams(searchParams);
      }
    },
  });
}

export function useCreateMenuItem(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: MenuItemService.create,
    onSuccess: (_, { restaurantId }) => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(menuKeys.lists(restaurantId));
      if (options?.onSuccess) {
        options.onSuccess();
      }
    },
    onError: ({ cause }) => {
      if (isServerError(cause) && options?.onError) {
        options.onError(cause);
      } else {
        console.log("An error here shouldn't be happening");
      }
    },
  });
}

export function useDeleteMenuItem(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: MenuItemService.deleteMenuItem,
    onSuccess: (_, { restaurantId }) => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(menuKeys.lists(restaurantId));
      if (options?.onSuccess) {
        options.onSuccess();
      }
    },

    onError: ({ cause }) => {
      if (isServerError(cause) && options?.onError) {
        options.onError(cause);
      } else {
        console.log("An error here shouldn't be happening");
      }
    },
  });
}
