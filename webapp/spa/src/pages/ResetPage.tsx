import { zodResolver } from "@hookform/resolvers/zod";
import {
  Anchor,
  Button,
  Container,
  Flex,
  Paper,
  PasswordInput,
  Text,
  Title,
} from "@mantine/core";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import {
  createSearchParams,
  useNavigate,
  useSearchParams,
} from "react-router-dom";
import * as z from "zod";
import { resetPassword } from "../api/services/AuthService";
import { useAuth } from "../context/AuthContext";

const forgotSchema = z
  .object({
    password: z.string().min(2).max(100),
    confirmPassword: z.string().min(2).max(100),
  })
  .superRefine(({ password, confirmPassword }, ctx) => {
    if (confirmPassword !== password) {
      ctx.addIssue({
        path: ["confirmPassword"],
        code: "custom",
        message: "The passwords do not match",
      });
    }
  });
type ForgotForm = z.infer<typeof forgotSchema>;

export function ResetPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ForgotForm>({
    mode: "onTouched",
    resolver: zodResolver(forgotSchema),
  });
  const { authed } = useAuth();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    if (authed) {
      navigate("/");
    }
  }, []);

  const processForm = async (data: ForgotForm) => {
    try {
      await resetPassword(searchParams.get("token")!, data.password);
      navigate({
        pathname: "/login",
        search: `${createSearchParams({ alert: "updated" })}`,
      });
    } catch (e) {
      console.error(e);
    }
  };

  if (searchParams.get("token")) {
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

        <Paper withBorder shadow="md" p={30} mt={30} radius="md">
          <form onSubmit={handleSubmit(processForm)}>
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
              error={errors.confirmPassword?.message}
              required
              mt="md"
              {...register("confirmPassword")}
            />
            <Button fullWidth mt="xl" color="orange" type="submit">
              {t("pages.reset.submit")}
            </Button>
          </form>
        </Paper>
      </Container>
    );
  }

  return (
    <Flex mt="xl" justify="center" direction="column" align="center">
      <Title mt="xl" mb="md">
        {t("pages.reset.error.title")}
      </Title>
      <Text size="xl">{t("pages.reset.error.subtitle")}</Text>
    </Flex>
  );
}
