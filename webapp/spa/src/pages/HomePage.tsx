import { Carousel } from "@mantine/carousel";
import {
  Center,
  Container,
  createStyles,
  Flex,
  Text,
  Image,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { getRestaurants } from "../api/services";
import { RestaurantCard } from "../components/RestaurantCard/RestaurantCard";
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
}));

export function HomePage() {
  const { status, data, error } = useQuery<Page<Restaurant>, Error>(
    [],
    async () => getRestaurants()
  );
  const { t } = useTranslation();
  const { classes } = useStyles();

  if (status === "loading") {
    return <div>...</div>;
  }
  if (status === "error") {
    return <div>{error!.message}</div>;
  }

  const restaurants =
    data?.data?.map((rest) => (
      <RestaurantCard
        image={rest.image}
        tags={rest.tags}
        name={rest.name}
        rating={rest.rating}
        likes={rest.likes}
        key={rest.name}
      />
    )) || [];

  if (data?.data?.length && data?.data?.length > 0 && data?.data?.length < 5) {
    // duplicate data to fix carousel
    for (let i = 0; i < data.data?.length; i++) {
      const restaurant = data.data[i];
      const rest = (
        <RestaurantCard
          image={restaurant.image}
          tags={restaurant.tags}
          name={restaurant.name}
          rating={restaurant.rating}
          likes={restaurant.likes}
          key={restaurant.name + i}
        />
      );
      restaurants.push(rest);
    }
  }

  return (
    <>
      <div className={classes.heading}>
        <Flex
          justify="space-between"
          align="center"
          className={classes.headingWrapper}
        >
          <Text className={classes.headingText}>{t("pages.home.heading")}</Text>
          <Image
            src={require("../assets/images/home_image.png")}
            height={"35vh"}
            width={"auto"}
          />
        </Flex>
      </div>
      <Container size="xl" my="xl">
        <Text className={classes.title}>{t`pages.home.highlights`}</Text>
        <Carousel
          slideSize="20%"
          slideGap="sm"
          breakpoints={[
            { maxWidth: "lg", slideSize: "33.333333333%" },
            { maxWidth: "md", slideSize: "50%" },
            { maxWidth: "sm", slideSize: "100%", slideGap: 0 },
          ]}
          align="start"
          controlSize={40}
          loop
        >
          {restaurants?.map((r) => (
            <Carousel.Slide key={r.key}>
              <Center p={0} m={0}>
                {r}
              </Center>
            </Carousel.Slide>
          ))}
        </Carousel>
      </Container>
    </>
  );
}
