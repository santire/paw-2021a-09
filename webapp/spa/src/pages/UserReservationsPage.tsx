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
  Tabs,
} from "@mantine/core";
import { IconAlertCircle, IconCircleCheck } from "@tabler/icons";
import qs from "qs";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import {
  Navigate,
  useNavigate,
  useParams,
  useSearchParams,
} from "react-router-dom";
import { getUserReservations } from "../api/services";
import { FilterParams } from "../api/services/UserService";
import { ReservationCard } from "../components/ReservationCard/ReservationCard";
import { UserReservationsTable } from "../components/UserReservationsTable/UserReservationsTable";
//import { getRestaurants } from "../api/services";
import { Reservation } from "../types";
import { Page } from "../types/Page";

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
  centerTabsList: {
    display: "flex",
    justifyContent: "center"
  }
}));

export function UserReservationsPage() {
  const { userId } = useParams();
  const [params, setParams] = useState<FilterParams>({
    page: 1,
  });
  const [apiParams, setApiParams] = useState(params);
  const [searchParams, setSearchParams] = useSearchParams();
  const { t } = useTranslation();
  const { classes } = useStyles();
  const navigate = useNavigate();
  
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

  return (
    <>
      <Container size="xl" my="xl">
        <>
          <Text
            className={classes.title}
            mb={"xl"}
            mt={"xl"}>
            {t`pages.restaurantReservations.title`}
          </Text>
          <Button
              style={{
                background: 'none',
                textDecoration: 'underline',
                cursor: 'pointer',
              }}
              onClick={() => navigate(`/users/${userId}/reservations/history`)}
            >
            {t`pages.restaurantReservations.history`}
            </Button>
          <div style={{ marginTop: "4rem", textAlign: "center" }}></div>
          <UserReservationsTable filterBy="" />
        </>
      </Container>
    </>
  );
}
