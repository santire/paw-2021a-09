import { Autocomplete, Container, Flex, Grid, Group } from "@mantine/core";
import { IconSearch } from "@tabler/icons";
import { useTranslation } from "react-i18next";
import { Link, RelativeRoutingType } from "react-router-dom";
import { GourmetableLogo } from "../GourmetableLogo/GourmetableLogo";
import useStyles from "./Header.styles";

interface NavItemProps {
  label: string;
  path: string;
  relative?: RelativeRoutingType;
}
function NavItem({ label, path, relative }: NavItemProps) {
  const { classes } = useStyles();
  return (
    <Link
      key={label}
      to={path}
      relative={relative ?? "path"}
      className={classes.link}
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
            theme.colorScheme == "dark"
              ? theme.colors.dark[1]
              : theme.colors.gray[8],
        },
      })}
    />
  );
}

export function Header() {
  const { classes } = useStyles();
  const { t } = useTranslation();

  return (
    <div className={classes.header}>
      <Grid justify="center" p={0} m={0} align="center">
        <Grid.Col span={5}>
          <Flex justify="left">
            <Group spacing={0}>
              <GourmetableLogo />
              <NavItem label={t("header.browse")} path="restaurants" />
              <NavItem label={t("header.reservations")} path="reservations" />
            </Group>
          </Flex>
        </Grid.Col>
        <Grid.Col span={5}>
          <SearchBar />
        </Grid.Col>
        <Grid.Col span={2}>
          <Flex justify="right">
            <span>login/user</span>
          </Flex>
        </Grid.Col>
      </Grid>
    </div>
  );
}
