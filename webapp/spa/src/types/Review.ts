
export interface Review {
    id?: string;
    username: string;
    userComment: string;
    date: string;
  }
  
  export interface PaginatedMenuItems {
    menuItems: Review[];
    maxPages: number;
  }