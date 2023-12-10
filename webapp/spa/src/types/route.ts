import { QueryClient } from "@tanstack/react-query";
import { LoaderFunction } from "react-router-dom";

export interface IRoute {
  path: string;
  element: JSX.Element;
  protection?: RouteProtection;
  title?: string;
  loader?: (queryClient: QueryClient) => LoaderFunction<any>;
}

export interface RouteProtection {
  type: "authed" | "public";
  redirectPath?: string;
}
