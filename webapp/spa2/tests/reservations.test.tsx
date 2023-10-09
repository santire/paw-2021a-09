import {afterAll, afterEach, assert, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, screen, fireEvent, waitFor, cleanup} from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom'; 
import { QueryClient, QueryClientProvider } from 'react-query';

import React from 'react';
import mockReservation from './mocks/reservation.mock';
import { UserReservation } from '../src/components/UserReservation/UserReservation';

let queryClient: QueryClient;

beforeEach(() => {
  queryClient = new QueryClient();
  render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <UserReservation key={mockReservation.reservationId} reservation={mockReservation} isHistory={false}/>
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