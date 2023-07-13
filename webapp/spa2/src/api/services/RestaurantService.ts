import { RestaurantFilterParams } from "../../types/filters";
import { Page, PageParams } from "../../types/page";
import { IRestaurant, IRestaurantResponse } from "../../types/restaurant/restaurant.models";
import { apiClient, apiErrorHandler } from "../client";
import { pagedResponse } from "../utils";

const PATH = "/restaurants";
interface IRestaurantService {
  getAll(
    pageParams: PageParams,
    filterParams?: RestaurantFilterParams
  ): Promise<Page<IRestaurantResponse[]>>;
}

module RestaurantServiceImpl {
  export async function getAll(
    pageParams: PageParams,
    filterParams?: RestaurantFilterParams
  ) {
    try {
      const params = {
        ...pageParams,
        ...filterParams,
      };
      const response = await apiClient.get<IRestaurant[]>(PATH, { params });
      return pagedResponse(response);
    } catch (error) {
      throw new Error("General Error", {
        cause: apiErrorHandler(error),
      });
    }
  }
}

const RestaurantService: IRestaurantService = RestaurantServiceImpl;
export { RestaurantService };
