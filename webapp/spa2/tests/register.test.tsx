import {afterAll, afterEach, beforeAll, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, screen, fireEvent, waitFor, cleanup} from '@testing-library/react';
import {RegisterPage} from '../src/pages/Register'
import { QueryClient, QueryClientProvider } from 'react-query';
import { rest } from 'msw';
import { setupServer } from 'msw/node';

import React from 'react';
import { MemoryRouter } from 'react-router-dom';

let queryClient: QueryClient;

// Create a mock server
const server = setupServer(
    rest.post('/users', (req, res, ctx) => {
      return res(ctx.json({ success: true }));
    })
);

const mockSetCredentials = vi.fn();

vi.mock('../src/hooks/useAuth', () => {
    return {
      useAuth: () => ({
        setCredentials: mockSetCredentials,
        isAuthenticated: false, // Set the initial value for isAuthenticated, if needed.
      }),
    };
  });

beforeAll(() => {
    server.listen();
});


beforeEach(() => {
  queryClient = new QueryClient();
  render(
    <QueryClientProvider client={queryClient}>
        <MemoryRouter>
            <RegisterPage/>
        </MemoryRouter>
    </QueryClientProvider>
  );
})

afterEach(() => {
    server.close();
    cleanup();
});

afterAll(
  () => {
    vi.resetAllMocks();
  }  
)

describe('Register tests', () => {
    test('Should redirect after successful registration', async () => {
        const emailInput = screen.getByLabelText(/email-input/i);
        const usernameInput = screen.getByLabelText(/username-input/i);
        const passwordInput = screen.getByLabelText(/pass-input/i);
        const repeatPasswordInput = screen.getByLabelText(/repeat-input/i);
        const firstNameInput = screen.getByLabelText(/first-name-input/i);
        const lastNameInput = screen.getByLabelText(/last-name-input/i);
        const phoneInput = screen.getByLabelText(/phone-input/i);

        await fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
        await fireEvent.change(usernameInput, { target: { value: 'password123' } });
        await fireEvent.change(passwordInput, { target: { value: 'password123' } });
        await fireEvent.change(repeatPasswordInput, { target: { value: 'password123' } });
        await fireEvent.change(firstNameInput, { target: { value: 'name' } });
        await fireEvent.change(lastNameInput, { target: { value: 'lastName' } });
        await fireEvent.change(phoneInput, { target: { value: '123456789' } });
        const registerButton = screen.getByLabelText(/register-button/i);
        await fireEvent.click(registerButton);
        // Wait for the API call to finish
        const confirmation = screen.getByLabelText(/confirmation-text/i);
        expect(confirmation).toBeDefined();
        // await waitFor(() => {
        // });   
     })
});