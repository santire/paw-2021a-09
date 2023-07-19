import { Suspense, useEffect, useState } from "react";
import { AuthContextProvider } from "./context/AuthContext";
import { ThemeProvider } from "./context/ThemeContext";
import { QueryClient, QueryClientProvider } from "react-query";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import Layout from "./components/Layout/Layout";
import { NotFoundPage } from "./pages/NotFound";
import { LoginPage } from "./pages/Login";
import { RegisterPage } from "./pages/Register";
import { ForgotPage } from "./pages/Forgot";
import { ResetPage } from "./pages/Reset";
import { HomePage } from "./pages/Home";
import { RestaurantsPage } from "./pages/Restaurants";
import { ProfilePage } from "./pages/Profile";
import { RegisterRestaurantPage } from "./pages/RegisterRestaurant";
import { UserRestaurantsPage } from "./pages/UserRestaurants";
import { ValidateRestaurant } from "./pages/Restaurant";
import { ValidateRestaurantUpdate } from "./pages/UpdateRestaurant";
import { NotificationsProvider } from "@mantine/notifications";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      // refetchOnWindowFocus: false, // default: true
      // staleTime: Infinity,
    },
  },
});

// const basename = import.meta.env.VITE_CONTEXT || "";
const router = createBrowserRouter(
  [
    {
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
          element: <ValidateRestaurant />,
        },
        {
          path: "/restaurants/:restaurantId/edit",
          element: <ValidateRestaurantUpdate />,
        },
        {
          path: "/restaurants/register",
          element: <RegisterRestaurantPage />,
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
          path: "/user/edit",
          element: <ProfilePage />,
        },
        {
          path: "/user/restaurants",
          element: <UserRestaurantsPage />,
        },
        {
          path: "*",
          element: <NotFoundPage />,
        },
      ],
    },
  ],
  { basename: import.meta.env.BASE_URL }
);

function App() {
  const [, rerenderer] = useState(0);
  useEffect(() => {
    rerenderer(Math.random());
  }, []);
  return (
    <QueryClientProvider client={queryClient}>
      <AuthContextProvider>
        <Suspense fallback={<Layout />}>
          <ThemeProvider>
            <NotificationsProvider>
              <RouterProvider router={router} />
            </NotificationsProvider>
          </ThemeProvider>
        </Suspense>
      </AuthContextProvider>
    </QueryClientProvider>
  );
}

export default App;
