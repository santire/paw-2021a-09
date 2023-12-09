export interface IRoute {
  path: string;
  element: JSX.Element;
  protection?: RouteProtection;
  title?: string;
}

export interface RouteProtection {
  type: "authed" | "public";
  redirectPath?: string;
}
