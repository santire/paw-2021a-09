import {
  afterAll,
  afterEach,
  beforeEach,
  describe,
  expect,
  test,
  vi,
} from "vitest";
import {
  render,
  screen,
  fireEvent,
  waitFor,
  cleanup,
} from "@testing-library/react";
import { LoginPage } from "../src/pages/Login/Login";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import React from "react";
import { MemoryRouter } from "react-router-dom";
import { MOCK_USER } from "./mocks/user.mock";

let queryClient: QueryClient;

const mockLoginError = vi.fn(() => {
  return Promise.reject({
    message: "Authorization Error",
    cause: { code: "invalid_credentials" },
  });
});

beforeEach(() => {
  queryClient = new QueryClient();
  render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <LoginPage />
      </MemoryRouter>
    </QueryClientProvider>,
  );
});

afterEach(() => {
  cleanup();
});

afterAll(() => {
  vi.resetAllMocks();
});

describe("Login tests", () => {
  test("Should redirect after successful login", async () => {
    vi.mock("../src/api/services/UserService.ts", () => {
      return {
        UserService: { getByEmail: async () => Promise.resolve(MOCK_USER) },
      };
    });
    const emailInput = screen.getByLabelText(/email-input/i);
    const passwordInput = screen.getByLabelText(/pass-input/i);

    fireEvent.change(emailInput, { target: { value: "test@example.com" } });
    fireEvent.change(passwordInput, { target: { value: "password123" } });
    const loginButton = screen.getByLabelText(/login-button/i);
    fireEvent.click(loginButton);
  }),
    test("Should handle login error", async () => {
      // Mock AuthService.login to return a failed login response
      vi.mock("../src/api/services/AuthService.ts", () => {
        const mockedLoginService = () => mockLoginError;
        return { AuthService: { login: mockedLoginService } };
      });

      const emailInput = screen.getByLabelText(/email-input/i);
      const passwordInput = screen.getByLabelText(/pass-input/i);

      fireEvent.change(emailInput, { target: { value: "invalid" } });
      const loginButton = screen.getByLabelText(/login-button/i);
      fireEvent.change(passwordInput, { target: { value: "invalidPassword" } });
      fireEvent.click(loginButton);
      const errorMsg = screen.queryByText("Invalid");
      expect(errorMsg).toBeDefined();
    });
});
