// mockQueryResult.ts

import { UseQueryResult } from "@tanstack/react-query";
import { vi } from "vitest";
import { IRestaurant } from "../../src/types/restaurant/restaurant.models";
import { Page } from "../../src/types/page";
import mockRestaurant from "./restaurant.mock";

const restaurants = [mockRestaurant];
export const mockQueryResult: UseQueryResult<Page<IRestaurant[]>> = {
  data: {
    data: restaurants, // Initialize with an empty array or your desired data
    meta: {
      maxPages: 1,
      perPage: 1,
      total: 1,
    },
  },
  error: null,
  failureCount: 0,
  errorUpdateCount: 0,
  isFetched: true,
  isRefetching: false,
  isFetchedAfterMount: true,
  isError: false,
  isLoading: false,
  isPending: false,
  isPaused: false,
  isFetching: false,
  isSuccess: true,
  status: "success",
  dataUpdatedAt: 0,
  errorUpdatedAt: 0,
  isLoadingError: false,
  isPlaceholderData: false,
  isRefetchError: false,
  isInitialLoading: false,
  fetchStatus: "idle",
  isStale: false,
  failureReason: new Error(),
  refetch: vi.fn(),
};
