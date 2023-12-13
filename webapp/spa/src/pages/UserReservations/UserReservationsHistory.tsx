import { Link } from "react-router-dom";
import {
  Text,
  Container,
  Flex,
  Loader,
  createStyles,
  Table,
  Pagination,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useGetReservations } from "@/hooks/reservation.hooks";
import { usePageSearchParams } from "@/hooks/searchParams.hooks";
import { UserReservation } from "@/components/UserReservation/UserReservation";

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
export function UserReservationsHistoryPage() {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const [pageParams, setPageParams] = usePageSearchParams();
  const { data } = useGetReservations("history", "any", pageParams);

  if (data && data.meta.total <= 0) {
    return (
      <Wrapper>
        <Flex justify="center" align="center" h={"100%"}>
          <div className={classes.empty}>
            <Text mb={50}>{t("pages.userReservations.notFound")}</Text>
          </div>
        </Flex>
      </Wrapper>
    );
  }

  if (data) {
    return (
      <Wrapper>
        <Flex direction="column" align="center">
          <Table>
            <thead>
              <tr>
                <th>{t("pages.userReservations.table.customers")}</th>
                <th>{t("pages.userReservations.table.date")}</th>
                <th>{t("pages.userReservations.table.restaurant")}</th>
                <th>{t("pages.userReservations.table.state")}</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {data.data.map((r) => (
                <UserReservation
                  key={r.self}
                  reservation={r}
                  isHistory={true}
                />
              ))}
            </tbody>
          </Table>
          <div style={{ marginTop: "2rem", textAlign: "center" }}></div>
          <Pagination
            total={data.meta.maxPages ?? 1}
            siblings={3}
            page={pageParams?.page}
            onChange={(e) => setPageParams({ page: e })}
            align="center"
            color="orange"
          />
        </Flex>
      </Wrapper>
    );
  }
  return (
    <Wrapper>
      <Container size="xl" my="xl">
        <Flex justify="center" align="center" h={"100%"}>
          <Loader color="orange" size="lg" />
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
          {t`pages.restaurantHistoryReservations.title`}
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
          to={`/user/reservations`}
        >
          <Text mb="xl">{t`pages.restaurantHistoryReservations.current`}</Text>
        </Link>
        {children}
      </Container>
    </>
  );
}
