import { zodResolver } from "@hookform/resolvers/zod";
import {
  Button,
  Divider,
  Group,
  Paper,
  PasswordInput,
  SimpleGrid,
  Text,
  TextInput,
} from "@mantine/core";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";
import * as z from "zod";
import { register as registerUser } from "../../api/services/AuthService";
import useStyles from "./RegisterForm.styles";

const registerSchema = z
  .object({
    email: z.string().email().min(2).max(100),
    firstName: z.string().min(1).max(100),
    lastName: z.string().min(1).max(100),
    phone: z
      .string()
      .min(6)
      .max(15)
      .regex(/[0-9]+/),
    username: z
      .string()
      .min(3)
      .max(30)
      .regex(/[a-zA-Z0-9]+/),
    password: z.string().min(8).max(100),
    confirmPassword: z.string().min(8).max(100),
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

type RegisterForm = z.infer<typeof registerSchema>;

export function RegisterForm() {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const [_, setSearchParams] = useSearchParams();

  const {
    register,
    handleSubmit,
    reset,
    resetField,
    formState: { errors },
    setValue,
    setError
  } = useForm<RegisterForm>({
    mode: "onTouched",
    resolver: zodResolver(registerSchema),
  });

  const processForm = async (data: RegisterForm) => {
    const { confirmPassword, ...user } = data;
    try {
      await registerUser({ ...user });
      reset();
      setSearchParams({ pendingConfirmation: "true" });
    } catch (e) {
      setError("email", {
        type: "custom",
        message: t("pages.register.email.taken") || ""
      });
      setValue("email", user.email);
    }
  };

  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <form className={classes.form} onSubmit={handleSubmit(processForm)}>
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
                error={errors.confirmPassword?.message}
                {...register("confirmPassword")}
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
                error={errors.phone?.message}
                {...register("phone")}
              />
            </SimpleGrid>

            <Group position="center" mt="md">
              <Button type="submit" color="orange" fullWidth px="xl">
                {t("pages.register.submit")}
              </Button>
            </Group>
          </div>
        </form>
      </div>
    </Paper>
  );
}
