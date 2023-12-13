import {
  afterAll,
  afterEach,
  beforeEach,
  describe,
  expect,
  test,
  vi,
} from "vitest";
import { render, fireEvent, cleanup } from "@testing-library/react";
import { mockQueryResult } from "./mocks/restaurantPage.mock"; // Import the mock properties

import { MemoryRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import * as useRestaurants from "../src/hooks/restaurant.hooks";
import * as searchParams from "../src/hooks/searchParams.hooks";

import React from "react";
import { Filter } from "../src/components/Filter/Filter";
import { RestaurantFilterAndPageParamsProvider } from "@/context/RestaurantFilterAndPageContext";
import * as useRestaurantFilterAndPage from "@/context/RestaurantFilterAndPageContext";
import { RestaurantsPage } from "@/pages/Restaurants/Restaurants";

const setFilterParamsSpy = vi.fn();

// Mock the context hook to spy on setFilterParams
vi.spyOn(
  useRestaurantFilterAndPage,
  "useRestaurantFilterAndPage",
).mockReturnValue({
  filterParams: {},
  setFilterParams: setFilterParamsSpy,
  setPageParams: vi.fn(),
});

let queryClient: QueryClient;

beforeEach(() => {
  queryClient = new QueryClient();
});

afterEach(() => {
  cleanup();
});

afterAll(() => {
  vi.resetAllMocks();
});

describe("Api call tests", () => {
  test("Should call restaurants api with applied filters in url", () => {
    const useRestaurantsSpy = vi.spyOn(useRestaurants, "useGetRestaurants");

    // Create a mock response that matches the expected type
    const mockResponse = mockQueryResult;
    useRestaurantsSpy.mockReturnValue(mockResponse);

    const { getByText } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RestaurantsPage />
        </MemoryRouter>
      </QueryClientProvider>,
    );
    expect(getByText("Sample Restaurant"));
  });
});

describe("Filters tests", () => {
  test("When clear button is pressed, should call clearAll", () => {
    const { getByLabelText } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RestaurantFilterAndPageParamsProvider>
            <Filter />
          </RestaurantFilterAndPageParamsProvider>
        </MemoryRouter>
      </QueryClientProvider>,
    );

    // Find the "Filter" button and click it
    const clearButton = getByLabelText(/filter-button/i);
    fireEvent.click(clearButton);
    expect(setFilterParamsSpy).toHaveBeenCalled();
  });
});
