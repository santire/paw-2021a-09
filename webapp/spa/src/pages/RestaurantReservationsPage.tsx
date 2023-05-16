import { Carousel } from "@mantine/carousel";
import {
  Center,
  Container,
  createStyles,
  Flex,
  Text,
  Image,
  Loader,
  Button,
  SimpleGrid,
  Pagination,
  TextInput,
} from "@mantine/core";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { getRestaurantReservations } from "../api/services";
import { confirmReservation, denyReservation } from "../api/services";
import { FilterParams } from "../api/services/UserService";
import { Reservation } from "../types";
import { Page } from "../types/Page";
import { Table } from '@mantine/core';
import { IconSearch } from "@tabler/icons";


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
  empty: {
    align: "center",
    fontSize: 30,
    marginTop: 100,
  },
}));


export function RestaurantReservationsPage() {
  const { restaurantId } = useParams();
  const [params, setParams] = useState<FilterParams>({
    page: 1,
  });
  const [apiParams, setApiParams] = useState(params);
  const [searchParams, setSearchParams] = useSearchParams();
  const {
    status,
    data: reservations,
    error,
  } = useQuery<Reservation[], Error>(
    ["reservation"],
    async () => getRestaurantReservations(restaurantId || ""),
    { retry: false }
  );
  const { t } = useTranslation();
  const { classes } = useStyles();
  const navigate = useNavigate();
  let rows: JSX.Element[] = []


  const parseSearchParams = () => {
    const auxParams: FilterParams = {};
    searchParams.forEach((value, key) => {
      switch (key) {
        case "page": {
          auxParams.page = parseInt(value);
          break;
        }
      }
    });

    return auxParams;
  };


  useEffect(() => {
    // first time loading use paremeters in url, otherwise set on change
    const parsedParams = parseSearchParams();
    setParams(parsedParams);
    setApiParams(parsedParams);
  }, [searchParams]);

  if (reservations) {
    rows = reservations.map((reservation: any) => (
      <tr key={reservation.id}>
        <td>{reservation.quantity}</td>
        <td>{reservation.date}</td>
        <td>{reservation.username}</td>
        {/* onClick={confirmReservation(restaurantId, reservation.id)}> */}
        <td>    
          <Button color="green">
            Confirm
          </Button>
        </td>
        <td>
          <Button color="red">
            Deny
          </Button>
        </td>
      </tr>
    ));
  }

  if (status === "error") {
    return <div>{error!.message}</div>;
  }

  return (
    <>
      <Container size="xl" my="xl">
        {status === "loading" ? (
          <Flex justify="center" align="center" h={"100%"}>
            <Loader color="orange" />
          </Flex>
        ) : (
          <>
            <Text
              className={classes.title}
              mb={"xl"}
              mt={"xl"}
            >{t`pages.restaurantReservations.title`}</Text>

            {rows.length === 0 ? (
              <Flex justify="center" align="center" h={"100%"}>
                <div className={classes.empty}>
                  <Text mb={50}>{t("pages.restaurantReservations.notFound")}</Text>
                </div>
              </Flex>
            ) : (

            <Flex direction="column" align="center">
              <Table>
                <thead>
                  <tr>
                    <th>{t("pages.restaurantReservations.table.customers")}</th>
                    <th>{t("pages.restaurantReservations.table.date")}</th>
                    <th>{t("pages.restaurantReservations.table.user")}</th>
                    {/* Confirm button */}
                    <th></th>
                    {/* Deny button */}
                    <th></th>
                  </tr>
                </thead>
                <tbody>{rows}</tbody>
              </Table>
            </Flex>
            )}
          </>
        )}
      </Container>
    </>
  );
}
