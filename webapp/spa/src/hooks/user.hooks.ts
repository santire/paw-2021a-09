import { UserService } from "@/api/services/UserService";
import { queries } from "@/queries";
import {
  IUserLogin,
  IUserResetPassword,
  IUserUpdate,
} from "@/types/user/user.models";
import { clearItems } from "@/utils/AuthStorage";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";

export function useUser() {
  return useQuery(queries.users.detail());
}

export function useCreateUser() {
  return useMutation({
    mutationFn: UserService.create,
  });
}

export function useUpdateUser() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ url, data }: { url: string; data: IUserUpdate }) =>
      UserService.update(url, data),
    onSuccess: (data) => {
      // Update query cache
      return queryClient.setQueryData(queries.users.detail().queryKey, data);
    },
  });
}

export function useRequestPasswordReset() {
  return useMutation({
    mutationFn: UserService.requestPasswordReset,
  });
}

export function useActivateUser() {
  const navigate = useNavigate();
  return useMutation({
    mutationFn: ({ url, token }: { url: string; token: string }) =>
      UserService.activate(url, token),
    onSuccess: () => {
      // Successfully activated, log in user
      navigate("/");
    },
  });
}
export function useResetUser() {
  const navigate = useNavigate();
  return useMutation({
    mutationFn: ({
      url,
      token,
      data,
    }: {
      url: string;
      token: string;
      data: IUserResetPassword;
    }) => UserService.resetPassword(url, token, data),
    onSuccess: () => {
      // Successfully activated, log in user
      navigate("/");
    },
  });
}

export function useLoginUser() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: IUserLogin) => UserService.getByEmail(data.email, data),
    onSuccess: async (data) => {
      // TODO: instead if invalidating, update queries with likes
      await queryClient.invalidateQueries();
      return queryClient.setQueryData(queries.users.detail().queryKey, data);
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
