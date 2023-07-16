import { RestaurantFilterParams } from "../../types/filters";
import {
  DEFAULT_PAGE,
  MAX_PAGE_AMOUNT,
  Page,
  PageParams,
} from "../../types/page";
import {
  IRestaurant,
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
}

async function _getAll(
  params: PageParams & RestaurantFilterParams,
  filterBy?: string
) {
  if ((params?.pageAmount ?? 0) > MAX_PAGE_AMOUNT) {
    params.pageAmount = MAX_PAGE_AMOUNT;
  }
  try {
    const apiParams = {
      ...params,
      filterBy,
    };
    const response = await apiClient.get<IRestaurant[]>(PATH, {
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
    return _getAll(params);
  }

  export async function getPopular() {
    return _getAll(DEFAULT_PAGE, "popular");
  }

  export async function getHot() {
    return _getAll(DEFAULT_PAGE, "hot");
  }
}

const RestaurantService: IRestaurantService = RestaurantServiceImpl;
export { RestaurantService };
