import {assert, beforeEach, describe, expect, test} from 'vitest';
import {render, screen} from '@testing-library/react';
import {RestaurantCard} from '../src/components/RestaurantCard/RestaurantCard'
import mockRestaurant from './mocks/restaurant.mock'
import { MemoryRouter } from 'react-router-dom'; 
import { QueryClient, QueryClientProvider } from 'react-query';
import React from 'react';

// Initialize QueryClient before each test
let queryClient: QueryClient;

beforeEach(() => {
    queryClient = new QueryClient();
    const container = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <RestaurantCard restaurant={mockRestaurant} />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });

describe('Framework testing', () => {
    // Render test
    test('Should display correct restaurant name', () => {
        const nameElement = screen.getByText(mockRestaurant.name);
        assert(nameElement !== null, 'nameElement should not be null');
        expect(nameElement.textContent === mockRestaurant.name); 
      });
})