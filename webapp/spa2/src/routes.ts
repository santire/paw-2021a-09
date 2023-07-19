import { createBrowserRouter } from "react-router-dom";
import { LoginPage } from "./pages/Login";
const routes = [
  {
    name: "login",
    title: "Login page",
    component: LoginPage,
    path: "/login",
    isPublic: true,
  },
];
