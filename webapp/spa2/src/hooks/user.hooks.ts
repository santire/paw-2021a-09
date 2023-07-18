import { useMutation, useQuery, useQueryClient } from "react-query";
import { AuthService } from "../api/services/AuthService";
import { useAuth } from "./useAuth";
import { ServerError, isServerError } from "../api/client";
import { UserService } from "../api/services/UserService";
import { IRestaurant } from "../types/restaurant/restaurant.models";
import { IUserUpdate } from "../types/user/user.models";

const userKeys = {
  all: ["users"] as const,
  details: () => [...userKeys.all, "detail"] as const,
  detail: (id: number) => [...userKeys.details(), id] as const,
};

interface QueryOptions {
  onSuccess?: () => void;
  onError?: (error: ServerError) => void;
}

export function useGetUser() {
  const { isAuthenticated, userId } = useAuth();

  return useQuery({
    queryKey: userKeys.details(),
    queryFn: () => UserService.getById(userId),
    staleTime: Infinity,
    enabled: isAuthenticated,
  });
}

export function useUpdateUser(options?: QueryOptions) {
  const { userId } = useAuth();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: IUserUpdate) => UserService.update(userId, data),
    onSuccess: () => {
      queryClient.invalidateQueries(userKeys.details());
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

export function useLoginUser(options?: QueryOptions) {
  const { setCredentials } = useAuth();

  return useMutation({
    mutationFn: AuthService.login,
    onSuccess: (data) => {
      setCredentials(data.token, data.userId);
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

export function useActivateUser(options?: QueryOptions) {
  return useMutation({
    mutationFn: AuthService.activate,
    onSuccess: () => {
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

export function useRequestPasswordReset(options?: QueryOptions) {
  return useMutation({
    mutationFn: AuthService.requestPasswordReset,
    onSuccess: () => {
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

export function usePasswordReset(options?: QueryOptions) {
  return useMutation({
    mutationFn: AuthService.resetPassword,
    onSuccess: () => {
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

export function useCreateUser(options?: QueryOptions) {
  return useMutation({
    mutationFn: UserService.create,
    onSuccess: () => {
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

type isOwnerProps = {
  restaurant: IRestaurant;
};
export function useIsOwner({ restaurant }: isOwnerProps) {
  const { userId } = useAuth();
  const { owner } = restaurant;
  const rid = parseInt(owner.substring(owner.lastIndexOf("/") + 1));
  return userId === rid;
}
