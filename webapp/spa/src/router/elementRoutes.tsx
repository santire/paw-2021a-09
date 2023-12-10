import { Helmet } from "react-helmet-async";
import { RouteGuard } from "./RouteGuard";
import { routes } from "./routes";
import { QueryClient } from "@tanstack/react-query";

export const elementRouter = (queryClient: QueryClient) =>
  routes.map(({ path, element, protection, title, loader }) => ({
    path: path,
    loader: loader ? loader(queryClient) : undefined,
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
