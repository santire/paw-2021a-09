import { RestaurantFilterParams } from "../../types/filters";
import {
  DEFAULT_PAGE,
  MAX_PAGE_AMOUNT,
  Page,
  PageParams,
} from "../../types/page";
import {
  IRestaurant,
  IRestaurantRegister,
  IRestaurantResponse,
} from "../../types/restaurant/restaurant.models";
import { apiClient, apiErrorHandler } from "../client";
import { pagedResponse } from "../utils";

const PATH = "/restaurants";
interface IRestaurantService {
  getAll(
    params: PageParams & RestaurantFilterParams
  ): Promise<Page<IRestaurantResponse[]>>;
  getPopular(): Promise<Page<IRestaurantResponse[]>>;
  getHot(): Promise<Page<IRestaurantResponse[]>>;

  create(params: IRestaurantRegister): Promise<IRestaurantResponse>;
  deleteRestaurant(restaurantId: number): Promise<void>;
  getOwnedRestaurants(
    userId: number,
    params: PageParams
  ): Promise<Page<IRestaurantResponse[]>>;
}

async function _getAll(
  params: PageParams & RestaurantFilterParams,
  filterBy?: string,
  path?: string
) {
  if ((params?.pageAmount ?? 0) > MAX_PAGE_AMOUNT) {
    params.pageAmount = MAX_PAGE_AMOUNT;
  }
  try {
    const apiParams = {
      ...params,
      filterBy,
    };
    const response = await apiClient.get<IRestaurant[]>(path ?? PATH, {
      params: apiParams,
    });
    return pagedResponse(response);
  } catch (error) {
    throw new Error("General Error", {
      cause: apiErrorHandler(error),
    });
  }
}

module RestaurantServiceImpl {
  export async function getAll(params: PageParams & RestaurantFilterParams) {
    return _getAll({ ...params, pageAmount: 6 });
  }

  export async function getPopular() {
    return _getAll(DEFAULT_PAGE, "popular");
  }

  export async function getHot() {
    return _getAll(DEFAULT_PAGE, "hot");
  }

  export async function getOwnedRestaurants(
    userId: number,
    params: PageParams
  ) {
    const url = `/users/${userId}/restaurants`;
    return _getAll({ ...params, pageAmount: 6 }, undefined, url);
  }

  export async function deleteRestaurant(restaurantId: number) {
    try {
      const url = `${PATH}/${restaurantId}`;
      const response = await apiClient.delete(url);
      return response.data;
    } catch (error) {
      throw new Error("Restaurant Delete Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          // TODO: NotFound?
        }),
      });
    }
  }

  async function setImage(location: string, image: any) {
    const formData = new FormData();
    formData.append("image", image);
    const imageUrl = location + "/image";
    const imgResponse = await apiClient.put(imageUrl, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return imgResponse.data;
  }

  export async function create(restaurant: IRestaurantRegister) {
    try {
      const url = `${PATH}`;
      const { image, ...data } = restaurant;
      if (!image) {
        throw Error("Missing Image");
      }
      const response = await apiClient.post<IRestaurantResponse>(url, data);
      if ("location" in response.headers) {
        const formData = new FormData();
        formData.append("image", image);
        try {
          await setImage(response.headers["location"], image);
        } catch (error) {
          await apiClient.delete(response.headers["location"]);
          throw error;
        }
      }
      return response.data;
    } catch (error) {
      throw new Error("Response Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          ConstraintViolationException: { code: "validation_error" },
          InvalidImageException: { code: "invalid_image" },
        }),
      });
    }
  }
}

const RestaurantService: IRestaurantService = RestaurantServiceImpl;
export { RestaurantService };
