import { EditProfileForm } from "@/components/EditProfileForm/EditProfileForm";
import { Flex, Loader, Text } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useUser } from "@/hooks/user.hooks";

export function ProfilePage() {
  const { t } = useTranslation();
  const { isLoading, data: user } = useUser();

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
