import { Flex, Title, Text, Alert } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { IconAlertCircle } from "@tabler/icons-react";
import { RegisterForm } from "@/components/RegisterForm/RegisterForm";
import { isServerError } from "@/api/client";
import { useActivateUser } from "@/hooks/queries/users";

export function RegisterPage() {
  const { t } = useTranslation();
  const [error, setError] = useState("");
  const [pending, setPending] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();
  const { mutate } = useActivateUser();
  const navigate = useNavigate();

  function handleMutation(url: string, token: string) {
    mutate(
      { url, token },
      {
        onError: ({ cause }) => {
          if (isServerError(cause) && cause.code === "token_expired") {
            setError("errors.tokenExpired");
            setSearchParams("");
            navigate("/register", { replace: true });
          }
          if (isServerError(cause) && cause.code === "token_does_not_exist") {
            setError("errors.tokenDoesNotExist");
            setSearchParams("");
            navigate("/register", { replace: true });
          }
        },
      },
    );
  }

  useEffect(() => {
    const token = searchParams.get("token");
    const url = searchParams.get("patchUrl");
    if (token && url) {
      handleMutation(url, token);
    }
  }, []);

  if (pending) {
    return (
      <Flex mt="xl" justify="center" direction="column" align="center">
        <Title mt="xl" mb="md">
          {t("pages.register.confirmation.title")}
        </Title>
        <Text size="xl" aria-label="confirmation-text">
          {t("pages.register.confirmation.subtitle")}
        </Text>
      </Flex>
    );
  }
  return (
    <Flex mt="xl" justify="center" direction="column" align="center">
      <Alert
        icon={<IconAlertCircle size={16} />}
        mb={40}
        px={20}
        w={500}
        color="red"
        withCloseButton
        hidden={!error}
        onClose={() => setError("")}
      >
        {t(error)}
      </Alert>
      <RegisterForm setPending={setPending} />
    </Flex>
  );
}
