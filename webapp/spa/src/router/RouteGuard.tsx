import { RouteProtection } from "@/types/route";
import { isAuthed } from "@/utils/AuthStorage";
import { Navigate } from "react-router-dom";

interface Props {
  protection?: RouteProtection;
  children: JSX.Element;
}
export function RouteGuard({ protection, children }: Props) {
  if (protection?.type === "authed") {
    if (!isAuthed()) {
      return <Navigate to={protection.redirectPath ?? "/login"} replace />;
    }
  }
  if (protection?.type === "public") {
    if (isAuthed()) {
      return <Navigate to={protection.redirectPath ?? "/"} replace />;
    }
  }
  return children;
}
