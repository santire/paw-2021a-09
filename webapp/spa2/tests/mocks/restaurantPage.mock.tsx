// mockQueryResult.ts

import { UseQueryResult } from 'react-query';
import { vi } from 'vitest';
import { IRestaurant } from '../../src/types/restaurant/restaurant.models';
import { Page } from '../../src/types/page';
import mockRestaurant from './restaurant.mock'

const restaurants = [
    mockRestaurant
];
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
  remove: vi.fn(),
  isFetchedAfterMount: true,
  isError: false,
  isLoading: false,
  isFetching: false,
  isIdle: false,
  isSuccess: true,
  status: 'success',
  dataUpdatedAt: 0,
  errorUpdatedAt: 0,
  isLoadingError: false,
  isPlaceholderData: false,
  isPreviousData: false,
  isRefetchError: false,
  isStale: false,
  refetch: vi.fn(),
};