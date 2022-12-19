import {
  Autocomplete,
  Flex,
  Grid,
  Group,
  Menu,
  UnstyledButton,
  Text,
} from "@mantine/core";
import {
  IconChevronDown,
  IconLogout,
  IconMessagePlus,
  IconSearch,
  IconSettings,
  IconSquarePlus,
  IconToolsKitchen2,
} from "@tabler/icons";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import {
  Link,
  LinkProps,
  RelativeRoutingType,
  useNavigate,
} from "react-router-dom";
import { logout } from "../../api/services/AuthService";
import { useAuth } from "../../context/AuthContext";
import { GourmetableLogo } from "../GourmetableLogo/GourmetableLogo";
import useStyles from "./Header.styles";

interface NavItemProps extends LinkProps {
  label: string;
  to: string;
  relative?: RelativeRoutingType;
}
function NavItem({ label, to, relative, hidden }: NavItemProps) {
  const { classes } = useStyles();
  return (
    <Link
      key={label}
      to={to}
      className={classes.link}
      relative={relative ?? "route"}
      hidden={hidden}
    >
      {label}
    </Link>
  );
}

function SearchBar() {
  const { classes } = useStyles();
  const { t } = useTranslation();
  return (
    <Autocomplete
      className={classes.search}
      placeholder={t("header.search") || ""}
      icon={<IconSearch size={16} stroke={1.5} />}
      data={["teest", "test"]}
      styles={(theme) => ({
        input: {
          background:
            theme.colorScheme === "dark"
              ? theme.colors.dark[4]
              : theme.colors.gray[0],
          color:
            theme.colorScheme === "dark"
              ? theme.colors.dark[1]
              : theme.colors.gray[8],
        },
      })}
    />
  );
}

export function Header() {
  const { classes, cx } = useStyles();
  const { t } = useTranslation();
  const [userMenuOpened, setUserMenuOpened] = useState(false);
  const { user } = useAuth();
  const navigate = useNavigate();

  const UserMenu = (
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
              {user?.firstName}
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
          onClick={() => navigate(`/users/${user?.userId}/restaurants`)}
        >
          {t("header.userMenu.restaurants.restaurants")}
        </Menu.Item>
        <Menu.Item
          icon={<IconMessagePlus size={14} stroke={1.5} />}
          onClick={() => navigate(`/users/${user?.userId}/reservations`)}
        >
          {t("header.userMenu.restaurants.reservations")}
        </Menu.Item>
        <Menu.Divider />
        <Menu.Label>{t("header.userMenu.settings.title")}</Menu.Label>
        <Menu.Item
          icon={<IconSettings size={14} stroke={1.5} />}
          onClick={() => navigate(`/users/${user?.userId}`)}
        >
          {t("header.userMenu.settings.account")}
        </Menu.Item>
        <Menu.Item
          icon={<IconLogout size={14} stroke={1.5} />}
          onClick={() => {
            logout();
            navigate("/");
          }}
        >
          {t("header.userMenu.settings.logout")}
        </Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
  return (
    <div className={classes.header}>
      <Grid justify="center" px="sm" m={0} align="center">
        <Grid.Col span={6}>
          <Flex justify="left">
            <Group spacing={0}>
              <GourmetableLogo />
              <NavItem label={t("header.browse")} to="restaurants" />
              <NavItem
                label={t("header.reservations")}
                to={`users/${user?.userId}/reservations`}
                hidden={!user}
              />
            </Group>
          </Flex>
        </Grid.Col>
        <Grid.Col span={6}>
          <Flex justify="right">
            <SearchBar />
            {user ? (
              <Group spacing={0}>{UserMenu}</Group>
            ) : (
              <Group spacing={0}>
                <NavItem label={t("header.register")} to="register" />
                <NavItem label={t("header.login")} to="login" />
              </Group>
            )}
          </Flex>
        </Grid.Col>
      </Grid>
    </div>
  );
}
