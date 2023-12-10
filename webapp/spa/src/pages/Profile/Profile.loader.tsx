import { queries } from "@/queries";
import { QueryClient, useQuery } from "@tanstack/react-query";
import { useLoaderData } from "react-router-dom";

export const userProfileLoader = (queryClient: QueryClient) => async () =>
  await queryClient.ensureQueryData(queries.users.detail());

export function useProfileLoaderData() {
  const initialData = useLoaderData() as Awaited<
    ReturnType<ReturnType<typeof userProfileLoader>>
  >;
  return useQuery({ ...queries.users.detail(), initialData });
}
