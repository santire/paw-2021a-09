import { useTranslation } from "react-i18next";
import useStyles from "./RestaurantReservations.styles";
import { useTabSearchParam } from "@/hooks/searchParams.hooks";
import { Link, useNavigate } from "react-router-dom";
import { useGetRestaurant } from "@/hooks/restaurant.hooks";
import { useIsOwner } from "@/hooks/user.hooks";
import { Container, Flex, Loader, Tabs, Text } from "@mantine/core";
import { IconAlertCircle, IconCircleCheck } from "@tabler/icons-react";
import { PendingReservations } from "./PendingReservations";
import { ConfirmedReservations } from "./ConfirmedReservations";

export function RestaurantReservationsPage({
  restaurantId,
}: {
  restaurantId: number;
}) {
  const { t } = useTranslation();
  const [tabParam, setTabParam] = useTabSearchParam("pending");
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
