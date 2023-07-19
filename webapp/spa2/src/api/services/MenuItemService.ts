import {
  IMenuItem,
  IMenuItemRegister,
} from "../../types/menuItem/menuItem.models";
import { MAX_PAGE_AMOUNT, Page, PageParams } from "../../types/page";
import { apiClient, apiErrorHandler } from "../client";
import { pagedResponse } from "../utils";

const PATH = (id: number) => `/restaurants/${id}/menu`;

interface DeleteMenuItemParams {
  restaurantId: number;
  menuItemId: number;
}

interface CreateMenuItemParams {
  restaurantId: number;
  menuItem: IMenuItemRegister;
}
interface IMenuItemService {
  create(params: CreateMenuItemParams): Promise<IMenuItem>;
  deleteMenuItem(params: DeleteMenuItemParams): Promise<void>;
  getAll(restaurantId: number, params: PageParams): Promise<Page<IMenuItem[]>>;
}

module MenuItemServiceImpl {
  export async function getAll(restaurantId: number, params: PageParams) {
    if ((params?.pageAmount ?? 0) > MAX_PAGE_AMOUNT) {
      params.pageAmount = MAX_PAGE_AMOUNT;
    }
    try {
      const response = await apiClient.get<IMenuItem[]>(PATH(restaurantId), {
        params: params,
      });
      return pagedResponse(response);
    } catch (error) {
      throw new Error("General Error", {
        cause: apiErrorHandler(error),
      });
    }
  }
  export async function deleteMenuItem({
    restaurantId,
    menuItemId,
  }: DeleteMenuItemParams) {
    try {
      const response = await apiClient.delete(
        PATH(restaurantId) + `/${menuItemId}`
      );

      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
        }),
      });
    }
  }

  export async function create({
    restaurantId,
    menuItem,
  }: CreateMenuItemParams) {
    try {
      const response = await apiClient.post<IMenuItem>(
        PATH(restaurantId),
        menuItem
      );
      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
        }),
      });
    }
  }
}

const MenuItemService: IMenuItemService = MenuItemServiceImpl;
export { MenuItemService };
