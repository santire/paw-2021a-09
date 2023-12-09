import { Suspense, useEffect, useState } from "react";
import { ThemeProvider } from "./context/ThemeContext";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import Layout from "@/components/Layout/Layout";
import { NotFoundPage } from "@/pages/NotFound/NotFound";
import { Helmet, HelmetProvider } from 'react-helmet-async';
import { HomePage } from "@/pages/Home/Home";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // retry: false,
      // refetchOnWindowFocus: false, // default: true
      // staleTime: Infinity,
    },
  },
});

// const basename = import.meta.env.VITE_CONTEXT || "";
const router = createBrowserRouter(
  [
    {
      element: <Layout />,
      children: [
        {
          path: "/",
          element:
            <>
              <Helmet>
                <title>Home: Gourmetable</title>
              </Helmet>
              <HomePage />
            </>
        },
        {
          path: "*",
          element: <NotFoundPage />,
        },
      ],
    },
  ],
  { basename: import.meta.env.BASE_URL }
);

function App() {
  const [, rerenderer] = useState(0);
  useEffect(() => {
    rerenderer(Math.random());
  }, []);
  return (
    <QueryClientProvider client={queryClient}>
      <Suspense fallback={<Layout />}>
        <ThemeProvider>
          <HelmetProvider>
            <Helmet>
              <title>Gourmetable</title>
            </Helmet>
            <RouterProvider router={router} />
          </HelmetProvider>
        </ThemeProvider>
      </Suspense>
    </QueryClientProvider>
  );
}

export default App;
