import { Carousel } from "@mantine/carousel";
import {
  Center,
  Container,
  createStyles,
  Flex,
  Text,
  Image,
  Loader,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { getRestaurants } from "../api/services";
import { getUserLikes } from "../api/services/UserService";
import { RestaurantCard } from "../components/RestaurantCard/RestaurantCard";
import { useAuth } from "../context/AuthContext";
import { PaginatedRestaurants, Restaurant } from "../types";
import { Page } from "../types/Page";
import { RestaurantUtils } from "../utils/RestaurantUtils";

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
  const { authed, user } = useAuth();
  const { classes } = useStyles();
  const { status: likedStatus, data: likedData } = useQuery<string[], Error>(
    "likedRestaurants",
    async () => {
      const response = await getUserLikes(user?.userId!)
      return response;
    },
    { enabled: authed && user?.userId != null }
  );
  const likedRestaurants = likedData || [];


  if (status === "error") {
    return <div>{error!.message}</div>;
  }

  if (likedStatus === "loading") {
    return <div>Loading liked restaurants...</div>;
  }

  const restaurants = data?.data?.map((rest) => (
    <RestaurantCard
      restaurant={rest}
      isOwner={
        rest.owner && user?.userId
          ? RestaurantUtils.userIsOwner(rest.owner, user.userId)
          : false
      }
      likedByUser={likedRestaurants.includes(rest.id!)}
      authed={authed}
      key={rest.name}
    />
  )) || [];
  

  if (data?.data?.length && data?.data?.length > 0 && data?.data?.length < 5) {
    // duplicate data to fix carousel
    for (let i = 0; i < data.data?.length; i++) {
      const restaurant = data.data[i];
      const rest = (
        <RestaurantCard restaurant={restaurant} key={restaurant.name + i} />
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
        {status === "loading" ? (
          <Flex justify="center" align="center" h={"100%"}>
            <Loader color="orange" />
          </Flex>
        ) : (
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
              {restaurants?.map((r) => (
                <Carousel.Slide key={r.key}>
                  <Center p={0} m={0}>
                    {r}
                  </Center>
                </Carousel.Slide>
              ))}
            </Carousel>
          </>
        )}
      </Container>
    </>
  );
}
