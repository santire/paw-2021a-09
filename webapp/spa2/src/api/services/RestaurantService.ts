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
    pageParams: PageParams,
    filterParams?: RestaurantFilterParams
  ): Promise<Page<IRestaurantResponse[]>>;

  getPopular(): Promise<Page<IRestaurantResponse[]>>;

  getHot(): Promise<Page<IRestaurantResponse[]>>;
}

async function _getAll(
  pageParams: PageParams,
  filterParams?: RestaurantFilterParams,
  filterBy?: string
) {
  if (pageParams.pageAmount > MAX_PAGE_AMOUNT) {
    pageParams.pageAmount = MAX_PAGE_AMOUNT;
  }
  try {
    const params = {
      ...pageParams,
      ...filterParams,
      filterBy,
    };
    const response = await apiClient.get<IRestaurant[]>(PATH, { params });
    return pagedResponse(response);
  } catch (error) {
    throw new Error("General Error", {
      cause: apiErrorHandler(error),
    });
  }
}

module RestaurantServiceImpl {
  export async function getAll(
    pageParams: PageParams,
    filterParams?: RestaurantFilterParams
  ) {
    return _getAll(pageParams, filterParams);
  }

  export async function getPopular() {
    return _getAll(DEFAULT_PAGE, undefined, "popular");
  }

  export async function getHot() {
    return _getAll(DEFAULT_PAGE, undefined, "hot");
  }
}

const RestaurantService: IRestaurantService = RestaurantServiceImpl;
export { RestaurantService };
