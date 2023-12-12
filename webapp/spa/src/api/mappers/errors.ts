import { ErrorMap } from "../client";

export const apiErrorMap: ErrorMap = {
  Unauthorized: { code: "invalid_credentials" },
  TokenExpiredException: {
    code: "token_expired",
  },
  TokenDoesNotExistException: {
    code: "token_does_not_exist",
  },
  ConstraintViolationException: { code: "validation_error" },
  PersistenceException: {
    code: "validation_error",
    errors: [{ subject: "username", message: "username in use" }],
  },
  UsernameInUseException: {
    code: "validation_error",
    errors: [{ subject: "username", message: "username in use" }],
  },
  AccessDeniedException: { code: "access_denied" },
  AlreadyLikedException: { code: "already_liked" },
  LikeNotFound: { code: "like_not_found" },
  InvalidImageException: { code: "invalid_image" },
};
