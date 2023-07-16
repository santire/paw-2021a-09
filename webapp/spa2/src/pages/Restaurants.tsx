import {
  Center,
  Container,
  createStyles,
  Flex,
  Grid,
  Pagination,
  SimpleGrid,
  Text,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { RestaurantCard } from "../components/RestaurantCard/RestaurantCard";
import { useGetRestaurants } from "../hooks/restaurant.hooks";
import {
  useFilterSearchParams,
  usePageSearchParams,
} from "../hooks/searchParams.hooks";
import { Filter } from "../components/Filter/Filter";
import { RestaurantCardSkeleton } from "../components/RestaurantCard/RestaurantCardSkeleton";

const useStyles = createStyles((theme) => ({
  title: {
    fontSize: theme.fontSizes.xl * 1.8,
    paddingLeft: theme.spacing.sm,
  },
}));

export function RestaurantsPage() {
  const [pageParams, setPageParams] = usePageSearchParams();
  const [filterParams] = useFilterSearchParams();
  // Ugly hack, but otherwise would do two queries when loading from url with filter params
  const { data, error } = useGetRestaurants(
    pageParams && filterParams ? { ...pageParams, ...filterParams } : undefined
  );

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
                <RestaurantCard restaurant={rest} key={rest.name} />
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
