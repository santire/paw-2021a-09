import { Suspense } from "react";
import { ThemeProvider } from "./context/ThemeContext";
import {
  MutationCache,
  QueryCache,
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import Layout from "@/components/Layout/Layout";
import { Helmet, HelmetProvider } from "react-helmet-async";
import { elementRouter } from "./router/elementRoutes";
import { RestaurantFilterAndPageParamsProvider } from "./context/RestaurantFilterAndPageContext";
import { NotificationsProvider } from "@mantine/notifications";

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: ({ cause }) => {
      // console.log(cause);
    },
  }),
  mutationCache: new MutationCache({
    onError: () => {
      // console.log("GLOBAL MUTATION ERROR HANDLING WOULD GO HERE");
    },
  }),
  defaultOptions: {
    queries: {
      // staleTime: 2 * 60 * 1000,  // 2min
      retry: false,
      staleTime: Infinity,
    },
  },
});

const GourmetableRouter = createBrowserRouter(
  [
    {
      element: (
          <Layout />
      ),
      children: elementRouter(queryClient),
    },
  ],
  { basename: import.meta.env.BASE_URL },
);

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ReactQueryDevtools initialIsOpen={false} />
      <Suspense fallback={<Layout />}>
        <ThemeProvider>
          <NotificationsProvider>
            <HelmetProvider>
              <Helmet>
                <title>Gourmetable</title>
              </Helmet>
              <RouterProvider router={GourmetableRouter} />
            </HelmetProvider>
          </NotificationsProvider>
        </ThemeProvider>
      </Suspense>
    </QueryClientProvider>
  );
}

export default App;
