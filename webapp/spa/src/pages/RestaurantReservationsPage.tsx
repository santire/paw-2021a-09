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
  UnstyledButton,
  Group,
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
import { IconSelector, IconChevronDown, IconChevronUp, IconSearch } from "@tabler/icons";



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
  th: {
    padding: '0 !important',
  },

  control: {
    width: '100%',
    padding: `${theme.spacing.xs} ${theme.spacing.md}`,

    '&:hover': {
      backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.colors.gray[0],
    },
  },

  icon: {
    width: '40px',
    height: '40px',
    borderRadius: '40px',
  },
}));

interface TableSortProps {
  data: Reservation[];
}

interface ThProps {
  children: React.ReactNode;
  reversed: boolean;
  sorted: boolean;
  onSort(): void;
}

function Th({ children, reversed, sorted, onSort }: ThProps) {
  const { classes } = useStyles();
  const Icon = sorted ? (reversed ? IconChevronUp : IconChevronDown) : IconSelector;
  return (
    <th className={classes.th}>
      <UnstyledButton onClick={onSort} className={classes.control}>
        <Group position="apart">
          <Text fw={500} fz="sm">
            {children}
          </Text>
          <Center className={classes.icon}>
            <Icon size="0.9rem" stroke={1.5} />
          </Center>
        </Group>
      </UnstyledButton>
    </th>
  );
}


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

  const [sortedReservations, setSortedReservations] = useState<Reservation[]>([]);
  const [sortBy, setSortBy] = useState<string | null>(null);
  const [reverseSortDirection, setReverseSortDirection] = useState(false);


  const rows = sortedReservations.map((reservation) => (
      <tr key={reservation.id}>
        <td>{reservation.quantity}</td>
        <td>{reservation.date}</td>
        <td>{reservation.username}</td>
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

  const setSorting = (field: string) => {
    const reversed = field === sortBy ? !reverseSortDirection : false;
    setReverseSortDirection(reversed);
    setSortBy(field as keyof Reservation);
  };
  
  useEffect(() => {
    if(reservations){
      let sortedReservations = reservations;
    
      // Apply sorting
      if (sortBy) {
        sortedReservations.sort((a, b) => {
          const valueA = a[sortBy as keyof Reservation];
          const valueB = b[sortBy as keyof Reservation];
    
          if (valueA! < valueB!) {
            return reverseSortDirection ? 1 : -1;
          }
          if (valueA! > valueB!) {
            return reverseSortDirection ? -1 : 1;
          }
          return 0;
        });
      }
      setSortedReservations(sortedReservations);
    }
  
  }, [reservations, sortBy, reverseSortDirection]);

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
                    <Th sorted={sortBy === 'quantity'} reversed={reverseSortDirection} onSort={() => setSorting('quantity')}>
                      {t("pages.restaurantReservations.table.customers")}
                    </Th>
                    <Th sorted={sortBy === 'date'} reversed={reverseSortDirection} onSort={() => setSorting('date')}>
                      {t("pages.restaurantReservations.table.date")}
                    </Th>
                    <Th sorted={sortBy === 'username'} reversed={reverseSortDirection} onSort={() => setSorting('username')}>
                      {t("pages.restaurantReservations.table.user")}
                    </Th>
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
