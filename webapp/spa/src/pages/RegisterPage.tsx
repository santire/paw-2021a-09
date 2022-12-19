import { Flex, Text, Title } from "@mantine/core";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import {
  createSearchParams,
  useNavigate,
  useSearchParams,
} from "react-router-dom";
import { activateUser } from "../api/services/AuthService";
import { RegisterForm } from "../components/RegisterForm/RegisterForm";
import { useAuth } from "../context/AuthContext";

export function RegisterPage() {
  const [searchParams] = useSearchParams();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { authed } = useAuth();

  useEffect(() => {
    if (authed) {
      navigate("/");
    }
    const token = searchParams.get("token");
    const activate = async (token: string) => {
      try {
        await activateUser(token);
        navigate({
          pathname: "/login",
          search: `${createSearchParams({ alert: "confirmed" })}`,
        });
      } catch (e) {
        // TODO: handle error
        navigate("/register");
      }
    };
    if (token) {
      activate(token);
    }
  }, [searchParams]);

  if (searchParams.get("pendingConfirmation")) {
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
    <Flex mt="xl" justify="center">
      <RegisterForm />
    </Flex>
  );
}
