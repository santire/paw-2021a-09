import { IRestaurant } from "@/types/restaurant/restaurant.models";
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

const RestaurantService: IRestaurantService = RestaurantServiceImpl;
export { RestaurantService };
