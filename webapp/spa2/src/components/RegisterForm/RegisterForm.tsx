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
import { UserRegisterSchema } from "../../types/user/user.schemas";
import { useCreateUser } from "../../hooks/user.hooks";
import { Dispatch, SetStateAction } from "react";
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

  const { mutate, isLoading } = useCreateUser({
    onSuccess: () => {
      reset();
      setPending(true);
    },
    onError: (error) => {
      if (error.code === "validation_error" && error.errors) {
        for (const e of error.errors) {
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
              // TODO: Show default alert?
              console.error("Unexpected error subject: ", e.subject, e.message);
          }
        }
      } else {
        console.error("Unknown error code: ", error.code);
      }
    },
  });

  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <form
          className={classes.form}
          onSubmit={handleSubmit((data) => mutate(data))}
        >
          <Text className={classes.title} px="sm" mt="sm" mb="xl">
            {t("pages.register.title")}
          </Text>
          <div className={classes.fields}>
            <Divider my="xs" label={t("pages.register.loginDivider")} />
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.register.email.label")}
                placeholder={t("pages.register.email.placeholder") || ""}
                required
                error={errors.email?.message}
                {...register("email")}
              />
              <TextInput
                label={t("pages.register.username.label")}
                placeholder={t("pages.register.username.placeholder") || ""}
                required
                error={errors.username?.message}
                {...register("username")}
              />
            </SimpleGrid>
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <PasswordInput
                label={t("pages.register.password.label")}
                placeholder={t("pages.register.password.placeholder") || ""}
                required
                error={errors.password?.message}
                {...register("password")}
              />
              <PasswordInput
                label={t("pages.register.confirmPassword.label")}
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
                placeholder={t("pages.register.firstName.placeholder") || ""}
                required
                error={errors.firstName?.message}
                {...register("firstName")}
              />
              <TextInput
                label={t("pages.register.lastName.label")}
                placeholder={t("pages.register.lastName.placeholder") || ""}
                required
                error={errors.lastName?.message}
                {...register("lastName")}
              />
              <TextInput
                label={t("pages.register.phone.label")}
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
                color="orange"
                fullWidth
                px="xl"
                disabled={isLoading}
              >
                {isLoading ? (
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
