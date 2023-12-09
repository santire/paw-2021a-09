import { Flex, Grid, Group } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { GourmetableLogo } from "../GourmetableLogo/GourmetableLogo";
import useStyles from "./Header.styles";
import { useUser } from "@/hooks/queries/users";
import { NavItem } from "./NavItem";
import { SearchBar } from "./SearchBar";
import { UserMenu } from "./UserMenu";
import { useState } from "react";
import { isAuthed } from "@/utils/AuthStorage";
export function Header() {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const user = useUser();

  return (
    <div className={classes.header}>
      <Grid justify="center" px="sm" m={0} align="center">
        <Grid.Col span={6}>
          <Flex justify="left">
            <Group spacing={0}>
              <GourmetableLogo />
              <NavItem label={t("header.browse")} to="restaurants" />
              {user.isSuccess ? (
                <NavItem
                  label={t("header.reservations")}
                  to={`user/reservations`}
                  hidden={!user.data}
                />
              ) : null}
            </Group>
          </Flex>
        </Grid.Col>
        <Grid.Col span={6}>
          <Flex justify="right">
            <SearchBar />
            {user.isSuccess ? (
              <Group spacing={0}>
                <UserMenu user={user.data} />
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
