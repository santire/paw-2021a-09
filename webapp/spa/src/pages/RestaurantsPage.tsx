import {
  Container,
  createStyles,
  Flex,
  Grid,
  Loader,
  Pagination,
  SimpleGrid,
  Text,
} from "@mantine/core";
import qs from "qs";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";
import { FilterParams } from "../api/services";
import { Filter } from "../components/Filter/Filter";
import { RestaurantCard } from "../components/RestaurantCard/RestaurantCard";
import { useRestaurants } from "../hooks/useRestaurants";

const useStyles = createStyles((theme) => ({
  title: {
    fontSize: theme.fontSizes.xl * 1.8,
    paddingLeft: theme.spacing.sm,
  },
}));

export function RestaurantsPage() {
  const [params, setParams] = useState<FilterParams>({
    page: 1,
  });
  const [apiParams, setApiParams] = useState(params);
  const [searchParams, setSearchParams] = useSearchParams();

  const { status, data, error } = useRestaurants(apiParams);

  const { classes } = useStyles();
  const { t } = useTranslation();

  const parseSearchParams = () => {
    const auxParams: FilterParams = {};
    searchParams.forEach((value, key) => {
      switch (key) {
        case "page": {
          auxParams.page = parseInt(value);
          break;
        }
        case "min": {
          auxParams.min = parseInt(value);
          break;
        }
        case "max": {
          auxParams.max = parseInt(value);
          break;
        }
        case "order": {
          let order = value.toLowerCase();
          if (!["asc", "desc"].includes(order)) {
            order = "asc";
          }
          auxParams.order = order;
          break;
        }
        case "sort": {
          let sort = value.toLowerCase();
          // TODO: check valid options
          // if (order !== "ASC" && order !== "DESC") {
          //   order = "ASC";
          // }
          auxParams.sort = sort;
          break;
        }
        case "tags": {
          if (auxParams.tags === undefined) {
            auxParams.tags = [];
          }
          if (value !== "") {
            auxParams.tags.push(value);
          }
          break;
        }
        case "search": {
          auxParams.search = value;
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

  const apply = () => {
    // setSearchParams(makeSearchParams(params));
    setSearchParams(qs.stringify(params, { arrayFormat: "repeat" }));
  };

  const clear = () => {
    setSearchParams({});
    setParams({});
  };

  const restaurantCards =
    data?.data?.map((rest) => (
      <RestaurantCard restaurant={rest} key={rest.name} />
    )) || [];

  return (
    <Container my="xl" size={1920}>
      <Text
        my="xl"
        className={classes.title}
      >{t`pages.restaurants.title`}</Text>
      <Grid gutter="md" justify="center">
        <Grid.Col span={3}>
          <Filter
            params={params}
            setParams={setParams}
            apply={apply}
            clear={clear}
          />
        </Grid.Col>
        <Grid.Col span={9}>
          {status === "loading" ? (
            <Flex justify="center" align="center" h={"100%"}>
              <Loader color="orange" />
            </Flex>
          ) : (
            <Flex direction="column" align="center">
              <SimpleGrid cols={3} spacing="xl" mb="xl">
                {restaurantCards}
              </SimpleGrid>
              <Pagination
                total={data?.meta.maxPages ?? 0}
                siblings={3}
                initialPage={params.page ?? 1}
                page={params.page}
                onChange={(e) => {
                  setParams((prev) => ({ ...prev, page: e }));
                  setApiParams((prev) => ({ ...prev, page: e }));
                }}
                align="center"
                color="orange"
              />
            </Flex>
          )}
        </Grid.Col>
      </Grid>
    </Container>
  );
}
