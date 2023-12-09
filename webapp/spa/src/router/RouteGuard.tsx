import { RouteProtection } from "@/types/route";
import { isAuthed } from "@/utils/AuthStorage";
import { Navigate } from "react-router-dom";

interface Props {
  protection?: RouteProtection;
  children: JSX.Element;
}
export function RouteGuard({ protection, children }: Props) {
  const isAuthenticated = isAuthed();
  if (protection?.type === "authed") {
    if (!isAuthenticated) {
      return <Navigate to={protection.redirectPath ?? "/login"} replace />;
    }
  }
  if (protection?.type === "public") {
    if (isAuthenticated) {
      return <Navigate to={protection.redirectPath ?? "/"} replace />;
    }
  }
  return children;
}
