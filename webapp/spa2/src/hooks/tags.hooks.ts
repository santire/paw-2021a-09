import { useQuery } from "react-query";
import { TagService } from "../api/services/TagsService";

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
