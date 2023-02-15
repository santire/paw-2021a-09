import { apiClient } from "../client";
import { Restaurant } from "../../types";
import { Page } from "../../types/Page";
import { MenuItem } from "../../types/MenuItem";
import { Review } from "../../types/Review";
import { Rate } from "../../types/Rate";
import { RegisterRestaurantForm } from "../../components/RestaurantForm/RestaurantForm";

export interface FilterParams {
  page?: number;
  pageAmount?: number;
  search?: string;
  tags?: string[];
  sort?: string;
  min?: number;
  max?: number;
  order?: string;
}

const BASE_PATH = "restaurants";
export const NO_FILTER: FilterParams = {
  page: 1,
  pageAmount: 10,
};

export async function registerRestaurant(restaurant: RegisterRestaurantForm) {
  const url = `${BASE_PATH}`;
  const { image, ...data } = restaurant;
  console.log(restaurant);
  const response = await apiClient.post<Restaurant>(url, data);
  console.log(response);
  if ("location" in response.headers) {
    const formData = new FormData();
    formData.append("image", image);
    const imageUrl = response.headers["location"] + "/image";
    console.log(image);
    const imageResponse = await apiClient.put(imageUrl, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }
  return response.data;
}

export async function updateRestaurant({
  restaurant,
  id,
}: {
  id: string;
  restaurant: RegisterRestaurantForm;
}) {
  console.log(restaurant);
  const url = `${BASE_PATH}/${id}`;
  const response = await apiClient.put<Restaurant>(url, restaurant);
  return response.data;
}

export async function isRestaurantNameAvailable(name: string) {
  const url = `${BASE_PATH}/` + name;
  const response = await apiClient.head<string>(url);
  return response.data;
}

export async function getRestaurantById(id: string) {
  const url = `${BASE_PATH}/${id}`;
  const response = await apiClient.get<Restaurant>(url);
  return response.data;
}

export async function getRestaurantTags() {
  const url = `/tags`;
  const response = await apiClient.get<string[]>(url);
  return response.data;
}

// export async function getRestaurantImage(id: string) {
//   const url = `${BASE_PATH}/${id}/image`;
//   const response = await apiClient.get<string>(url);
//   return response.data;
// }

export async function deleteRestaurantById(id: string) {
  const url = `${BASE_PATH}/${id}`;
  const response = await apiClient.delete(url);
  return response.status;
}

export async function likeRestaurant(id: string) {
  const url = `${BASE_PATH}/${id}/likes`;
  const response = await apiClient.post(url);
  return response.status;
}

export async function dislikeRestaurant(id: string) {
  const url = `${BASE_PATH}/${id}/likes`;
  const response = await apiClient.delete(url);
  return response.status;
}

export async function getRestaurantLike(restaurantId: string, userId: string) {
  const url = `${BASE_PATH}/${restaurantId}/likes?userId=${userId}`;
  const response = await apiClient.get(url);
  return response.data;
}

export async function rateRestaurant(restaurantId: string, rating: Rate) {
  const url = `${BASE_PATH}/${restaurantId}/ratings?rating=${rating}`;
  const response = await apiClient.post<Rate>(url, rating);
  return response.data;
}

export async function getRestaurantRating(
  restaurantId: string,
  userId: string
) {
  const url = `${BASE_PATH}/${restaurantId}/ratings?userId=${userId}`;
  const response = await apiClient.get(url);
  return response.data;
}

export async function reviewRestaurant(id: string, review: Review) {
  const url = `${BASE_PATH}/${id}/reviews`;
  console.log(review);
  const response = await apiClient.post(url, review);
  return response.status;
}

export async function addMenuItem(id: string, item: MenuItem) {
  const url = `${BASE_PATH}/${id}/menu`;
  const response = await apiClient.post(url, item);
  return response.status;
}

export async function deleteMenuItem(restaurantId: string, itemId: string) {
  const url = `${BASE_PATH}/${restaurantId}/menu/${itemId}`;
  const response = await apiClient.delete(url);
  return response.status;
}

export async function getRestaurants(params = NO_FILTER) {
  const url = `${BASE_PATH}/`;
  const response = await apiClient.get<Restaurant[]>(url, { params });
  const links = {
    first: 0,
    last: 0,
    next: 0,
    prev: 0,
  };

  response.headers.link?.split(",").forEach((str) => {
    const linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/gi.exec(str);
    if (linkInfo != null) {
      const pageInfo = /page=([^&]*)/gi.exec(linkInfo[1]);
      if (pageInfo != null) {
        // Redudant switch because typesript
        // if (["first", "last", "next", "prev"].includes(linkInfo[2]))
        // links[linkInfo[2]] = parseInt(pageInfo[1]);
        switch (linkInfo[2]) {
          case "first": {
            links["first"] = parseInt(pageInfo[1]);
            break;
          }
          case "last": {
            links["last"] = parseInt(pageInfo[1]);
            break;
          }
          case "next": {
            links["next"] = parseInt(pageInfo[1]);
            break;
          }
          case "prev": {
            links["prev"] = parseInt(pageInfo[1]);
            break;
          }
        }
      }
    }
  });

  const page: Page<Restaurant> = {
    data: response.data,
    meta: {
      perPage: response.data.length,
      maxPages: links?.hasOwnProperty("last") ? links.last : 0,
    },
  };
  return page;
}

export async function getRestaurantMenu(id: string, params = NO_FILTER) {
  const url = `${BASE_PATH}/${id}/menu`;
  const response = await apiClient.get<MenuItem[]>(url, { params });
  const links = {
    first: 0,
    last: 0,
    next: 0,
    prev: 0,
  };

  response.headers.link?.split(",").forEach((str) => {
    const linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/gi.exec(str);
    if (linkInfo != null) {
      const pageInfo = /page=([^&]*)/gi.exec(linkInfo[1]);
      if (pageInfo != null) {
        switch (linkInfo[2]) {
          case "first": {
            links["first"] = parseInt(pageInfo[1]);
            break;
          }
          case "last": {
            links["last"] = parseInt(pageInfo[1]);
            break;
          }
          case "next": {
            links["next"] = parseInt(pageInfo[1]);
            break;
          }
          case "prev": {
            links["prev"] = parseInt(pageInfo[1]);
            break;
          }
        }
      }
    }
  });

  const page: Page<MenuItem> = {
    data: response.data,
    meta: {
      perPage: response.data.length,
      maxPages: links?.hasOwnProperty("last") ? links.last : 0,
    },
  };
  return page;
}

export async function getRestaurantReviews(id: string, params = NO_FILTER) {
  const url = `${BASE_PATH}/${id}/reviews`;
  const response = await apiClient.get<Review[]>(url, { params });
  const links = {
    first: 0,
    last: 0,
    next: 0,
    prev: 0,
  };

  response.headers.link?.split(",").forEach((str) => {
    const linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/gi.exec(str);
    if (linkInfo != null) {
      const pageInfo = /page=([^&]*)/gi.exec(linkInfo[1]);
      if (pageInfo != null) {
        switch (linkInfo[2]) {
          case "first": {
            links["first"] = parseInt(pageInfo[1]);
            break;
          }
          case "last": {
            links["last"] = parseInt(pageInfo[1]);
            break;
          }
          case "next": {
            links["next"] = parseInt(pageInfo[1]);
            break;
          }
          case "prev": {
            links["prev"] = parseInt(pageInfo[1]);
            break;
          }
        }
      }
    }
  });

  const page: Page<Review> = {
    data: response.data,
    meta: {
      perPage: response.data.length,
      maxPages: links?.hasOwnProperty("last") ? links.last : 0,
    },
  };
  return page;
}
