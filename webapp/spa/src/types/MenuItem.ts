
export interface MenuItem {
  id?: string;
  name: string;
  description: string;
  price: string;
}

export interface PaginatedMenuItems {
  menuItems: MenuItem[];
  maxPages: number;
}