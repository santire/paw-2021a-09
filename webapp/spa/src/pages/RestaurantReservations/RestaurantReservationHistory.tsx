import { Link, useNavigate, useParams } from "react-router-dom";
import { usePageSearchParams } from "@/hooks/searchParams.hooks";
import {
  Center,
  Container,
  Flex,
  Loader,
  Pagination,
  Table,
  Text,
  createStyles,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useGetRestaurantReservations } from "@/hooks/reservation.hooks";
import { RestaurantReservation } from "@/components/RestaurantReservation/RestaurantReservation";
import { useGetRestaurant } from "@/hooks/restaurant.hooks";
import { useIsOwner } from "@/hooks/user.hooks";
import { NotFoundPage } from "../NotFound/NotFound";
import { useEffect } from "react";

const useStyles = createStyles((theme) => ({
  heading: {
    background:
      theme.colorScheme === "dark"
        ? theme.colors.dark[4]
        : theme.colors.gray[3],
    opacity: 0.8,
    paddingTop: theme.spacing.xl,
    paddingBottom: theme.spacing.xl,
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
  subtitle: {
    fontSize: theme.fontSizes.md,
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

export function RestaurantReservationsHistoryPage({
  restaurantId,
}: {
  restaurantId: number;
}) {
  const navigate = useNavigate();
  const { data, isLoading } = useGetRestaurant(restaurantId);
  const isOwner = useIsOwner({ restaurant: data });

  if (isLoading) {
    return (
      <Flex justify="center" align="center" h={"100vh"}>
        <Loader color="orange" />
      </Flex>
    );
  }

  if (!isLoading && !isOwner) {
    navigate("/");
  }

  return (
    <Wrapper restaurantId={restaurantId}>
      <Flex justify={"center"}>
        <Reservations restaurantId={restaurantId} />
      </Flex>
    </Wrapper>
  );
}

function Reservations({ restaurantId }: { restaurantId: number }) {
  const { t } = useTranslation();
  const [pendingPageParams, setPendingPageParams] = usePageSearchParams();
  const { data } = useGetRestaurantReservations(
    restaurantId,
    "history",
    "any",
    pendingPageParams,
  );

  if (data && data.meta.total <= 0) {
    return (
      <Flex align="flex-start" justify={"center"} w="100%">
        <Flex direction="column" justify="center">
          <Text size={30} mb={5} align="left">
            {t("pages.restaurantReservations.notFound")}
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
                <RestaurantReservation
                  key={r.id}
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

export function ValidateRestaurantReservationHistory() {
  const params = useParams();
  const restaurantId = params?.restaurantId?.match(/\d+/);
  if (!restaurantId) {
    return <NotFoundPage />;
  }
  return (
    <RestaurantReservationsHistoryPage
      restaurantId={parseInt(restaurantId[0])}
    />
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
          to={`/restaurants/${restaurantId}/reservations`}
        >
          <Text mb="xl">{t`pages.restaurantHistoryReservations.current`}</Text>
        </Link>
        {children}
      </Container>
    </>
  );
}
