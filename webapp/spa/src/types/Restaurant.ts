import { getRestaurantTags } from "../api/services";

export interface Restaurant {
  id?: string;
  name: string;
  address: string;
  phoneNumber: string;
  likes?: number;
  rating?: number;
  facebook?: string;
  instagram?: string;
  twitter?: string;
  tags: string[];
  reservationsCount?: number;

  menu?: string;
  reviews?: string;
  image?: any;
  owner?: string;
}

export interface PaginatedRestaurants {
  restaurants: Restaurant[];
  maxPages: number;
}

export const getTags = async () => {
  try {
    const data = await getRestaurantTags();
    return data.map(t => t.toLowerCase());
  } catch (e) {
    //console.error(e);
    return [];
  }
};
