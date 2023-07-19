import { useSearchParams } from "react-router-dom";
import { ServerError, isServerError } from "../api/client";
import { Page, PageParams } from "../types/page";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { ReviewService } from "../api/services/ReviewService";
import { IReview } from "../types/review/review.models";

interface QueryOptions {
  onSuccess?: () => void;
  onError?: (error: ServerError) => void;
}

const reviewKeys = {
  all: ["reviews"] as const,
  lists: (restaurantId: number) =>
    [...reviewKeys.all, "list", restaurantId] as const,
  list: (restaurantId: number, filters?: PageParams) =>
    [...reviewKeys.lists(restaurantId), { filters }] as const,
};

export function useGetReviews(restaurantId: number, params: PageParams) {
  const [searchParams, setSearchParams] = useSearchParams();
  return useQuery<Page<IReview[]>>({
    queryKey: reviewKeys.list(restaurantId, params),
    queryFn: () => ReviewService.getAll(restaurantId, params!),
    onSuccess: (data) => {
      const page = parseInt(searchParams.get("page") || "NaN");
      // If page is invalid resets back to first
      if (page > data.meta.maxPages) {
        searchParams.delete("page");
        setSearchParams(searchParams);
      }
    },
  });
}

export function useCreateReview(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ReviewService.create,
    onSuccess: (_, { restaurantId }) => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(reviewKeys.lists(restaurantId));
      if (options?.onSuccess) {
        options.onSuccess();
      }
    },
    onError: ({ cause }) => {
      if (isServerError(cause) && options?.onError) {
        options.onError(cause);
      } else {
        console.log("An error here shouldn't be happening");
      }
    },
  });
}

export function useDeleteReview(options?: QueryOptions) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ReviewService.deleteReview,
    onSuccess: (_, { restaurantId }) => {
      // Invalidate because might change page order
      queryClient.invalidateQueries(reviewKeys.lists(restaurantId));
      if (options?.onSuccess) {
        options.onSuccess();
      }
    },

    onError: ({ cause }) => {
      if (isServerError(cause) && options?.onError) {
        options.onError(cause);
      } else {
        console.log("An error here shouldn't be happening");
      }
    },
  });
}
