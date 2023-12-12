import {
  IRestaurant,
  IRestaurantRegister,
  IRestaurantUpdate,
} from "@/types/restaurant/restaurant.models";
import { apiClient } from "../client";
import { DEFAULT_PAGE, MAX_PAGE_AMOUNT, Page, PageParams } from "@/types/page";
import { RestaurantFilterParams } from "@/types/filters";
import { pagedResponse } from "../utils";

const PATH = "/restaurants";

interface IRestaurantService {
  get(id: number): Promise<IRestaurant>;

  getFilteredBy(
    params: RestaurantFilterParams & PageParams,
  ): Promise<Page<IRestaurant[]>>;
  getOwnedRestaurants(
    userId: number,
    params: PageParams,
  ): Promise<Page<IRestaurant[]>>;

  getPopular(): Promise<Page<IRestaurant[]>>;
  getHot(): Promise<Page<IRestaurant[]>>;

  create(userId: number, params: IRestaurantRegister): Promise<IRestaurant>;
  update(url: string, params: IRestaurantUpdate): Promise<IRestaurant>;
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

  export async function getPopular() {
    return _getAll(DEFAULT_PAGE, "popular");
  }

  export async function getHot() {
    return _getAll(DEFAULT_PAGE, "hot");
  }

  export async function getOwnedRestaurants(
    userId: number,
    params: PageParams,
  ) {
    const response = await apiClient.get<IRestaurant[]>(PATH, {
      params: {
        ownedBy: userId,
        ...params,
      },
    });
    return pagedResponse(response);
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
          code: "missing_image",
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

  export async function update(url: string, params: IRestaurantUpdate) {
    const { image, ...data } = params;
    if (!image) {
      console.log("uppps")
      throw new Error("Restaurant Creation Error", {
        cause: {
          status: "400",
          code: "missing_image",
          errors: [
            {
              subject: "image",
              message: "missing image",
            },
          ],
        },
      });
    }
    const response = await apiClient.patch<IRestaurant>(url, {
      ...data,
    });
    if (response.status === 200 && image) {
      const formData = new FormData();
      formData.append("image", image);
      await setImage(url, image);
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
