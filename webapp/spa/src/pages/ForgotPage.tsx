import { zodResolver } from "@hookform/resolvers/zod";
import {
  Anchor,
  Button,
  Container,
  Flex,
  Paper,
  Text,
  TextInput,
  Title,
} from "@mantine/core";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import * as z from "zod";
import { resetUser } from "../api/services/AuthService";
import { useAuth } from "../context/AuthContext";

const forgotSchema = z.object({
  email: z.string().email().min(2),
});
type ForgotForm = z.infer<typeof forgotSchema>;

export function ForgotPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ForgotForm>({
    mode: "onTouched",
    resolver: zodResolver(forgotSchema),
  });
  const { authed } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();

  useEffect(() => {
    if (authed) {
      navigate("/");
    }
  }, []);

  const processForm = async (data: ForgotForm) => {
    try {
      await resetUser(data.email);
      reset();
      setSearchParams({ pendingConfirmation: "true" });
    } catch (e) {
      console.error(e);
    }
  };

  if (searchParams.get("pendingConfirmation")) {
    return (
      <Flex mt="xl" justify="center" direction="column" align="center">
        <Title mt="xl" mb="md">
          {t("pages.forgot.confirmation.title")}
        </Title>
        <Text size="xl">{t("pages.forgot.confirmation.subtitle")}</Text>
      </Flex>
    );
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
        {t("pages.forgot.title")}
      </Title>
      <Text color="dimmed" size="sm" align="center" my={5}>
        {t("pages.forgot.account.title")}{" "}
        <Anchor size="sm" color="orange" onClick={() => navigate("/login")}>
          {t("pages.forgot.account.prompt")}
        </Anchor>
      </Text>

      <Paper withBorder shadow="md" p={30} mt={30} radius="md">
        <form onSubmit={handleSubmit(processForm)}>
          <TextInput
            label={t("pages.forgot.email.label")}
            placeholder={t("pages.forgot.email.placeholder") || ""}
            required
            error={errors.email?.message}
            {...register("email")}
          />
          <Button fullWidth mt="xl" color="orange" type="submit">
            {t("pages.forgot.submit")}
          </Button>
        </form>
      </Paper>
    </Container>
  );
}
