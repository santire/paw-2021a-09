import { RestaurantReservation } from "@/components/RestaurantReservation/RestaurantReservation";
import { useGetRestaurantReservations } from "@/hooks/reservation.hooks";
import { usePageSearchParams } from "@/hooks/searchParams.hooks";
import { Center, Flex, Loader, Pagination, Table, Text } from "@mantine/core";
import { useTranslation } from "react-i18next";

export function PendingReservations({
  restaurantId,
}: {
  restaurantId: number;
}) {
  const { t } = useTranslation();
  const [pendingPageParams, setPendingPageParams] = usePageSearchParams(
    undefined,
    "pendingPage",
  );
  const { data, isSuccess } = useGetRestaurantReservations(
    restaurantId,
    "current",
    "pending",
    pendingPageParams,
  );

  if (isSuccess && data && data.meta.total <= 0) {
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
  if (isSuccess && data) {
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
                <RestaurantReservation key={r.id} reservation={r} />
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
