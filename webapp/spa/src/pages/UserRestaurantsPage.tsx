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
} from "@mantine/core";
import qs from "qs";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { Navigate, useNavigate, useParams, useSearchParams } from "react-router-dom";
import { FilterParams, getUserRestaurants } from "../api/services/UserService";
//import { getRestaurants } from "../api/services";
import { UserRestaurantCard } from "../components/UserRestaurantCard/UserRestaurantCard";
import { useAuth } from "../context/AuthContext";
import { PaginatedRestaurants, Restaurant } from "../types";
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
}));

export function UserRestaurantsPage() {
  const { userId } = useParams();
  const [params, setParams] = useState<FilterParams>({
    page: 1,
  });
  const [apiParams, setApiParams] = useState(params);
  const [searchParams, setSearchParams] = useSearchParams();
  const { status, data, error, refetch } = useQuery<Page<Restaurant>, Error>(
    ["ownedRestaurants", { userId, params }],
    async () =>
      getUserRestaurants({
        userId: userId || "",
        params: { ...params },
      })
  );
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


  if (status === "error") {
    return <div>{error!.message}</div>;
  }

  const restaurants =
    data?.data?.map((rest) => (
      <UserRestaurantCard
        restaurant={rest}
        key={rest.name}
        onDelete={(id) => refetch()}
      />
    )) || [];

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
            >{t`pages.userRestaurants.title`}</Text>

            {restaurants.length === 0 ? (
              <Flex justify="center" align="center" h={"100%"}>
                <div className={classes.empty}>
                  <Text mb={50}>{t("pages.userRestaurants.notFound")}</Text>
                  <Button
                    color="orange"
                    mx={170}
                    onClick={() => navigate("/restaurants/register")}
                  >
                    {t("pages.userRestaurants.createOne")}
                  </Button>
                </div>
              </Flex>
            ) : (

            <Flex direction="column" align="center">
              <SimpleGrid cols={3} spacing="xl" mb="xl">
                {restaurants}
              </SimpleGrid>
              <Pagination
                total={data?.meta.maxPages ?? 0}
                siblings={3}
                initialPage={params.page ?? 1}
                page={params.page}
                onChange={(e) => {
                  setParams((prev) => ({ ...prev, page: e }));
                  setApiParams((prev) => ({ ...prev, page: e }));
                  setSearchParams(
                    qs.stringify(
                      { ...params, page: e },
                      { arrayFormat: "repeat" }
                    )
                  );
                }}
                align="center"
                color="orange"
              />
            </Flex>
            )}
          </>
        )}
      </Container>
    </>
  );
}
