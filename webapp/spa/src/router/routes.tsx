import { HomePage } from "@/pages/Home/Home";
import { NotFoundPage } from "@/pages/NotFound/NotFound";
import { IRoute } from "@/types/route";

export const routes: IRoute[] = [
  {
    path: "/",
    element: <HomePage />,
    title: "Home | Gourmetable",
  },
  {
    path: "/protected",
    protection: { type: "authed", redirectPath: "/" },
    element: <HomePage />,
    title: "Home | Gourmetable",
  },
  {
    path: "*",
    element: <NotFoundPage />,
    title: "Not Found | Gourmetable",
  },
];
