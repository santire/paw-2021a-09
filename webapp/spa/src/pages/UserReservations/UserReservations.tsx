import { Link } from "react-router-dom";
import { Text, Container, Flex, createStyles, Tabs } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useTabSearchParam } from "@/hooks/searchParams.hooks";
import { IconAlertCircle, IconCircleCheck } from "@tabler/icons-react";
import { UserPendingReservations } from "./UserPendingReservations";
import { UserConfirmedReservations } from "./UserConfirmedReservations";

const useStyles = createStyles((theme) => ({
  heading: {
    background:
      theme.colorScheme === "dark"
        ? theme.colors.dark[4]
        : theme.colors.gray[3],
    opacity: 0.8,
    paddingTop: theme.spacing.xl,
    paddingBottom: theme.spacing.xl,
    paddingLeft: theme.spacing.md,
    paddingRight: theme.spacing.md,
  },
  headingWrapper: {
    maxWidth: 1920,
    margin: "0 auto",
  },
  headingText: {
    fontSize: "5vh",
    maxWidth: "50%",
    paddingLeft: theme.spacing.xl * 2,
    paddingRight: theme.spacing.xl * 2,
    color:
      theme.colorScheme === "dark"
        ? theme.colors.dark[0]
        : theme.colors.gray[9],
  },
  title: {
    fontSize: theme.fontSizes.xl * 1.8,
  },
  empty: {
    align: "center",
    fontSize: 30,
    marginTop: 100,
  },
  centerTabsList: {
    display: "flex",
    justifyContent: "center",
  },
}));
export function UserReservationsPage() {
  const [tabParam, setTabParam] = useTabSearchParam("pending");
  const { t } = useTranslation();
  return (
    <Wrapper>
      <Container size="xl" my="xl">
        <Flex justify="center" align="center" h={"100%"}>
          <Tabs
            value={tabParam ?? "pending"}
            onTabChange={setTabParam}
            style={{ width: "80%", justifyContent: "center" }}
          >
            <Tabs.List>
              <Tabs.Tab value="pending" icon={<IconAlertCircle size={16} />}>
                {t`pages.restaurantReservations.tabs.pending.title`}
              </Tabs.Tab>
              <Tabs.Tab value="confirmed" icon={<IconCircleCheck size={16} />}>
                {t`pages.restaurantReservations.tabs.confirmed.title`}
              </Tabs.Tab>
            </Tabs.List>

            <Tabs.Panel value="pending" pt="xs">
              <UserPendingReservations />
            </Tabs.Panel>

            <Tabs.Panel value="confirmed" pt="xs">
              <UserConfirmedReservations />
            </Tabs.Panel>
          </Tabs>
        </Flex>
      </Container>
    </Wrapper>
  );
}

function Wrapper({ children }: { children: React.ReactNode }) {
  const { classes, theme } = useStyles();
  const { t } = useTranslation();
  return (
    <>
      <Container size="xl" my="xl">
        <Text className={classes.title} mb={"sm"} mt={"xl"}>
          {t`pages.userReservations.title`}
        </Text>
        <Link
          style={{
            background: "none",
            textDecoration: "underline",
            cursor: "pointer",
            color:
              theme.colorScheme === "dark"
                ? theme.colors.gray[3]
                : theme.colors.gray[8],
          }}
          to={`/user/reservations/history`}
        >
          <Text mb="xl">{t`pages.userReservations.history`}</Text>
        </Link>
        {children}
      </Container>
    </>
  );
}
