import { Group, Menu, UnstyledButton, Text } from "@mantine/core";
import {
  IconChevronDown,
  IconLogout,
  IconSettings,
  IconSquarePlus,
  IconToolsKitchen2,
} from "@tabler/icons-react";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import useStyles from "./Header.styles";
import { IUser } from "@/types/user/user.models";
import { useLogoutUser } from "@/hooks/user.hooks";
import { useNavigate } from "react-router-dom";

interface Props {
  user: IUser;
  // setAuth: (auth: boolean) => void;
}
export function UserMenu({ user }: Props) {
  const { t } = useTranslation();
  const { classes, cx } = useStyles();
  const [userMenuOpened, setUserMenuOpened] = useState(false);
  const navigate = useNavigate();
  const logout = useLogoutUser();

  return (
    <Menu
      width={260}
      position="bottom-end"
      transition="pop-top-right"
      onClose={() => setUserMenuOpened(false)}
      onOpen={() => setUserMenuOpened(true)}
    >
      <Menu.Target>
        <UnstyledButton
          className={cx(classes.user, { [classes.userActive]: userMenuOpened })}
        >
          <Group spacing={7}>
            <Text weight={500} sx={{ lineHeight: 1 }} mr={3}>
              {user.firstName}
            </Text>
            <IconChevronDown size={12} stroke={1.5} />
          </Group>
        </UnstyledButton>
      </Menu.Target>
      <Menu.Dropdown>
        <Menu.Label>{t("header.userMenu.restaurants.title")}</Menu.Label>
        <Menu.Item
          icon={<IconSquarePlus size={14} stroke={1.5} />}
          onClick={() => navigate("/restaurants/register")}
        >
          {t("header.userMenu.restaurants.register")}
        </Menu.Item>
        <Menu.Item
          icon={<IconToolsKitchen2 size={14} stroke={1.5} />}
          onClick={() => navigate(`/user/restaurants`)}
        >
          {t("header.userMenu.restaurants.restaurants")}
        </Menu.Item>
        <Menu.Divider />
        <Menu.Label>{t("header.userMenu.settings.title")}</Menu.Label>
        <Menu.Item
          icon={<IconSettings size={14} stroke={1.5} />}
          onClick={() => navigate("/user/edit")}
        >
          {t("header.userMenu.settings.account")}
        </Menu.Item>
        <Menu.Item
          icon={<IconLogout size={14} stroke={1.5} />}
          onClick={() => {
            logout();
          }}
        >
          {t("header.userMenu.settings.logout")}
        </Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
}
