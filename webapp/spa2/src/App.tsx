import { Suspense } from "react";
import { AuthContextProvider } from "./context/AuthContext";
import { ThemeProvider } from "./context/ThemeContext";
import { QueryClient, QueryClientProvider } from "react-query";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import Layout from "./components/Layout/Layout";
import { NotFoundPage } from "./pages/NotFound";
import { LoginPage } from "./pages/Login";
import { RegisterPage } from "./pages/Register";
import { ForgotPage } from "./pages/Forgot";
import { ResetPage } from "./pages/Reset";

const queryClient = new QueryClient();

const basename = import.meta.env.VITE_CONTEXT || "";
const router = createBrowserRouter(
  [
    {
      path: "/",
      element: <Layout />,
      children: [
        {
          path: "/login",
          element: <LoginPage />,
        },
        {
          path: "/register",
          element: <RegisterPage />,
        },
        {
          path: "/forgot",
          element: <ForgotPage />,
        },
        {
          path: "/reset",
          element: <ResetPage />,
        },
        {
          path: "*",
          element: <NotFoundPage />,
        },
      ],
    },
  ],
  { basename: basename }
);

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Suspense fallback={<Layout />}>
        <ThemeProvider>
          <AuthContextProvider>
            <RouterProvider router={router} />
          </AuthContextProvider>
        </ThemeProvider>
      </Suspense>
    </QueryClientProvider>
  );
}

export default App;
