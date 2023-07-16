import { Flex, Loader, Text } from "@mantine/core";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { useGetUser } from "../hooks/user.hooks";
import { useTranslation } from "react-i18next";
import { EditProfileForm } from "../components/EditProfileForm/EditProfileForm";

export function ProfilePage() {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth();
  const { data: user, isLoading } = useGetUser();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/");
      return;
    }
  }, []);

  if (isLoading) {
    return (
      <Flex justify="center" align="center" h={"100vh"}>
        <Loader color="orange" />
      </Flex>
    );
  }

  if (user) {
    return (
      <Flex mt="xl" justify="center" align="center">
        <EditProfileForm user={user} />
      </Flex>
    );
  }

  return (
    <Flex justify="center" align="center" h={"100vh"}>
      <Text size="xl">{t("pages.profile.notFound")}</Text>
    </Flex>
  );
}
