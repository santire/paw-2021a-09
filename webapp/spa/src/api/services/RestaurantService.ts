import {
  IRestaurant,
  IRestaurantRegister,
} from "@/types/restaurant/restaurant.models";
import { apiClient } from "../client";
import { MAX_PAGE_AMOUNT, Page, PageParams } from "@/types/page";
import { RestaurantFilterParams } from "@/types/filters";
import { pagedResponse } from "../utils";

const PATH = "/restaurants";

interface IRestaurantService {
  get(id: number): Promise<IRestaurant>;

  getFilteredBy(
    params: RestaurantFilterParams & PageParams,
  ): Promise<Page<IRestaurant[]>>;

  create(userId: number, params: IRestaurantRegister): Promise<IRestaurant>;
  remove(url: string): Promise<void>;
}

module RestaurantServiceImpl {
  export async function get(id: number) {
    const response = await apiClient.get<IRestaurant>(`${PATH}/${id}`);
    return response.data;
  }
  export async function getFilteredBy(
    params: RestaurantFilterParams & PageParams,
  ) {
    return _getAll(params);
  }

  export async function create(
    userId: number,
    restaurant: IRestaurantRegister,
  ) {
    const { image, ...data } = restaurant;
    if (!image) {
      throw new Error("Restaurant Creation Error", {
        cause: {
          status: "400",
          code: "validation_error",
          errors: [
            {
              subject: "image",
              message: "missing image",
            },
          ],
        },
      });
    }
    const response = await apiClient.post<IRestaurant>(PATH, {
      ownerId: userId,
      ...data,
    });
    const newRestLocation = response.headers["location"];
    if (newRestLocation) {
      const formData = new FormData();
      formData.append("image", image);
      try {
        await setImage(newRestLocation, image);
      } catch (error) {
        await apiClient.delete(newRestLocation);
        throw error;
      }
    }
    return response.data;
  }

  export async function remove(url: string) {
    const response = await apiClient.delete(url);
    return response.data;
  }
}

async function _getAll(
  params: PageParams & RestaurantFilterParams,
  filterBy?: string,
  path?: string,
) {
  if ((params?.pageAmount ?? 0) > MAX_PAGE_AMOUNT) {
    params.pageAmount = MAX_PAGE_AMOUNT;
  }
  const apiParams = {
    ...params,
    filterBy,
  };
  const response = await apiClient.get<IRestaurant[]>(path ?? PATH, {
    params: apiParams,
  });
  return pagedResponse(response);
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

const RestaurantService: IRestaurantService = RestaurantServiceImpl;
export { RestaurantService };
