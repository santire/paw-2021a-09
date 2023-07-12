import { zodResolver } from "@hookform/resolvers/zod";
import {
  Alert,
  Anchor,
  Button,
  Container,
  Flex,
  Loader,
  Paper,
  PasswordInput,
  Text,
  Title,
} from "@mantine/core";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import {
  createSearchParams,
  useNavigate,
  useSearchParams,
} from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { IUserResetPassword } from "../types/user/user.models";
import { UserResetPasswordSchema } from "../types/user/user.schemas";
import { usePasswordReset } from "../hooks/user.hooks";
import { IconAlertCircle } from "@tabler/icons-react";

export function ResetPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [searchParams, setSearchParams] = useSearchParams();
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<IUserResetPassword>({
    mode: "onTouched",
    resolver: zodResolver(UserResetPasswordSchema),
  });
  const { isAuthenticated } = useAuth();
  const { mutate, isLoading } = usePasswordReset({
    onSuccess: () => {
      navigate({
        pathname: "/login",
        search: `${createSearchParams({ alert: "updated" })}`,
      });
    },
    onError: (err) => {
      if (err.code === "token_expired") {
        setError("errors.tokenExpired");
        setSearchParams("");
      }
      if (err.code === "token_does_not_exist") {
        setError("errors.tokenDoesNotExist");
        setSearchParams("");
      }
    },
  });

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/");
    }
  }, []);

  if (searchParams.get("token")) {
    const token = searchParams.get("token") || "";
    return (
      <Container size={420} my={40}>
        <Title
          align="center"
          sx={(theme) => ({
            fontFamily: `Greycliff CF, ${theme.fontFamily}`,
            fontWeight: 900,
          })}
        >
          {t("pages.reset.title")}
        </Title>
        <Text color="dimmed" size="sm" align="center" my={5}>
          {t("pages.forgot.account.title")}{" "}
          <Anchor
            size="sm"
            color="orange"
            onClick={() => navigate("/register")}
          >
            {t("pages.forgot.account.prompt")}
          </Anchor>
        </Text>

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

        <Paper withBorder shadow="md" p={30} mt={30} radius="md">
          <form
            onSubmit={handleSubmit((data) => mutate({ token: token, ...data }))}
          >
            <PasswordInput
              label={t("pages.reset.password.label")}
              placeholder={t("pages.login.password.placeholder") || ""}
              error={errors.password?.message}
              required
              mt="md"
              {...register("password")}
            />
            <PasswordInput
              label={t("pages.reset.confirmPassword.label")}
              placeholder={t("pages.reset.confirmPassword.placeholder") || ""}
              error={
                errors.repeatPassword
                  ? t(errors.repeatPassword.message || "") ||
                    "The passwords do not match"
                  : null
              }
              required
              mt="md"
              {...register("repeatPassword")}
            />
            <Button
              fullWidth
              mt="xl"
              color="orange"
              type="submit"
              disabled={isLoading}
            >
              {isLoading ? (
                <Loader variant="dots" color="orange" />
              ) : (
                t("pages.reset.submit")
              )}
            </Button>
          </form>
        </Paper>
      </Container>
    );
  }

  return (
    <Flex mt="xl" justify="center" direction="column" align="center">
      <Paper withBorder shadow="md" p={30} mt={30} radius="md">
        <Title mt="xl" mb="md">
          {t("pages.reset.error.title")}
        </Title>
        <Text size="xl">{t(error) || t("pages.reset.error.subtitle")}</Text>

        <Button
          fullWidth
          mt="xl"
          color="orange"
          type="submit"
          onClick={() => navigate("/forgot")}
        >
          {t("error.takeMeBackForgot")}
        </Button>
      </Paper>
    </Flex>
  );
}
