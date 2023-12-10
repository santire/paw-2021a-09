import {
  Button,
  Divider,
  Group,
  Loader,
  Paper,
  PasswordInput,
  SimpleGrid,
  Text,
  TextInput,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import useStyles from "./RegisterForm.styles";
import { useForm } from "react-hook-form";
import { IUserRegister } from "../../types/user/user.models";
import { zodResolver } from "@hookform/resolvers/zod";
import { UserRegisterSchema } from "@/types/user/user.schemas";
import { useCreateUser } from "@/hooks/queries/users";
import { Dispatch, SetStateAction } from "react";
import { isServerError } from "@/api/client";
interface RegisterFormProps {
  setPending: Dispatch<SetStateAction<boolean>>;
}
export function RegisterForm({ setPending }: RegisterFormProps) {
  const { classes } = useStyles();
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
    setError,
  } = useForm<IUserRegister>({
    mode: "onTouched",
    resolver: zodResolver(UserRegisterSchema),
  });

  const { mutate, isPending } = useCreateUser();

  function handleMutation(data: IUserRegister) {
    mutate(data, {
      onSuccess: () => {
        reset();
        setPending(true);
      },
      onError: ({ cause }) => {
        if (
          isServerError(cause) &&
          cause.code === "validation_error" &&
          cause.errors
        ) {
          for (const e of cause.errors) {
            switch (e.subject) {
              case "email":
                setError("email", {
                  type: "custom",
                  message: t("pages.register.email.taken") || "",
                });
                break;

              case "username":
                setError("username", {
                  type: "custom",
                  message: t("pages.register.username.taken") || "",
                });
                break;

              default:
                // todo: show default alert?
                console.error(
                  "unexpected error subject: ",
                  e.subject,
                  e.message,
                );
            }
          }
        }
      },
    });
  }

  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <form
          className={classes.form}
          onSubmit={handleSubmit((data) => handleMutation(data))}
        >
          <Text className={classes.title} px="sm" mt="sm" mb="xl">
            {t("pages.register.title")}
          </Text>
          <div className={classes.fields}>
            <Divider my="xs" label={t("pages.register.loginDivider")} />
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.register.email.label")}
                aria-label="email-input"
                placeholder={t("pages.register.email.placeholder") || ""}
                required
                error={errors.email?.message}
                {...register("email")}
              />
              <TextInput
                label={t("pages.register.username.label")}
                aria-label="username-input"
                placeholder={t("pages.register.username.placeholder") || ""}
                required
                error={errors.username?.message}
                {...register("username")}
              />
            </SimpleGrid>
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <PasswordInput
                label={t("pages.register.password.label")}
                aria-label="pass-input"
                placeholder={t("pages.register.password.placeholder") || ""}
                required
                error={errors.password?.message}
                {...register("password")}
              />
              <PasswordInput
                label={t("pages.register.confirmPassword.label")}
                aria-label="repeat-input"
                placeholder={
                  t("pages.register.confirmPassword.placeholder") || ""
                }
                required
                error={
                  errors.repeatPassword
                    ? t(errors.repeatPassword.message || "") ||
                      "The passwords do not match"
                    : null
                }
                {...register("repeatPassword")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.register.contactDivider")} />
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.register.firstName.label")}
                aria-label="first-name-input"
                placeholder={t("pages.register.firstName.placeholder") || ""}
                required
                error={errors.firstName?.message}
                {...register("firstName")}
              />
              <TextInput
                label={t("pages.register.lastName.label")}
                aria-label="last-name-input"
                placeholder={t("pages.register.lastName.placeholder") || ""}
                required
                error={errors.lastName?.message}
                {...register("lastName")}
              />
              <TextInput
                label={t("pages.register.phone.label")}
                aria-label="phone-input"
                placeholder={t("pages.register.phone.placeholder") || ""}
                type="number"
                required
                error={
                  errors.phone?.message === "errors.invalidPhone"
                    ? t(errors.phone?.message)
                    : errors.phone?.message
                }
                {...register("phone")}
              />
            </SimpleGrid>

            <Group position="center" mt="md">
              <Button
                type="submit"
                aria-label="register-button"
                color="orange"
                fullWidth
                px="xl"
                disabled={isPending}
              >
                {isPending ? (
                  <Loader variant="dots" color="orange" />
                ) : (
                  t("pages.register.submit")
                )}
              </Button>
            </Group>
          </div>
        </form>
      </div>
    </Paper>
  );
}
