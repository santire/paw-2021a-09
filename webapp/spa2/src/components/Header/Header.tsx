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
  IconSearch,
  IconSettings,
  IconSquarePlus,
  IconToolsKitchen2,
} from "@tabler/icons-react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import {
  Link,
  LinkProps,
  RelativeRoutingType,
  createSearchParams,
  useNavigate,
} from "react-router-dom";
import { GourmetableLogo } from "../GourmetableLogo/GourmetableLogo";
import useStyles from "./Header.styles";
import { useAuth } from "../../hooks/useAuth";
import { useGetUser } from "../../hooks/user.hooks";
import { IUser } from "../../types/user/user.models";
import { useQueryClient } from "react-query";
import { useFilterSearchParams } from "../../hooks/searchParams.hooks";

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
  const [search, setSearch] = useState("");
  const [filterParams, setFilterParams] = useFilterSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    setSearch(filterParams?.search ?? "");
  }, [filterParams]);

  return (
    <Autocomplete
      className={classes.search}
      placeholder={t("header.search") || ""}
      icon={<IconSearch size={16} stroke={1.5} />}
      value={search}
      onChange={(e) => setSearch(e.trim())}
      data={[]}
      onKeyDown={(e) => {
        if (e.key === "Enter") {
          setFilterParams({ search: search });
          navigate({
            pathname: "restaurants",
            search: `${createSearchParams({ search: search })}`,
          });
        }
      }}
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

function UserMenu(user: IUser) {
  const { t } = useTranslation();
  const { classes, cx } = useStyles();
  const [userMenuOpened, setUserMenuOpened] = useState(false);
  const navigate = useNavigate();
  const { logout } = useAuth();
  const queryClient = useQueryClient();
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
            queryClient.clear();
            navigate("/");
          }}
        >
          {t("header.userMenu.settings.logout")}
        </Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
}

export function Header() {
  const { classes, cx } = useStyles();
  const { t } = useTranslation();
  const user = useGetUser();
  const { isAuthenticated } = useAuth();

  return (
    <div className={classes.header}>
      <Grid justify="center" px="sm" m={0} align="center">
        <Grid.Col span={6}>
          <Flex justify="left">
            <Group spacing={0}>
              <GourmetableLogo />
              <NavItem label={t("header.browse")} to="restaurants" />
              {isAuthenticated && user.isSuccess ? (
                <NavItem
                  label={t("header.reservations")}
                  to={`user/reservations`}
                  hidden={!user}
                />
              ) : null}
            </Group>
          </Flex>
        </Grid.Col>
        <Grid.Col span={6}>
          <Flex justify="right">
            <SearchBar />
            {isAuthenticated && user.isSuccess ? (
              <Group spacing={0}>
                <UserMenu {...user.data} />
              </Group>
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
