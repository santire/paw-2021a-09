import { Suspense } from "react";
import { QueryClient, QueryClientProvider } from "react-query";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "./components/Layout/Layout";
import { ThemeProvider } from "./components/ThemeProvider/ThemeProvider";
import { ErrorPage } from "./pages/ErrorPage";
import { HomePage } from "./pages/HomePage";

const queryClient = new QueryClient();
const router = createBrowserRouter([
  
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        path: "/",
        element: <HomePage />,
      },
      {
        path: "*",
        // TODO: Change this to NotFound
        element: <ErrorPage />
      }
    ],
  },
]);

export function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Suspense fallback="loading">
        <ThemeProvider>
          <RouterProvider router={router} />
        </ThemeProvider>
      </Suspense>
    </QueryClientProvider>
  );
}
