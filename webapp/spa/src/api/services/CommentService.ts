import { IComment, ICommentRegister } from "@/types/comment/comment.models";
import { Page, PageParams } from "@/types/page";
import { apiClient } from "../client";
import { pagedResponse } from "../utils";

const PATH = "/comments";
interface ICommentService {
  getAll(restaurantId: number, params: PageParams): Promise<Page<IComment[]>>;
  get(commentId: number): Promise<IComment>;
  create(
    userId: number,
    restaurantId: number,
    comment: ICommentRegister,
  ): Promise<IComment>;
  remove(url: string): Promise<void>;
}

module CommentServiceImpl {
  export async function getAll(restaurantId: number, params: PageParams) {
    const response = await apiClient.get<IComment[]>(PATH, {
      params: { ...params, madeTo: restaurantId },
    });

    return pagedResponse(response);
  }

  export async function get(commentId: number) {
    const response = await apiClient.get<IComment>(`${PATH}/${commentId}`);
    return response.data;
  }

  export async function create(
    userId: number,
    restaurantId: number,
    comment: ICommentRegister,
  ) {
    const response = await apiClient.post(PATH, {
      userId,
      restaurantId,
      message: comment.message,
    });

    return response.data;
  }

  export async function remove(url: string) {
    const response = await apiClient.delete(url);
    return response.data;
  }
}

const CommentService: ICommentService = CommentServiceImpl;
export { CommentService };
