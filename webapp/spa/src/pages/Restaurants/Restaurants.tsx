import {
  Container,
  createStyles,
  Flex,
  Grid,
  Pagination,
  SimpleGrid,
  Text,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { RestaurantCard } from "@/components/RestaurantCard/RestaurantCard";
import { Filter } from "@/components/Filter/Filter";
import { RestaurantCardSkeleton } from "@/components/RestaurantCard/RestaurantCardSkeleton";
import { useGetRestaurants } from "@/hooks/restaurant.hooks";
import { useRestaurantFilterAndPage } from "@/context/RestaurantFilterAndPageContext";
import { useEffect } from "react";

const useStyles = createStyles((theme) => ({
  title: {
    fontSize: theme.fontSizes.xl * 1.8,
    paddingLeft: theme.spacing.sm,
  },
}));

export function RestaurantsPage() {
  const { error, data } = useGetRestaurants();
  const { pageParams, setPageParams, setFilterParams } =
    useRestaurantFilterAndPage();

  useEffect(() => {
    return () => {
      setPageParams();
      setFilterParams({});
    };
  }, []);
  const { t } = useTranslation();

  if (error) {
    return (
      <Wrapper>
        <Flex direction="column" align="center">
          <Text> ERROR MAKE CARD </Text>
        </Flex>
      </Wrapper>
    );
  }

  if (data) {
    const restaurants = data;
    if (restaurants.data.length > 0) {
      return (
        <Wrapper>
          <Flex direction="column" align="center">
            <SimpleGrid cols={3} spacing="xl" mb="xl">
              {restaurants.data.map((rest) => (
                <RestaurantCard restaurant={rest} key={rest.self} />
              ))}
            </SimpleGrid>
            <Pagination
              total={restaurants.meta.maxPages ?? 1}
              siblings={3}
              page={pageParams?.page}
              onChange={(e) => setPageParams({ page: e })}
              align="center"
              color="orange"
              disabled={restaurants.data.length <= 0}
            />
          </Flex>
        </Wrapper>
      );
    } else {
      return (
        <Wrapper>
          <Flex
            direction="column"
            align="center"
            justify="space-between"
            h={"100%"}
          >
            <Flex align={"center"} justify="center" h="100%">
              <Text size="xl">{t("pages.restaurants.notFound")}</Text>
            </Flex>
            <Pagination
              total={1}
              initialPage={1}
              align="center"
              color="orange"
              disabled={true}
            />
          </Flex>
        </Wrapper>
      );
    }
  }

  return (
    <Wrapper>
      <Flex direction="column" align="center">
        <SimpleGrid cols={3} spacing="xl" mb="xl">
          {Array(6)
            .fill(null)
            .map((_, i) => (
              <RestaurantCardSkeleton key={i} />
            ))}
        </SimpleGrid>
        <Pagination
          total={1}
          initialPage={1}
          align="center"
          color="orange"
          disabled={true}
        />
      </Flex>
    </Wrapper>
  );
}

function Wrapper({ children }: { children: React.ReactNode }) {
  const { classes } = useStyles();
  const { t } = useTranslation();
  return (
    <Container my="xl" size={1920}>
      <Text
        my="xl"
        className={classes.title}
      >{t`pages.restaurants.title`}</Text>
      <Grid gutter="md" justify="center">
        <Grid.Col span={3}>
          <Filter />
        </Grid.Col>
        <Grid.Col span={9}>{children}</Grid.Col>
      </Grid>
    </Container>
  );
}
