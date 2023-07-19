import {afterAll, afterEach, assert, beforeAll, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, screen, fireEvent, waitFor, cleanup, renderHook} from '@testing-library/react';
import {LoginPage} from '../src/pages/Login'
import { QueryClient, QueryClientProvider } from 'react-query';

import React from 'react';
import { MemoryRouter } from 'react-router-dom';

let queryClient: QueryClient;

const mockLogin = vi.fn( () => {
    return Promise.resolve({token: 'token', userId: '1'})
})

const mockLoginError = vi.fn(() => {
    return Promise.reject({
      message: 'Authorization Error',
      cause: { code: 'invalid_credentials' },
    });
  });
const mockSetCredentials = vi.fn();

vi.mock('../src/hooks/useAuth', () => {
    return {
      useAuth: () => ({
        setCredentials: mockSetCredentials,
        isAuthenticated: false, // Set the initial value for isAuthenticated, if needed.
      }),
    };
  });


beforeEach(() => {
  queryClient = new QueryClient();
  render(
    <QueryClientProvider client={queryClient}>
        <MemoryRouter>
            <LoginPage/>
        </MemoryRouter>
    </QueryClientProvider>
  );
})

afterEach(() => {
  cleanup();
});

afterAll(
  () => {
    vi.resetAllMocks();
  }  
)

describe('Login tests', () => {
    test('Should redirect after successful login', async () => {
        vi.mock('../src/api/services/AuthService.ts', () => {
            const mockedLoginService = () => mockLogin;
            return { AuthService: { login: mockedLoginService } };
        });
        const emailInput = screen.getByLabelText(/email-input/i);
        const passwordInput = screen.getByLabelText(/pass-input/i);

        await fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
        await fireEvent.change(passwordInput, { target: { value: 'password123' } });
        const loginButton = screen.getByLabelText(/login-button/i);
        await fireEvent.click(loginButton);
        await waitFor(async () => expect(mockSetCredentials).toBeCalled());
    }),

    test('Should handle login error', async () => {
        // Mock AuthService.login to return a failed login response
        vi.mock('../src/api/services/AuthService.ts', () => {
          const mockedLoginService = () => mockLoginError;
          return { AuthService: { login: mockedLoginService } };
        });
  
        const emailInput = screen.getByLabelText(/email-input/i);
        const passwordInput = screen.getByLabelText(/pass-input/i);
  
        await fireEvent.change(emailInput, { target: { value: 'invalid@example.com' } });
        await fireEvent.change(passwordInput, { target: { value: 'invalidPassword' } });
        const loginButton = screen.getByLabelText(/login-button/i);
        await fireEvent.click(loginButton);
      });
});