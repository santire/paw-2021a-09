import { Suspense } from "react";
import { ThemeProvider } from "./context/ThemeContext";
import {
  MutationCache,
  QueryCache,
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { RouterProvider } from "react-router-dom";
import Layout from "@/components/Layout/Layout";
import { Helmet, HelmetProvider } from "react-helmet-async";
import { GourmetableRouter } from "./router/GourmetableRouter";

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: () => {
      console.log("GLOBAL QUERY ERROR HANDLING WOULD GO HERE");
    },
  }),
  mutationCache: new MutationCache({
    onError: () => {
      console.log("GLOBAL MUTATION ERROR HANDLING WOULD GO HERE");
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

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ReactQueryDevtools initialIsOpen={false} />
      <Suspense fallback={<Layout />}>
        <ThemeProvider>
          <HelmetProvider>
            <Helmet>
              <title>Gourmetable</title>
            </Helmet>
            <RouterProvider router={GourmetableRouter} />
          </HelmetProvider>
        </ThemeProvider>
      </Suspense>
    </QueryClientProvider>
  );
}

export default App;
