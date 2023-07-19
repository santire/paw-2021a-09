import {
  Alert,
  Anchor,
  Button,
  Container,
  Group,
  Loader,
  Paper,
  PasswordInput,
  Text,
  TextInput,
  Title,
} from "@mantine/core";
import { IconCheck } from "@tabler/icons-react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import { IUserLogin } from "../types/user/user.models";
import { zodResolver } from "@hookform/resolvers/zod";
import { UserLoginSchema } from "../types/user/user.schemas";
import { useAuth } from "../hooks/useAuth";
import { ServerError } from "../api/client";
import { useLoginUser } from "../hooks/user.hooks";
import { useEffect, useState } from "react";

export function LoginPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [showAlert, setShowAlert] = useState("");
  const [searchParams, setSearchParams] = useSearchParams();
  const {
    register,
    handleSubmit,
    resetField,
    setError,
    formState: { errors },
  } = useForm<IUserLogin>({
    mode: "onTouched",
    resolver: zodResolver(UserLoginSchema),
  });

  const { mutate, isLoading } = useLoginUser({
    onError: handleError,
  });

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/");
    }
    const alert = searchParams.get("alert") ?? "";
    if (["", "confirmed", "updated"].includes(alert)) {
      setShowAlert(alert);
    }
  }, [isAuthenticated, setShowAlert, searchParams]);

  function handleError(error: ServerError) {
    if (error.code === "invalid_credentials") {
      resetField("password");
      setError("email", {
        type: "custom",
        message: t("errors.invalidCredentials") || "",
      });
    } else {
      // Unknown error notification
    }
  }

  return (
    <Container size={420} my={40}>
      <Title
        align="center"
        sx={(theme) => ({
          fontFamily: `Greycliff CF, ${theme.fontFamily}`,
          fontWeight: 900,
        })}
      >
        {t("pages.login.welcomeBack")}
      </Title>
      <Text color="dimmed" size="sm" align="center" my={5}>
        {t("pages.login.createAccount.title")}{" "}
        <Anchor size="sm" color="orange" onClick={() => navigate("/register")}>
          {t("pages.login.createAccount.prompt")}
        </Anchor>
      </Text>

      <Alert
        icon={<IconCheck size={16} />}
        mt={30}
        color="green"
        withCloseButton
        hidden={showAlert !== "confirmed" && showAlert !== "updated"}
        onClose={() => {
          setShowAlert("");
          setSearchParams("");
        }}
      >
        {showAlert === "confirmed"
          ? t("pages.login.confirmAlert")
          : t("pages.login.updatedAlert")}
      </Alert>
      <Paper withBorder shadow="md" p={30} mt={showAlert ? 10 : 30} radius="md">
        <form onSubmit={handleSubmit((data) => mutate(data))}>
          <TextInput
            aria-label="email-input"
            label={t("pages.login.email.label")}
            placeholder={t("pages.login.email.placeholder") || ""}
            required
            error={errors.email?.message}
            {...register("email")}
          />
          <PasswordInput
            aria-label="pass-input"
            label={t("pages.login.password.label")}
            placeholder={t("pages.login.password.placeholder") || ""}
            error={errors.password?.message}
            required
            mt="md"
            {...register("password")}
          />
          <Group position="apart" mt="lg">
            <Anchor
              onClick={() => navigate("/forgot")}
              size="sm"
              color="orange"
            >
              {t("pages.login.password.forgot")}
            </Anchor>
          </Group>
          <Button
            fullWidth
            mt="xl"
            color="orange"
            type="submit"
            aria-label="login-button"
            disabled={isLoading}
          >
            {isLoading ? (
              <Loader variant="dots" color="orange" />
            ) : (
              t("pages.login.signIn")
            )}
          </Button>
        </form>
      </Paper>
    </Container>
  );
}
