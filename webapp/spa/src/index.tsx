import { MantineProvider } from "@mantine/core";
import React, { Suspense } from "react";
import ReactDOM from "react-dom/client";
import { QueryClient, QueryClientProvider } from "react-query";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./i18n";
import { HomePage } from "./pages/HomePage";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
const queryClient = new QueryClient();
const router = createBrowserRouter([
  {
    path: "/",
    element: <HomePage />,
  },
]);

root.render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <MantineProvider withGlobalStyles withNormalizeCSS>
        <Suspense fallback="loading">
          <RouterProvider router={router} />
        </Suspense>
      </MantineProvider>
    </QueryClientProvider>
  </React.StrictMode>
);
