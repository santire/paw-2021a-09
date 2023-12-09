import {
  Autocomplete,
  Flex,
  Grid,
  Group,
} from "@mantine/core";
import {
  IconSearch,
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
import { useFilterSearchParams } from "@/hooks/searchParams.hooks";

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

export function SearchBar() {
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
      aria-label="search-input"
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
export function Header() {
  const { classes, } = useStyles();
  const { t } = useTranslation();

  return (
    <div className={classes.header}>
      <Grid justify="center" px="sm" m={0} align="center">
        <Grid.Col span={6}>
          <Flex justify="left">
            <Group spacing={0}>
              <GourmetableLogo />
              <NavItem label={t("header.browse")} to="restaurants" />
            </Group>
          </Flex>
        </Grid.Col>
        <Grid.Col span={6}>
          <Flex justify="right">
            <SearchBar />
            <Group spacing={0}>
              <NavItem label={t("header.register")} to="register" />
              <NavItem label={t("header.login")} to="login" />
            </Group>
          </Flex>
        </Grid.Col>
      </Grid>
    </div>
  );
}
