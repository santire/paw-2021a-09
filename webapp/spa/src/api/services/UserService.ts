import { apiClient } from "../client";
import {Restaurant, User} from "../../types";
import { Page } from "../../types/Page";

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

const BASE_PATH = "users";
export const NO_FILTER: FilterParams = {
  page: 1,
  pageAmount: 10,
};

export async function updateProfile(user: User) {
  console.log(user);
  const url = `${BASE_PATH}/${user.userId}`;
  user.userId = undefined;
  const response = await apiClient.put<User>(url, user);
  return response.data;
}

export async function getUserById(id: string) {
  const url = `${BASE_PATH}/${id}`;
  const response = await apiClient.get<User>(url);
  return response.data;
}

export async function getUserRestaurants({
  userId,
  params = NO_FILTER,
}: {
  userId: string;
  params: FilterParams;
}) {
  const url = `${BASE_PATH}/${userId}/restaurants`;
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
