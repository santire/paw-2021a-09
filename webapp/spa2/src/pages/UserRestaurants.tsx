import {
  Button,
  Container,
  createStyles,
  Flex,
  Grid,
  Pagination,
  SimpleGrid,
  Text,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useGetOwnedRestaurants } from "../hooks/restaurant.hooks";
import { usePageSearchParams } from "../hooks/searchParams.hooks";
import { RestaurantCardSkeleton } from "../components/RestaurantCard/RestaurantCardSkeleton";
import { UserRestaurantCard } from "../components/UserRestaurantCard/UserRestaurantCard";
import { useAuth } from "../hooks/useAuth";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const useStyles = createStyles((theme) => ({
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
  const [pageParams, setPageParams] = usePageSearchParams({
    page: 1,
    pageAmount: 6,
  });
  // Ugly hack, but otherwise would do two queries when loading from url with filter params
  const { data, error } = useGetOwnedRestaurants(pageParams);

  const { classes } = useStyles();
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [first, setFirst] = useState(true);
  useEffect(() => {
    // Gives userAuth a chance to check auth before redirecting
    if (first) {
      setFirst(false);
    } else if (!isAuthenticated) {
      navigate("/");
    }
  }, [isAuthenticated]);

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
                <UserRestaurantCard restaurant={rest} key={rest.id} />
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
      >{t`pages.userRestaurants.title`}</Text>
      <Grid gutter="md" justify="center">
        <Grid.Col span={12}>{children}</Grid.Col>
      </Grid>
    </Container>
  );
}
