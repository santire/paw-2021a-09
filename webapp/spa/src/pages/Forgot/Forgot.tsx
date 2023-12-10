import { zodResolver } from "@hookform/resolvers/zod";
import {
  Anchor,
  Button,
  Container,
  Flex,
  Loader,
  Paper,
  Text,
  TextInput,
  Title,
} from "@mantine/core";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { IUserForgot } from "@/types/user/user.models";
import { UserForgotSchema } from "@/types/user/user.schemas";
import { isServerError } from "@/api/client";
import { useRequestPasswordReset } from "@/hooks/user.hooks";

export function ForgotPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
    setError,
  } = useForm<IUserForgot>({
    mode: "onTouched",
    resolver: zodResolver(UserForgotSchema),
  });
  const [pending, setPending] = useState(false);

  const { mutate, isPending } = useRequestPasswordReset();

  function handleMutation(data: IUserForgot) {
    mutate(data, {
      onSuccess: () => {
        reset();
        setPending(true);
      },
      onError: ({ cause }) => {
        if (isServerError(cause) && cause.code === "not_found") {
          setError("email", {
            type: "custom",
            message: t("errors.userNotFound") || "",
          });
        }
      },
    });
  }

  if (pending) {
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
        <form onSubmit={handleSubmit((data) => handleMutation(data))}>
          <TextInput
            label={t("pages.forgot.email.label")}
            placeholder={t("pages.forgot.email.placeholder") || ""}
            required
            error={errors.email?.message}
            {...register("email")}
          />
          <Button
            fullWidth
            mt="xl"
            color="orange"
            type="submit"
            disabled={isPending}
          >
            {isPending ? (
              <Loader variant="dots" color="orange" />
            ) : (
              t("pages.forgot.submit")
            )}
          </Button>
        </form>
      </Paper>
    </Container>
  );
}
