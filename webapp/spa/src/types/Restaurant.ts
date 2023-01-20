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

  menu?: string;
  reviews?: string;
  image?: string;
  owner?: string;
}

export interface PaginatedRestaurants {
  restaurants: Restaurant[];
  maxPages: number;
}

export const getTags = async () => {
  try {
    const data = await getRestaurantTags()
    return data;
  } catch (e) {
    //console.error(e);
    return [];
  }
}