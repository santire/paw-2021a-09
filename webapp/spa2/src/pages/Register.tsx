import { Flex, Title, Text, Alert } from "@mantine/core";
import { useTranslation } from "react-i18next";
import {
  useNavigate,
  useSearchParams,
  createSearchParams,
} from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { useEffect, useState } from "react";
import { useActivateUser, useLoginUser } from "../hooks/user.hooks";
import { IconAlertCircle } from "@tabler/icons-react";
import { RegisterForm } from "../components/RegisterForm/RegisterForm";

export function RegisterPage() {
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth();
  const [error, setError] = useState("");
  const [pending, setPending] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();
  const { mutate } = useActivateUser({
    onSuccess: () => {
      navigate({
        pathname: "/login",
        search: `${createSearchParams({ alert: "confirmed" })}`,
      });
    },
    onError: (err) => {
      if (err.code === "token_expired") {
        setError("errors.tokenExpired");
        setSearchParams("")
      }
      if (err.code === "token_does_not_exist") {
        setError("errors.tokenDoesNotExist");
        setSearchParams("")
      }
    },
  });

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/");
    }
    const token = searchParams.get("token");
    if (token) {
      mutate(token);
    }
  }, []);

  if (pending) {
    return (
      <Flex mt="xl" justify="center" direction="column" align="center">
        <Title mt="xl" mb="md">
          {t("pages.register.confirmation.title")}
        </Title>
        <Text size="xl">{t("pages.register.confirmation.subtitle")}</Text>
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
