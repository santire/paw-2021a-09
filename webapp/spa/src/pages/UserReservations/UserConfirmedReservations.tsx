import { UserReservation } from "@/components/UserReservation/UserReservation";
import { useGetReservations } from "@/hooks/reservation.hooks";
import { usePageSearchParams } from "@/hooks/searchParams.hooks";
import { Center, Flex, Loader, Pagination, Table, Text } from "@mantine/core";
import { useTranslation } from "react-i18next";

export function UserConfirmedReservations() {
  const { t } = useTranslation();
  const [confirmedPageParams, setConfirmedPageParams] = usePageSearchParams(
    undefined,
    "confirmedPage",
  );

  const { data } = useGetReservations(
    "current",
    "confirmed",
    confirmedPageParams,
  );

  if (data && data.meta.total <= 0) {
    return (
      <Flex align="flex-start" justify={"center"} w="100%">
        <Flex direction="column" justify="center">
          <Text size={30} mb={5} align="left">
            {t("pages.userReservations.notFoundConfirmed")}
          </Text>
        </Flex>
      </Flex>
    );
  }
  if (data) {
    return (
      <>
        <Center mt={50}>
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
                    isHistory={false}
                  />
                ))}
              </tbody>
            </Table>
            <div style={{ marginTop: "2rem", textAlign: "center" }}></div>
            <Pagination
              total={data.meta.maxPages ?? 1}
              siblings={3}
              page={confirmedPageParams?.page}
              onChange={(e) => setConfirmedPageParams({ page: e })}
              align="center"
              color="orange"
            />
          </Flex>
        </Center>
      </>
    );
  }
  return (
    <Flex justify="center" align="center" h={"100%"}>
      <Loader color="orange" />
    </Flex>
  );
}
