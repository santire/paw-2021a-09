import { useTranslation } from "react-i18next";
import { useForm } from "react-hook-form";
import { IUser, IUserUpdate } from "../../types/user/user.models";
import { zodResolver } from "@hookform/resolvers/zod";
import { UserUpdateSchema } from "../../types/user/user.schemas";
import {
  Text,
  Paper,
  Divider,
  SimpleGrid,
  TextInput,
  Group,
  Button,
  Loader,
  Alert,
  PasswordInput,
} from "@mantine/core";
import useStyles from "./EditProfileForm.styles";
import { useUpdateUser } from "../../hooks/user.hooks";
import { useState } from "react";
import { IconCheck } from "@tabler/icons-react";

interface EditProfileFormProps {
  user: IUser;
}

export function EditProfileForm({ user }: EditProfileFormProps) {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const [alert, setAlert] = useState(false);
  const { mutate, isLoading } = useUpdateUser({
    onSuccess: () => {
      setAlert(true);
      resetField("password");
      resetField("repeatPassword");
    },
  });
  const { email, username, phone, firstName, lastName } = user;

  const {
    register,
    handleSubmit,
    resetField,
    formState: { errors },
  } = useForm<IUserUpdate>({
    mode: "onTouched",
    resolver: zodResolver(UserUpdateSchema),
  });

  return (
    <Paper shadow="md" radius="lg" w={"70%"} p="sm">
      <div className={classes.wrapper}>
        <form
          className={classes.form}
          encType="multipart/form-data"
          onSubmit={handleSubmit((data) => mutate(data))}
        >
          <Alert
            icon={<IconCheck size={16} />}
            color="green"
            withCloseButton
            hidden={!alert}
            onClose={() => setAlert(false)}
          >
            {t("pages.profile.successAlert")}
          </Alert>
          <Text
            className={classes.title}
            px="sm"
            mt="sm"
            mb="xl"
            align="center"
          >
            {t("pages.profile.profile")}
          </Text>

          <div className={classes.fields}>
            <SimpleGrid
              cols={2}
              mt="md"
              breakpoints={[{ maxWidth: "sm", cols: 1 }]}
            >
              <TextInput
                mb="md"
                label={t("pages.profile.email.label")}
                value={email}
                disabled={true}
              />
              <TextInput
                mb="md"
                label={t("pages.profile.username.label")}
                value={username}
                disabled={true}
              />
            </SimpleGrid>
            <Divider mt="xl" />
            <SimpleGrid
              cols={2}
              mt="md"
              breakpoints={[{ maxWidth: "sm", cols: 1 }]}
            >
              <TextInput
                mb="md"
                label={t("pages.profile.firstName.label")}
                defaultValue={firstName}
                placeholder={t("pages.profile.firstName.placeholder") || ""}
                required
                error={errors.firstName?.message}
                {...register("firstName")}
              />
              <TextInput
                mb="md"
                label={t("pages.profile.lastName.label")}
                defaultValue={lastName}
                placeholder={t("pages.profile.lastName.placeholder") || ""}
                required
                error={errors.lastName?.message}
                {...register("lastName")}
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
            <SimpleGrid
              cols={2}
              mt="md"
              breakpoints={[{ maxWidth: "sm", cols: 1 }]}
            >
              <TextInput
                mb="md"
                label={t("pages.profile.phoneNumber.label")}
                defaultValue={phone}
                placeholder={t("pages.profile.phoneNumber.placeholder") || ""}
                required
                error={
                  errors.phone?.message === "errors.invalidPhone"
                    ? t(errors.phone?.message)
                    : errors.phone?.message
                }
                {...register("phone")}
              />
            </SimpleGrid>
          </div>
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
                t("pages.profile.submit")
              )}
            </Button>
          </Group>
        </form>
      </div>
    </Paper>
  );
}
