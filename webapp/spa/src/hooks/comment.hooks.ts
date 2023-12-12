import { CommentService } from "@/api/services/CommentService";
import { queries } from "@/queries";
import { IComment, ICommentRegister } from "@/types/comment/comment.models";
import { PageParams } from "@/types/page";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

export function useGetRestaurantComments(
  restaurantId: number,
  params: PageParams,
) {
  return useQuery({
    queryKey: queries.comments.list(restaurantId, params).queryKey,
    queryFn: () => CommentService.getAll(restaurantId, params),
  });
}

export function useCreateComment() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      userId,
      restaurantId,
      comment,
    }: {
      userId: number;
      restaurantId: number;
      comment: ICommentRegister;
    }) => CommentService.create(userId, restaurantId, comment),
    onSuccess: (comment, { restaurantId }) => {
      queryClient.invalidateQueries({
        queryKey: [...queries.comments.list._def, restaurantId],
      });
      queryClient.setQueryData(
        queries.comments.detail(comment.id).queryKey,
        comment,
      );
    },
  });
}

export function useDeleteComment(restaurantId: number) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ comment }: { comment: IComment }) =>
      CommentService.remove(comment.self),
    onSuccess: (_, { comment }) => {
      queryClient.invalidateQueries({
        queryKey: [...queries.comments.list._def, restaurantId],
      });
      queryClient.invalidateQueries(queries.comments.detail(comment.id));
    },
  });
}
