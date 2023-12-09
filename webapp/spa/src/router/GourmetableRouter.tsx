import { createBrowserRouter } from "react-router-dom";
import Layout from "@/components/Layout/Layout";
import { Helmet } from "react-helmet-async";
import { RouteGuard } from "./RouteGuard";
import { routes } from "./routes";

const elementRouter = routes.map(({ path, element, protection, title }) => ({
  path: path,
  element: (
    <RouteGuard protection={protection}>
      <>
        {title && (
          <Helmet>
            <title>{title}</title>
          </Helmet>
        )}
        {element}
      </>
    </RouteGuard>
  ),
}));

export const GourmetableRouter = createBrowserRouter(
  [
    {
      element: <Layout />,
      children: elementRouter,
    },
  ],
  { basename: import.meta.env.BASE_URL },
);
