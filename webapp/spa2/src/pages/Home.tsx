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
import homeImageSrc from "../assets/images/home_image.png";
import { useGetRestaurants } from "../hooks/restaurant.hooks";
import { useQueryClient } from "react-query";
import { RestaurantCard } from "../components/RestaurantCard/RestaurantCard";

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
  const { classes } = useStyles();
  const { t } = useTranslation();
  const restaurants = [<div>rest</div>, <div>rest</div>];
  const r = useGetRestaurants({ page: 0, pageAmount: 10 });
  const queryClient = useQueryClient();

  // if (data?.data?.length && data?.data?.length > 0 && data?.data?.length < 5) {
  //   // duplicate data to fix carousel
  //   for (let i = 0; i < data.data?.length; i++) {
  //     const restaurant = data.data[i];
  //     const rest = (
  //       <RestaurantCard restaurant={restaurant} key={restaurant.name + i} />
  //     );
  //     restaurants.push(rest);
  //   }
  // }

  return (
    <>
      <button onClick={() => queryClient.invalidateQueries("restaurants")}>
        invalidate
      </button>
      {r.data?.data.map((d) => (
        <div key={d.id}>
          <RestaurantCard restaurant={d} />
          <br />
          <br />
        </div>
      ))}
      {r.data ? <div></div> : null}
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
        <>
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
            //loop
          >
            {restaurants?.map((r, i) => (
              <Carousel.Slide key={i}>
                <Center p={0} m={0}>
                  {r}
                </Center>
              </Carousel.Slide>
            ))}
          </Carousel>
        </>
      </Container>
    </>
  );
}
