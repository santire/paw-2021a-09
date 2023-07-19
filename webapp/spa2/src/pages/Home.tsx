import {
  Container,
  createStyles,
  Flex,
  Text,
  Image,
  Center,
  Loader,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import homeImageSrc from "../assets/images/home_image.png";
import {
  useGetHotRestaurants,
  useGetPopularRestaurants,
} from "../hooks/restaurant.hooks";
import {
  CarouselWrapper,
  RestaurantCarousel,
} from "../components/RestaurantCarousel/RestaurantCarousel";

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
  const { data: popularRestaurants } = useGetPopularRestaurants();
  const { data: hotRestaurants } = useGetHotRestaurants();

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
        {popularRestaurants ? (
          <RestaurantCarousel
            restaurants={popularRestaurants.data}
            title={t("pages.home.hot")}
            tooltip={t("pages.home.hotTooltip")}
          />
        ) : (
          <CarouselWrapper
            title={t("pages.home.hot")}
            tooltip={t("pages.home.hotTooltip")}
          >
            <Center w={"100%"} mih={200}>
              <Loader variant="dots" color="orange" size={65} />
            </Center>
          </CarouselWrapper>
        )}
      </Container>
      <Container size="xl" my="xl">
        {hotRestaurants ? (
          <RestaurantCarousel
            restaurants={hotRestaurants.data}
            title={t("pages.home.hot")}
            tooltip={t("pages.home.hotTooltip")}
          />
        ) : (
          <CarouselWrapper
            title={t("pages.home.hot")}
            tooltip={t("pages.home.hotTooltip")}
          >
            <Center w={"100%"} mih={200}>
              <Loader variant="dots" color="orange" size={65} />
            </Center>
          </CarouselWrapper>
        )}
      </Container>
    </>
  );
}
