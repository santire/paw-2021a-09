import {
  Button,
  Container,
  createStyles,
  Text,
} from "@mantine/core";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { FilterParams } from "../api/services/UserService";
import { RestaurantReservationsTable } from "../components/RestaurantReservationsTable/RestaurantReservationsTable";
import { IconCircleCheck, IconAlertCircle } from '@tabler/icons-react';
import { Tabs } from '@mantine/core';

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
    justifyContent: "center"
  }
}));


export function RestaurantReservationsPage() {
  const { restaurantId } = useParams();
  const [params, setParams] = useState<FilterParams>({
    page: 1,
  });
  const [apiParams, setApiParams] = useState(params);
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const { t } = useTranslation();
  const { classes } = useStyles();

  const [activeTab, setActiveTab] = useState('pending');

  const handleTabChange = (value: string) => {
    setActiveTab(value);
  };
  
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
              onClick={() => navigate(`/restaurants/${restaurantId}/reservations/history`)}
            >
            {t`pages.restaurantReservations.history`}
            </Button>
          <div style={{ marginTop: "4rem", textAlign: "center" }}></div>
          <Tabs variant="pills" value={activeTab} onTabChange={handleTabChange}>
            <Tabs.List className={classes.centerTabsList}>
              <Tabs.Tab value="pending" color={"yellow"} icon={<IconAlertCircle size="16px"/>}>
                {t`pages.restaurantReservations.tabs.pending.title`}
              </Tabs.Tab>
              <Tabs.Tab value="confirmed" color={"green"} icon={<IconCircleCheck size="16px" />}>
                {t`pages.restaurantReservations.tabs.confirmed.title`}
              </Tabs.Tab>
            </Tabs.List>
            <Tabs.Panel value="pending">
              <RestaurantReservationsTable filterBy="pending" />
            </Tabs.Panel>
            <Tabs.Panel value="confirmed">
              <RestaurantReservationsTable filterBy="confirmed" />
            </Tabs.Panel>
          </Tabs>
        </>
      </Container>
    </>
  );
}
