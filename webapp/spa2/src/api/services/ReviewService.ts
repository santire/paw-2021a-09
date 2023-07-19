import { MAX_PAGE_AMOUNT, Page, PageParams } from "../../types/page";
import { IReview, IReviewRegister } from "../../types/review/review.models";
import { apiClient, apiErrorHandler } from "../client";
import { pagedResponse } from "../utils";

const PATH = (id: number) => `/restaurants/${id}/reviews`;

interface DeleteReviewParams {
  restaurantId: number;
  reviewId: number;
}

interface CreateReviewParams {
  restaurantId: number;
  review: IReviewRegister;
}
interface IReviewService {
  create(params: CreateReviewParams): Promise<IReview>;
  deleteReview(params: DeleteReviewParams): Promise<void>;
  getAll(restaurantId: number, params: PageParams): Promise<Page<IReview[]>>;
}

module ReviewServiceImpl {
  export async function getAll(restaurantId: number, params: PageParams) {
    if ((params?.pageAmount ?? 0) > MAX_PAGE_AMOUNT) {
      params.pageAmount = MAX_PAGE_AMOUNT;
    }
    try {
      const response = await apiClient.get<IReview[]>(PATH(restaurantId), {
        params: params,
      });
      return pagedResponse(response);
    } catch (error) {
      throw new Error("General Error", {
        cause: apiErrorHandler(error),
      });
    }
  }
  export async function deleteReview({
    restaurantId,
    reviewId,
  }: DeleteReviewParams) {
    try {
      const response = await apiClient.delete(
        PATH(restaurantId) + `/${reviewId}`
      );

      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
        }),
      });
    }
  }

  export async function create({ restaurantId, review }: CreateReviewParams) {
    try {
      const response = await apiClient.post<IReview>(
        PATH(restaurantId),
        review
      );
      return response.data;
    } catch (error) {
      throw new Error("Authorization Error", {
        cause: apiErrorHandler(error, {
          Unauthorized: { code: "invalid_credentials" },
          AccessDeniedException: { code: "access_denied" },
        }),
      });
    }
  }
}

const ReviewService: IReviewService = ReviewServiceImpl;
export { ReviewService };
