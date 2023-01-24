import { Suspense } from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "./components/Layout/Layout";
import { ThemeProvider } from "./components/ThemeProvider/ThemeProvider";
import { AuthProvider } from "./context/AuthContext";
import { ErrorPage } from "./pages/ErrorPage";
import { ForgotPage } from "./pages/ForgotPage";
import { RestaurantsPage } from "./pages/RestaurantsPage";
import { HomePage } from "./pages/HomePage";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { ResetPage } from "./pages/ResetPage";
import { RestaurantPage } from "./pages/RestaurantPage";
import { RegisterRestaurantPage } from "./pages/RegisterRestaurantPage";
import { UserRestaurantsPage } from "./pages/UserRestaurantsPage";
import { EditRestaurantPage } from "./pages/EditRestaurantPage";

const queryClient = new QueryClient();
const basename = process.env.REACT_APP_CONTEXT || "";

const router = createBrowserRouter(
  [
    {
      path: "/",
      element: <Layout />,
      children: [
        {
          path: "/",
          element: <HomePage />,
        },
        {
          path: "/restaurants",
          element: <RestaurantsPage />,
        },
        {
          path: "/restaurants/:restaurantId",
          element: <RestaurantPage />,
        },
        {
          path: "/restaurants/register",
          element: <RegisterRestaurantPage />
        },
        {
          path: "/restaurants/:restaurantId/edit",
          element: <EditRestaurantPage />
        },
        {
          path: "/login",
          element: <LoginPage />,
        },
        {
          path: "/register",
          element: <RegisterPage />,
        },
        {
          path: "/forgot",
          element: <ForgotPage />,
        },
        {
          path: "/reset",
          element: <ResetPage />,
        },
        {
          path: "/users/:userId/restaurants",
          element: <UserRestaurantsPage />
        },
        {
          path: "/users/:userId/restaurants",
          element: <UserRestaurantsPage />
        },
        {
          path: "*",
          // TODO: Change this to NotFound
          element: <ErrorPage />,
        },
      ],
    },
  ],
  { basename: basename }
);

export function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Suspense fallback="loading">
        <ThemeProvider>
          <AuthProvider>
            <RouterProvider router={router} />
          </AuthProvider>
        </ThemeProvider>
      </Suspense>
    </QueryClientProvider>
  );
}
