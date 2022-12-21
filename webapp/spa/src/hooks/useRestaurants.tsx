import { useQuery } from "react-query";
import { FilterParams, getRestaurants } from "../api/services";
import { Restaurant } from "../types";
import { Page } from "../types/Page";

export function useRestaurants(params: FilterParams) {
  return useQuery<Page<Restaurant>, Error>(
    ["restaurants", { ...params }],
    async () => getRestaurants({ pageAmount: 6, ...params }),
    { keepPreviousData: true }
  );
}
