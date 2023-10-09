import {afterAll, afterEach, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, fireEvent, cleanup} from '@testing-library/react';
import { mockQueryResult } from './mocks/restaurantPage.mock'; // Import the mock properties

import { MemoryRouter } from 'react-router-dom'; 
import { QueryClient, QueryClientProvider } from 'react-query';
import * as useRestaurants from '../src/hooks/restaurant.hooks';
import * as searchParams from '../src/hooks/searchParams.hooks' 

import React from 'react';
import {RestaurantsPage} from '../src/pages/Restaurants'
import { Filter } from '../src/components/Filter/Filter';


let queryClient: QueryClient;
  
beforeEach(() => {
  queryClient = new QueryClient();
})

afterEach(() => {
  cleanup();
});

afterAll(
  () => {
    vi.resetAllMocks();
  }  
)

describe('Api call tests', () => {
    test('Should call restaurants api with applied filters in url', () => {
        const useRestaurantsSpy = vi.spyOn(useRestaurants, 'useGetRestaurants');

        // Create a mock response that matches the expected type
        const mockResponse = mockQueryResult;
        useRestaurantsSpy.mockReturnValue(mockResponse);
      
        const { getByText } = render(
            <QueryClientProvider client={queryClient}>
              <MemoryRouter>
                <RestaurantsPage/>
              </MemoryRouter>
            </QueryClientProvider>
        );
        expect(getByText('Sample Restaurant'));
      });
})

describe('Filters tests', () => {
    test('When clear button is pressed, should call clearAll', () => {
        const searchParamsHookSpy = vi.spyOn(searchParams, 'useFilterSearchParams');

        const { getByLabelText } = render(
            <QueryClientProvider client={queryClient}>
              <MemoryRouter>
                <Filter/>
              </MemoryRouter>
            </QueryClientProvider>
        );

        // Find the "Filter" button and click it
        const clearButton = getByLabelText(/filter-button/i);
        fireEvent.click(clearButton);
        expect(searchParamsHookSpy).toBeCalled();
      });
})