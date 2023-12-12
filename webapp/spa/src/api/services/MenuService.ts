import { IMenuItem, IMenuItemRegister } from "@/types/menuItem/menuItem.models";
import { Page, PageParams } from "@/types/page";
import { apiClient } from "../client";
import { pagedResponse } from "../utils";

interface IMenuItemService {
  getAll(url: string, params: PageParams): Promise<Page<IMenuItem[]>>;
  create(url: string, params: IMenuItemRegister): Promise<IMenuItem>;
  remove(url: string): Promise<void>;
}

module MenuItemServiceImpl {
  export async function getAll(url: string, params: PageParams) {
    const response = await apiClient.get<IMenuItem[]>(url, { params: params });
    return pagedResponse(response);
  }

  export async function create(url: string, params: IMenuItemRegister) {
    const response = await apiClient.post<IMenuItem>(url, params);
    return response.data;
  }

  export async function remove(url: string) {
    const response = await apiClient.delete(url);
    return response.data;
  }
}

const MenuItemService: IMenuItemService = MenuItemServiceImpl;
export { MenuItemService };
