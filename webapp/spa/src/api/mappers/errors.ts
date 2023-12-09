import { ErrorMap } from "../client";

export const apiErrorMap: ErrorMap = {
  Unauthorized: { code: "invalid_credentials" },
  TokenExpiredException: {
    code: "token_expired",
  },
  TokenDoesNotExistException: {
    code: "token_does_not_exist",
  },
};
