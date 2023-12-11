import { TagService } from "@/api/services/TagsService";
import { useQuery } from "@tanstack/react-query";

const tagKeys = {
  all: ["tags"] as const,
};

export function useGetTags() {
  return useQuery({
    queryKey: tagKeys.all,
    queryFn: () => TagService.getAll(),
    staleTime: Infinity,
  });
}
