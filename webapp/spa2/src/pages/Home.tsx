import {
  Container,
  createStyles,
  Flex,
  Text,
  Image,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import homeImageSrc from "../assets/images/home_image.png";
import {
  useGetHotRestaurants,
  useGetPopularRestaurants,
} from "../hooks/restaurant.hooks";
import { RestaurantCarousel } from "../components/RestaurantCarousel/RestaurantCarousel";

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
}));

export function HomePage() {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const popularRestaurants = useGetPopularRestaurants();
  const hotRestaurants = useGetHotRestaurants();

  return (
    <>
      <div className={classes.heading}>
        <Flex
          justify="space-between"
          align="center"
          className={classes.headingWrapper}
        >
          <Text className={classes.headingText}>{t("pages.home.heading")}</Text>
          <Image src={homeImageSrc} height={"35vh"} width={"auto"} />
        </Flex>
      </div>
      <Container size="xl" my="xl">
        <RestaurantCarousel
          restaurants={popularRestaurants.data?.data}
          title={t("pages.home.popular")}
          tooltip={t("pages.home.popularTooltip")}
          loading={popularRestaurants.isLoading}
        />
      </Container>
      <Container size="xl" my="xl">
        <RestaurantCarousel
          restaurants={hotRestaurants.data?.data}
          title={t("pages.home.hot")}
          tooltip={t("pages.home.hotTooltip")}
          loading={hotRestaurants.isLoading}
        />
      </Container>
    </>
  );
}
