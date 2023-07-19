import { Link, useNavigate, useParams } from "react-router-dom";
import { NotFoundPage } from "./NotFound";
import {
  usePageSearchParams,
  useTabSearchParam,
} from "../hooks/searchParams.hooks";
import {
  Center,
  Container,
  Flex,
  Loader,
  Pagination,
  Table,
  Tabs,
  Text,
  createStyles,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import {
  useGetRestaurantReservation,
} from "../hooks/reservation.hooks";
import { IconAlertCircle, IconCircleCheck } from "@tabler/icons-react";
import { RestaurantReservation } from "../components/RestaurantReservation/RestaurantReservation";
import { useGetRestaurant } from "../hooks/restaurant.hooks";
import { useEffect, useState } from "react";
import { useAuth } from "../hooks/useAuth";
import { useIsOwner } from "../hooks/user.hooks";

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
    paddingLeft: theme.spacing.sm,
  },
  subtitle: {
    fontSize: theme.fontSizes.md,
    paddingLeft: theme.spacing.sm,
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

export function RestaurantReservationsPage({
  restaurantId,
}: {
  restaurantId: number;
}) {
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth();
  const [tabParam, setTabParam] = useTabSearchParam("pending");
  const [first, setFirst] = useState(true);
  const navigate = useNavigate();
  const { data, isLoading } = useGetRestaurant(restaurantId);
  const isOwner = useIsOwner({ restaurant: data });

  useEffect(() => {
    // Gives userAuth a chance to check auth before redirecting
    if (first) {
      setFirst(false);
    } else if (!isAuthenticated) {
      navigate("/");
    }
    if (!isLoading && !isOwner) {
      navigate("/");
    }
  }, [first, isAuthenticated, isOwner]);

  return (
    <Wrapper restaurantId={restaurantId}>
      <Flex justify={"center"}>
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
            <PendingReservations restaurantId={restaurantId} />
          </Tabs.Panel>

          <Tabs.Panel value="confirmed" pt="xs">
            <ConfirmedReservations restaurantId={restaurantId} />
          </Tabs.Panel>
        </Tabs>
      </Flex>
    </Wrapper>
  );
}

function PendingReservations({ restaurantId }: { restaurantId: number }) {
  const { t } = useTranslation();
  const [pendingPageParams, setPendingPageParams] = usePageSearchParams(
    undefined,
    "pendingPage"
  );
  const { data, isLoading } = useGetRestaurantReservation(
    restaurantId,
    "pending",
    pendingPageParams
  );

  if (data && data.meta.total <= 0) {
    return (
      <Flex align="flex-start" justify={"center"} w="100%">
        <Flex direction="column" justify="center">
          <Text size={30} mb={5} align="left">
            {t("pages.restaurantReservations.notFoundPending")}
          </Text>
        </Flex>
      </Flex>
    );
  }
  if (data) {
    return (
      <Center mt={50}>
        <Flex direction="column" align="center">
          <Table>
            <thead>
              <tr>
                <th>{t("pages.restaurantReservations.table.customerName")}</th>
                <th>{t("pages.restaurantReservations.table.customerPhone")}</th>
                <th>{t("pages.userReservations.table.customers")}</th>
                <th>{t("pages.userReservations.table.date")}</th>
                <th>{t("pages.userReservations.table.state")}</th>
                <th></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {data.data.map((r) => (
                <RestaurantReservation key={r.reservationId} reservation={r} />
              ))}
            </tbody>
          </Table>
          <div style={{ marginTop: "2rem", textAlign: "center" }}></div>
          <Pagination
            total={data.meta.maxPages ?? 1}
            siblings={3}
            page={pendingPageParams?.page}
            onChange={(e) => setPendingPageParams({ page: e })}
            align="center"
            color="orange"
          />
        </Flex>
      </Center>
    );
  }
  return (
    <Flex justify="center" align="center" h={"100%"}>
      <Loader color="orange" />
    </Flex>
  );
}

function ConfirmedReservations({ restaurantId }: { restaurantId: number }) {
  const { t } = useTranslation();
  const [pendingPageParams, setPendingPageParams] = usePageSearchParams(
    undefined,
    "confirmedPage"
  );
  const { data } = useGetRestaurantReservation(
    restaurantId,
    "confirmed",
    pendingPageParams
  );

  if (data && data.meta.total <= 0) {
    return (
      <Flex align="flex-start" justify={"center"} w="100%">
        <Flex direction="column" justify="center">
          <Text size={30} mb={5} align="left">
            {t("pages.restaurantReservations.notFoundConfirmed")}
          </Text>
        </Flex>
      </Flex>
    );
  }
  if (data) {
    return (
      <Center mt={50}>
        <Flex direction="column" align="center">
          <Table>
            <thead>
              <tr>
                <th>{t("pages.restaurantReservations.table.customerName")}</th>
                <th>{t("pages.restaurantReservations.table.customerPhone")}</th>
                <th>{t("pages.userReservations.table.customers")}</th>
                <th>{t("pages.userReservations.table.date")}</th>
                <th>{t("pages.userReservations.table.state")}</th>
                <th></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {data.data.map((r) => (
                <RestaurantReservation key={r.reservationId} reservation={r} />
              ))}
            </tbody>
          </Table>
          <div style={{ marginTop: "2rem", textAlign: "center" }}></div>
          <Pagination
            total={data.meta.maxPages ?? 1}
            siblings={3}
            page={pendingPageParams?.page}
            onChange={(e) => setPendingPageParams({ page: e })}
            align="center"
            color="orange"
          />
        </Flex>
      </Center>
    );
  }
  return (
    <Flex justify="center" align="center" h={"100%"}>
      <Loader color="orange" />
    </Flex>
  );
}

export function ValidateRestaurantReservation() {
  const params = useParams();
  const restaurantId = params?.restaurantId?.match(/\d+/);
  if (!restaurantId) {
    return <NotFoundPage />;
  }
  return (
    <RestaurantReservationsPage restaurantId={parseInt(restaurantId[0])} />
  );
}

function Wrapper({
  children,
  restaurantId,
}: {
  children: React.ReactNode;
  restaurantId: number;
}) {
  const { classes, theme } = useStyles();
  const { t } = useTranslation();
  return (
    <>
      <Container size="xl" my="xl">
        <Text className={classes.title} mb={"sm"} mt={"xl"}>
          {t`pages.restaurantReservations.title`}
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
          to={`/restaurants/${restaurantId}/reservations/history`}
        >
          <Text mb="xl">{t`pages.restaurantReservations.history`}</Text>
        </Link>
        {children}
      </Container>
    </>
  );
}
