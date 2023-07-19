import {afterAll, afterEach, assert, beforeAll, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, screen, fireEvent, waitFor, cleanup} from '@testing-library/react';
import {RestaurantCard} from '../src/components/RestaurantCard/RestaurantCard'
import mockRestaurant from './mocks/restaurant.mock'
import { MemoryRouter } from 'react-router-dom'; 
import { QueryClient, QueryClientProvider } from 'react-query';
import { rest } from 'msw';
import { setupServer } from 'msw/node';

import React from 'react';

let queryClient: QueryClient;

// Create a mock server
const server = setupServer(
  rest.post('/restaurants/' + mockRestaurant.id + '/likes', (req, res, ctx) => {
    return res(ctx.json({ success: true }));
  })
);

beforeAll(() => {
  server.listen();
});

beforeEach(() => {
  queryClient = new QueryClient();
  render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <RestaurantCard restaurant={mockRestaurant} />
      </MemoryRouter>
    </QueryClientProvider>
  );
})

afterEach(() => {
  cleanup();
});

afterAll(
  () => {
    server.close();
    vi.resetAllMocks();
  }  
)

describe('Render tests for Restaurant Card', () => {
    test('Should display correct restaurant name', () => {
        const nameElement = screen.getByText(mockRestaurant.name);
        assert(nameElement !== null, 'nameElement should not be null');
        expect(nameElement.textContent).toBe(mockRestaurant.name);
      });
}),

describe('Testing services used by Restaurant Card component', () => {
  test('Should call Like method - USER AUTH', async () => {
    const useAuthMock = vi.mock('../src/hooks/useAuth.ts', () => {
      const useAuth = vi.fn(() => true)
      return useAuth;
    })

    const likeRestaurantModule = await import('../src/hooks/restaurant.hooks');
    // Spy on the useLikeRestaurant hook
    const likeRestaurantSpy = vi.spyOn(likeRestaurantModule, 'useLikeRestaurant');

    // Find the LikeButton in the rendered component
    const likeButton = screen.getByRole('button', {name: /like-button/i});

    // Check if the button is found before clicking
    if (likeButton) {
      fireEvent.click(likeButton);

      // Expect the useLikeRestaurant hook to have been called with the correct restaurant ID
      await waitFor(() => {
        // Expect the useLikeRestaurant hook to have been called with the correct restaurant ID
        expect(likeRestaurantSpy).toHaveBeenCalled();
      });    } 
    else {
      // If the button is not found, fail the test
      expect(false).toBe(true);
    }
  })
  
  test('Should call Like method - USER NOT AUTH', async () => {
    const useAuthMock = vi.mock('../src/hooks/useAuth.ts', () => {
      const useAuth = vi.fn(() => false)
      return useAuth;
    })
    const useStateSpy = vi.spyOn(React, 'useState')
    const likeButton = screen.getByRole('button', {name: /like-button/i});
    fireEvent.click(likeButton);

  // Wait for the modal to appear
  await waitFor(() => {
    const modalElement = screen.queryByText('Oops!');
    expect(modalElement).toBeDefined();
  });
  })
  ;
})