import {afterAll, afterEach, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, fireEvent, cleanup, act} from '@testing-library/react';

import { MemoryRouter } from 'react-router-dom'; 
import { QueryClient, QueryClientProvider } from 'react-query';
import * as searchParams from '../src/hooks/searchParams.hooks' 

import React from 'react';
import { SearchBar } from '../src/components/Header/Header';


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
        const useFilterSearchParamsSpy = vi.spyOn(searchParams, 'useFilterSearchParams');
      
        const { getByLabelText } = render(
            <QueryClientProvider client={queryClient}>
              <MemoryRouter>
                <SearchBar/>
              </MemoryRouter>
            </QueryClientProvider>
        );
        const searchInput = getByLabelText(/search-input/i);

        act(() => {
            fireEvent.change(searchInput, { target: { value: 'pastas' } });
            fireEvent.keyDown(searchInput, { key: 'Enter', code: 'Enter', keyCode: 13, charCode: 13 });
        });
        
        expect(searchInput).toBeDefined();
        expect(useFilterSearchParamsSpy).toBeCalled();
        //expect(useFilterSearchParamsSpy).toBeCalledWith({ search: 'pastas' });
    });
})