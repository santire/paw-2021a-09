import { Button, 
        Center, 
        Flex, 
        Group, 
        Table, 
        UnstyledButton, 
        Text, 
        createStyles,
        Container,
        Loader,
        Pagination,
    } from "@mantine/core";
import { IconChevronDown, IconChevronUp, IconSelector } from "@tabler/icons";
import { useEffect, useState } from "react";
import { Reservation } from "../../types";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { confirmReservation, FilterParams, getRestaurantReservations } from "../../api/services";
import { useParams, useSearchParams } from "react-router-dom";
import { Page } from "../../types/Page";
import { DenyReservationModal } from "./DenyReservationModal/DenyReservationModal";


const useStyles = createStyles((theme) => ({
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

export function RestaurantReservationsTable({ filterBy }: { filterBy: string }) {
    const { restaurantId } = useParams();
    const [reservationsPage, setReservationsPage] = useState(1);
    const [params, setParams] = useState<FilterParams>({
        page: reservationsPage,
        filterBy: filterBy || "pending",
      });
    const [apiParams, setApiParams] = useState(params);

    const { classes } = useStyles();
    const [sortedReservations, setSortedReservations] = useState<Reservation[]>([]);
    const [sortBy, setSortBy] = useState<string | null>(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);
    const { t } = useTranslation();
    const [searchParams, setSearchParams] = useSearchParams();
    const {
        status,
        data,
        error,
      } = useQuery<Page<Reservation>, Error>(
        ["reservation", apiParams, reservationsPage],
        async () => getRestaurantReservations(restaurantId || "", apiParams),
        { retry: false }
      );

    const [denyModalVisible, setDenyModalVisible] = useState(false);
    const [selectedReservationId, setSelectedReservationId] = useState("");
    const [selectedRestaurantId, setSelectedRestaurantId] = useState("");
      
    const openDenyModal = (restaurantId: string, reservationId: string) => {
        setSelectedRestaurantId(restaurantId);
        setSelectedReservationId(reservationId);
        setDenyModalVisible(true);
    };
      
    const parseSearchParams = () => {
    const auxParams: FilterParams = params;
    searchParams.forEach((value, key) => {
        switch (key) {
            case "page": {
                auxParams.page = parseInt(value);
                break;
            }
            case "filterBy": {
                auxParams.filterBy = value;
                break;
            }
        }
    });

    return auxParams;
    };

    async function confirmReservationHandler(restaurantId: string, reservationId: string){
        var res = await confirmReservation(restaurantId, reservationId);
        if(res == 204   ){
            setSortedReservations(sortedReservations.filter((reservation) => reservation.id !== reservationId));
        }
    }

    const handlePageChange = (page: number) => {
        setSearchParams({ ...searchParams, page: page.toString() });
        setReservationsPage(page)
      };
      
    const rows = sortedReservations.map((reservation) => (
        <tr key={reservation.id}>
          <td>{reservation.quantity}</td>
          <td>{reservation.date}</td>
          <td>{reservation.username}</td>
          {/* Conditional rendering of buttons */}
          {params.filterBy === "pending" ? (
            <>
              <td>
                <Button
                  color="green"
                  onClick={() => confirmReservationHandler(restaurantId!, reservation.id!)}
                >
                  {t`pages.restaurantReservations.confirmButton`}
                </Button>
              </td>
              <td>
                <Button color="red" onClick={() => openDenyModal(restaurantId!, reservation.id!)}>
                {t`pages.restaurantReservations.denyButton`}
                </Button>
              </td>
            </>
          ) : (
            <td>
              <Button color="red" onClick={() => openDenyModal(restaurantId!, reservation.id!)}>{t`pages.restaurantReservations.cancelButton`}</Button>
            </td>
          )}
        </tr>
      ));
      
    const setSorting = (field: string) => {
      const reversed = field === sortBy ? !reverseSortDirection : false;
      setReverseSortDirection(reversed);
      setSortBy(field as keyof Reservation);
    };
    
    useEffect(() => {
    if(data){
        let sortedReservations = data.data;
    
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
    
    }, [data, sortBy, reverseSortDirection]);

    useEffect(() => {
        // first time loading use paremeters in url, otherwise set on change
        const parsedParams = parseSearchParams();
        // Reset to page 1 when switching to an inactive tab
        if (parsedParams.filterBy !== filterBy) {
            setSearchParams({ ...searchParams, page: "1" });
            setReservationsPage(1);
        } else {
            setApiParams(parsedParams);
        }
        setApiParams(parsedParams);
      }, [searchParams, filterBy]);
    
      if (status === "error") {
        return <div>{error!.message}</div>;
      }

      return (
        <Container size="xl" my="xl">
          {status === "loading" ? (
            <Flex justify="center" align="center" h={"100%"}>
              <Loader color="orange" />
            </Flex>
          ) : (
            <>
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
                        {/* Conditional rendering of buttons */}
                        {params.filterBy === "pending" && (
                          <>
                            <th></th> {/* Confirm button */}
                            <th></th> {/* Deny button */}
                          </>
                        )}
                        {params.filterBy !== "pending" && (
                          <th></th>
                        )}
                      </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                  </Table>
                  <div style={{ marginTop: "2rem", textAlign: "center" }}></div>
                  <Pagination
                    total={data?.meta.maxPages ?? 0}
                    siblings={3}
                    page={reservationsPage}
                    onChange={handlePageChange}
                    align="center"
                    color="orange"
                    />
                </Flex>
              )}
            </>
          )}
            {denyModalVisible && (
            <DenyReservationModal
                show={denyModalVisible}
                onClose={() => setDenyModalVisible(false)}
                restaurantId={selectedRestaurantId}
                reservationId={selectedReservationId}
                isCancellation={params.filterBy !== "pending"}
            />
            )}
        </Container>
      );
}

