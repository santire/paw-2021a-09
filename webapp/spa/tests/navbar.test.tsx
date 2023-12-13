import {afterAll, afterEach, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, fireEvent, cleanup, act} from '@testing-library/react';

import { MemoryRouter } from 'react-router-dom'; 
import {
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import * as searchParams from '../src/hooks/searchParams.hooks' 

import React from 'react';

import { RestaurantFilterAndPageParamsProvider } from '@/context/RestaurantFilterAndPageContext';
import * as useRestaurantFilterAndPage from '@/context/RestaurantFilterAndPageContext'; 
import { SearchBar } from '@/components/Header/SearchBar';

const setFilterParamsSpy = vi.fn();

   // Mock the context hook to spy on setFilterParams
   vi.spyOn(useRestaurantFilterAndPage, 'useRestaurantFilterAndPage').mockReturnValue({
    filterParams: {},
    setFilterParams: setFilterParamsSpy,
    setPageParams: vi.fn(),
  });



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

describe('Search bar', () => {
    test('Should navigate to restaurants with params after pressing search', async () => {      
        const { getByLabelText } = render(
            <QueryClientProvider client={queryClient}>
              <MemoryRouter>
              <RestaurantFilterAndPageParamsProvider>
                <SearchBar/>
              </RestaurantFilterAndPageParamsProvider>
              </MemoryRouter>
            </QueryClientProvider>
        );
        const searchInput = getByLabelText(/search-input/i);

        act(() => {
            fireEvent.change(searchInput, { target: { value: 'pastas' } });
            fireEvent.keyDown(searchInput, { key: 'Enter', code: 'Enter', keyCode: 13, charCode: 13 });
        });
        
        expect(searchInput).toBeDefined();
        expect(setFilterParamsSpy).toHaveBeenCalled();
    });
})