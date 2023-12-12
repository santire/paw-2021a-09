import { CommentService } from "@/api/services/CommentService";
import { PageParams } from "@/types/page";
import { createQueryKeys } from "@lukemorales/query-key-factory";

export const comments = createQueryKeys("comments", {
  all: null,
  list: (restaurantId: number, params: PageParams) => ({
    queryKey: [restaurantId, { params }],
    queryFn: () => CommentService.getAll(restaurantId, params),
  }),
  detail: (id: number) => ({
    queryKey: [id],
    queryFn: () => CommentService.get(id),
  }),
});
