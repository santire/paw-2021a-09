import { zodResolver } from "@hookform/resolvers/zod";
import {
  Alert,
  Anchor,
  Button,
  Container,
  Group,
  Paper,
  PasswordInput,
  Text,
  TextInput,
  Title,
} from "@mantine/core";
import { IconAlertCircle, IconCheck } from "@tabler/icons";
import axios from "axios";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import * as z from "zod";
import { login } from "../api/services/AuthService";
import { useAuth } from "../context/AuthContext";

const loginSchema = z.object({
  email: z.string().email().min(2),
  password: z.string().min(6),
});
type ILogin = z.infer<typeof loginSchema>;

export function LoginPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    reset,
    resetField,
    formState: { errors },
  } = useForm<ILogin>({ mode: "onBlur", resolver: zodResolver(loginSchema) });
  const [showAlert, setShowAlert] = useState("");
  const { authed } = useAuth();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    if (authed) {
      navigate("/");
    }
    const alert = searchParams.get("alert") ?? "";
    if (["", "confirmed", "error", "updated"].includes(alert)) {
      setShowAlert(alert);
    }
  }, [authed, setShowAlert, searchParams]);

  const processForm = async (data: ILogin) => {
    try {
      await login(data.email, data.password);
      reset();
      navigate("/");
    } catch (e) {
      if (axios.isAxiosError(e) && e.response?.status === 401) {
        setShowAlert("error");
        resetField("password");
      }
    }
  };

  // TODO: map form error type to i18n messages

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
        icon={<IconAlertCircle size={16} />}
        mt={30}
        color="red"
        withCloseButton
        hidden={showAlert !== "error"}
        onClose={() => setShowAlert("")}
      >
        {t("pages.login.alert")}
      </Alert>
      <Alert
        icon={<IconCheck size={16} />}
        mt={30}
        color="green"
        withCloseButton
        hidden={showAlert !== "confirmed" && showAlert !== "updated"}
        onClose={() => setShowAlert("")}
      >
        {showAlert === "confirmed"
          ? t("pages.login.confirmAlert")
          : t("pages.login.updatedAlert")}
      </Alert>
      <Paper withBorder shadow="md" p={30} mt={showAlert ? 10 : 30} radius="md">
        <form onSubmit={handleSubmit(processForm)}>
          <TextInput
            label={t("pages.login.email.label")}
            placeholder={t("pages.login.email.placeholder") || ""}
            required
            error={errors.email?.message}
            {...register("email")}
          />
          <PasswordInput
            label={t("pages.login.password.label")}
            placeholder={t("pages.login.password.placeholder") || ""}
            error={errors.password?.message}
            required
            mt="md"
            {...register("password")}
          />
          <Group position="apart" mt="lg">
            {/* <Checkbox */}
            {/*   label={t("pages.login.rememberMe.label")} */}
            {/*   sx={{ lineHeight: 1 }} */}
            {/* /> */}
            <Anchor
              onClick={() => navigate("/forgot")}
              size="sm"
              color="orange"
            >
              {t("pages.login.password.forgot")}
            </Anchor>
          </Group>
          <Button fullWidth mt="xl" color="orange" type="submit">
            {t("pages.login.signIn")}
          </Button>
        </form>
      </Paper>
    </Container>
  );
}
