import { UserService } from "@/api/services/UserService";
import { IUserLogin } from "@/types/user/user.models";
import { clearItems, getUserEmail } from "@/utils/AuthStorage";
import { createQueryKeys } from "@lukemorales/query-key-factory";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
export const users = createQueryKeys("users", {
  all: null,
  detail: () => ({
    queryKey: ["email"],
  }),
});

export function useUser() {
  const email = getUserEmail() ?? "";
  return useQuery({
    queryKey: users.detail().queryKey,
    queryFn: () => UserService.getByEmail(email),
  });
}

export function useLoginUser() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: IUserLogin) => UserService.getByEmail(data.email, data),
    onSuccess: (data) => {
      return queryClient.setQueryData(users.detail().queryKey, data);
    },
  });
}

export function useLogoutUser() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  return async () => {
    clearItems();
    await queryClient.invalidateQueries();
    queryClient.clear();
    navigate("/", { replace: true });
  };
}
