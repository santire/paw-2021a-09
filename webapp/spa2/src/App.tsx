import { Suspense } from "react";
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
import { RestaurantPage, ValidateRestaurant } from "./pages/Restaurant";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      // refetchOnWindowFocus: false, // default: true
      staleTime: Infinity,
    },
  },
});

const basename = import.meta.env.VITE_CONTEXT || "";
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
  { basename: basename }
);

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Suspense fallback={<Layout />}>
        <ThemeProvider>
          <AuthContextProvider>
            <RouterProvider router={router} />
          </AuthContextProvider>
        </ThemeProvider>
      </Suspense>
    </QueryClientProvider>
  );
}

export default App;
