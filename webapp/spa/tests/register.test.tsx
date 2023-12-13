import {afterAll, afterEach, beforeAll, beforeEach, describe, expect, test, vi} from 'vitest';
import {render, screen, fireEvent, waitFor, cleanup} from '@testing-library/react';
import {RegisterPage} from '../src/pages/Register/Register'
import {
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";import { rest } from 'msw';
import { setupServer } from 'msw/node';
import * as userHook from '../src/hooks/user.hooks'

import React from 'react';
import { MemoryRouter } from 'react-router-dom';
import { t } from 'i18next';

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

describe('Register input tests', () => {
    test('Should show error message when invalid phone input', async () => {
      render(
        <QueryClientProvider client={queryClient}>
            <MemoryRouter>
                <RegisterPage/>
            </MemoryRouter>
        </QueryClientProvider>
      );
        const emailInput = screen.getByLabelText(/email-input/i);
        const usernameInput = screen.getByLabelText(/username-input/i);
        const passwordInput = screen.getByLabelText(/pass-input/i);
        const repeatPasswordInput = screen.getByLabelText(/repeat-input/i);
        const firstNameInput = screen.getByLabelText(/first-name-input/i);
        const lastNameInput = screen.getByLabelText(/last-name-input/i);
        const phoneInput = screen.getByLabelText(/phone-input/i);

        await fireEvent.change(emailInput, { target: { value: 'test@test.com' } });
        await fireEvent.change(usernameInput, { target: { value: 'username' } });
        await fireEvent.change(passwordInput, { target: { value: 'password123' } });
        await fireEvent.change(repeatPasswordInput, { target: { value: 'password123' } });
        await fireEvent.change(firstNameInput, { target: { value: 'name' } });
        await fireEvent.change(lastNameInput, { target: { value: 'lastName' } });
        await fireEvent.change(phoneInput, { target: { value: 'WRONG' } });
        const registerButton = screen.getByLabelText(/register-button/i);
        await fireEvent.click(registerButton);
        await fireEvent.click(registerButton);
        const errorMsg = screen.queryByText('Invalid');
        expect(errorMsg).toBeDefined();
     })
});

describe('Correct data processing', () => {
  test('Should call useCreateUser hook after correct form data', async () => {
    const useCreateUserSpy = vi.spyOn(userHook, 'useCreateUser');
    const { getByLabelText } = render(
      <QueryClientProvider client={queryClient}>
          <MemoryRouter>
              <RegisterPage/>
          </MemoryRouter>
      </QueryClientProvider>
    );
      const emailInput = getByLabelText(/email-input/i);
      const usernameInput = getByLabelText(/username-input/i);
      const passwordInput = getByLabelText(/pass-input/i);
      const repeatPasswordInput = getByLabelText(/repeat-input/i);
      const firstNameInput = getByLabelText(/first-name-input/i);
      const lastNameInput = getByLabelText(/last-name-input/i);
      const phoneInput = getByLabelText(/phone-input/i);

      await fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
      await fireEvent.change(usernameInput, { target: { value: 'username' } });
      await fireEvent.change(passwordInput, { target: { value: 'password123' } });
      await fireEvent.change(repeatPasswordInput, { target: { value: 'password123' } });
      await fireEvent.change(firstNameInput, { target: { value: 'name' } });
      await fireEvent.change(lastNameInput, { target: { value: 'lastName' } });
      await fireEvent.change(phoneInput, { target: { value: '123456789' } });
      const registerButton = screen.getByLabelText(/register-button/i);
      await fireEvent.click(registerButton);

      expect(useCreateUserSpy).toBeCalled();

   })
});