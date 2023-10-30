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
import { UserReservationsPage } from "./pages/UserReservations";
import { UserReservationsHistoryPage } from "./pages/UserReservationsHistory";
import { ValidateRestaurantReservation } from "./pages/RestaurantReservations";
import { ValidateRestaurantReservationHistory } from "./pages/RestaurantReservationsHistory";
import { Helmet, HelmetProvider } from 'react-helmet-async';

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
          element: 
          <>
            <Helmet>
              <title>Home: Gourmetable</title>
            </Helmet>
            <HomePage />
          </>
        },
        {
          path: "/restaurants",
          element: 
          <>
            <Helmet>
              <title>Restaurants: Gourmetable</title>
            </Helmet>
            <RestaurantsPage />
          </>
        },
        {
          path: "/restaurants/:restaurantId",
          element: <ValidateRestaurant />
        },
        {
          path: "/restaurants/:restaurantId/edit",
          element: 
          <>
            <Helmet>
              <title>Edit restaurant: Gourmetable</title>
            </Helmet>
            <ValidateRestaurantUpdate />
          </>
        },
        {
          path: "/restaurants/:restaurantId/reservations",
          element: 
          <>
            <Helmet>
              <title>Restaurant reservations: Gourmetable</title>
            </Helmet>
            <ValidateRestaurantReservation />
          </>
        },
        {
          path: "/restaurants/:restaurantId/reservations/history",
          element: 
          <>
          <Helmet>
            <title>Restaurant reservations history: Gourmetable</title>
          </Helmet>
          <ValidateRestaurantReservationHistory />
        </>
        },
        {
          path: "/restaurants/register",
          element: 
          <>
            <Helmet>
              <title>Register restaurant: Gourmetable</title>
            </Helmet>
            <RegisterRestaurantPage />
          </>
        },
        {
          path: "/login",
          element: 
          <>
            <Helmet>
              <title>Login: Gourmetable</title>
            </Helmet>
            <LoginPage />
          </>
        },
        {
          path: "/register",
          element: 
          <>
            <Helmet>
              <title>Register: Gourmetable</title>
            </Helmet>
            <RegisterPage />
          </>
        },
        {
          path: "/forgot",
          element: 
          <>
            <Helmet>
              <title>Forgot: Gourmetable</title>
            </Helmet>
            <ForgotPage />
          </>
        },
        {
          path: "/reset",
          element: 
          <>
            <Helmet>
              <title>Reset password: Gourmetable</title>
            </Helmet>
            <ResetPage />
          </>
        },
        {
          path: "/user/edit",
          element: 
          <>
            <Helmet>
              <title>Profile: Gourmetable</title>
            </Helmet>
            <ProfilePage />
          </>,
        },
        {
          path: "/user/reservations",
          element: 
          <>
            <Helmet>
              <title>Reservations: Gourmetable</title>
            </Helmet>
            <UserReservationsPage />
          </>
        },
        {
          path: "/user/reservations/history",
          element: 
          <>
            <Helmet>
              <title>Reservations history: Gourmetable</title>
            </Helmet>
            <UserReservationsHistoryPage />
          </>
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
              <HelmetProvider>
                <Helmet>
                  <title>Gourmetable</title>
                </Helmet>
                <RouterProvider router={router} />
              </HelmetProvider>
            </NotificationsProvider>
          </ThemeProvider>
        </Suspense>
      </AuthContextProvider>
    </QueryClientProvider>
  );
}

export default App;
