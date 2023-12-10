import { HomePage } from "@/pages/Home/Home";
import { LoginPage } from "@/pages/Login/Login";
import { NotFoundPage } from "@/pages/NotFound/NotFound";
import { RegisterPage } from "@/pages/Register/Register";
import { IRoute } from "@/types/route";

export const routes: IRoute[] = [
  {
    path: "/",
    element: <HomePage />,
    title: "Home | Gourmetable",
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
    path: "/user/activate",
    protection: { type: "public", redirectPath: "/" },
    element: <RegisterPage />,
    title: "Register | Gourmetable",
  },
  {
    path: "*",
    element: <NotFoundPage />,
    title: "Not Found | Gourmetable",
  },
];
