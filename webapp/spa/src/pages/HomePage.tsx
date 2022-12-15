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
import { Restaurant } from "../types";

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
  const { status, data, error } = useQuery<Restaurant[], Error>(
    [],
    getRestaurants
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
    data?.map((rest) => (
      <RestaurantCard
        image={rest.imgUrl}
        tags={rest.tags}
        name={rest.name}
        rating={rest.rating}
        likes={rest.likes}
        key={rest.name}
      />
    )) || [];

  if (data?.length && data.length > 0 && data.length < 5) {
    // duplicate data to fix carousel
    for (let i = 0; i < data.length; i++) {
      const rest = (
        <RestaurantCard
          image={data[i].imgUrl}
          tags={data[i].tags}
          name={data[i].name}
          rating={data[i].rating}
          likes={data[i].likes}
          key={data[i].name + i}
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
          <Text className={classes.headingText}>
            Discover and book the best restaurants
          </Text>
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
