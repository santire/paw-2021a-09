import { ForgotPage } from "@/pages/Forgot/Forgot";
import { HomePage } from "@/pages/Home/Home";
import { LoginPage } from "@/pages/Login/Login";
import { NotFoundPage } from "@/pages/NotFound/NotFound";
import { ProfilePage } from "@/pages/Profile/Profile";
import { userProfileLoader } from "@/pages/Profile/Profile.loader";
import { RegisterPage } from "@/pages/Register/Register";
import { RegisterRestaurantPage } from "@/pages/RegisterRestaurant/RegisterRestaurant";
import { ResetPage } from "@/pages/Reset/Reset";
import { RestaurantPage } from "@/pages/Restaurant/Restaurant";
import { ValidateRestaurant } from "@/pages/Restaurant/ValidateRestaurant";
import { RestaurantsPage } from "@/pages/Restaurants/Restaurants";
import { ValidateRestaurantUpdate } from "@/pages/UpdateRestaurant/ValidateUpdateRestaurant";
import { UserRestaurantsPage } from "@/pages/UserRestaurants/UserRestaurants";
import { IRoute } from "@/types/route";

export const routes: IRoute[] = [
  {
    path: "/",
    element: <HomePage />,
    title: "Home | Gourmetable",
  },
  {
    path: "/restaurants",
    element: <RestaurantsPage />,
    title: "Browse Restaurants | Gourmetable",
  },
  {
    path: "/restaurants/register",
    protection: { type: "authed", redirectPath: "/" },
    element: <RegisterRestaurantPage />,
    title: "Browse Restaurants | Gourmetable",
  },
  {
    path: "/restaurants/:restaurantId",
    element: <ValidateRestaurant />,
  },
  {
    path: "/restaurants/:restaurantId/edit",
    protection: {type: "authed", redirectPath: "/"},
    element: <ValidateRestaurantUpdate />,
  },
  {
    path: "/login",
    protection: { type: "public", redirectPath: "/" },
    element: <LoginPage />,
    title: "Login | Gourmetable",
  },
  {
    path: "/register",
    protection: { type: "public", redirectPath: "/" },
    element: <RegisterPage />,
    title: "Register | Gourmetable",
  },
  {
    path: "/forgot",
    protection: { type: "public", redirectPath: "/" },
    element: <ForgotPage />,
    title: "Forgot User | Gourmetable",
  },
  {
    path: "/user/edit",
    protection: { type: "authed", redirectPath: "/" },
    element: <ProfilePage />,
    title: "User Profile | Gourmetable",
    loader: userProfileLoader,
  },
  {
    path: "/user/activate",
    protection: { type: "public", redirectPath: "/" },
    element: <RegisterPage />,
    title: "Activate User | Gourmetable",
  },
  {
    path: "/user/reset",
    protection: { type: "public", redirectPath: "/" },
    element: <ResetPage />,
    title: "Reset Password | Gourmetable",
  },
  {
    path: "/user/restaurants",
    protection: { type: "authed", redirectPath: "/" },
    element: <UserRestaurantsPage />,
    title: "Owned Restaurants | Gourmetable",
  },
  {
    path: "*",
    element: <NotFoundPage />,
    title: "Not Found | Gourmetable",
  },
];
