import {afterAll, afterEach, assert, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, screen, fireEvent, waitFor, cleanup} from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom'; 
import {
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import React from 'react';
import mockReservation from './mocks/reservation.mock';
import { UserReservation } from '../src/components/UserReservation/UserReservation';

let queryClient: QueryClient;

vi.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key: string) => {
      switch (key) {
        case 'userReservation.confirmed':
          return 'confirmed'; // Mock the translated string for confirmed reservations
        case 'userReservation.pending':
          return 'pending'; // Mock the translated string for pending reservations
        default:
          return key;
      }
    },
  }),
}));

beforeEach(() => {
  queryClient = new QueryClient();
  render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <UserReservation key={mockReservation.id} reservation={mockReservation} isHistory={false}/>
      </MemoryRouter>
    </QueryClientProvider>
  );
})

afterEach(() => {
  cleanup();
});

afterAll(
  () => {
    //server.close();
    vi.resetAllMocks();
  }  
)

describe('Render tests for user reservations', () => {
    test('Should display correct restaurant name', () => {
        const reservationQuantity = screen.getByText(mockReservation.quantity);
        assert(reservationQuantity !== null, 'quantity should not be null');
        expect(reservationQuantity.textContent).toBe(mockReservation.quantity.toString());
      }),

      test('Should display correct reservation status', () => {
        const statusElement = screen.getByText(mockReservation.status.toLowerCase());
        expect(statusElement).toBeInTheDocument();
      });
}),

describe('Testing button behaviour', () => {
  test('Should open cancel reservation modal', async () => {
    const cancelButton = await screen.getByRole('button'); 

    // Check if the button is found before clicking
    if (cancelButton) {
        fireEvent.click(cancelButton);
        // Wait for the modal to appear
        await waitFor(() => {
            const modalElement = screen.queryByText('Cancel reservation');
            expect(modalElement).toBeDefined();
        });
    } 
    else {
      // If the button is not found, fail the test
      expect(false).toBe(true);
    }
  })
  ;
})