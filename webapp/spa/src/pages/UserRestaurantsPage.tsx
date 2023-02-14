import { Carousel } from "@mantine/carousel";
import {
  Center,
  Container,
  createStyles,
  Flex,
  Text,
  Image,
  Loader,
  Button,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { Navigate, useNavigate } from "react-router-dom";
import { getUserRestaurants } from "../api/services/UserService"
//import { getRestaurants } from "../api/services";
import { UserRestaurantCard } from "../components/UserRestaurantCard/UserRestaurantCard";
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
  empty: {
    align: "center",
    fontSize: 30,
    marginTop: 100
  }
}));

export function UserRestaurantsPage() {
  const { status, data, error, refetch } = useQuery<Page<Restaurant>, Error>(
    [],
    async () => getUserRestaurants()
  );
  const { t } = useTranslation();
  const { classes } = useStyles();
  const navigate = useNavigate();


  if (status === "error") {
    return <div>{error!.message}</div>;
  }
  

  const restaurants =
  data?.data?.map((rest) => (
    <UserRestaurantCard restaurant={rest} key={rest.name} onDelete={(id) => refetch()} />
  )) || [];

  return (
    <>
      <Container size="xl" my="xl">
        {status === "loading" ? (
          <Flex justify="center" align="center" h={"100%"}>
            <Loader color="orange" />
          </Flex>
        ) : (
          <>
            <Text className={classes.title} mb={"xl"} mt={"xl"}>{t`pages.userRestaurants.title`}</Text>

            {
              restaurants.length === 0 ? (
                <Flex justify="center" align="center" h={"100%"}>
                  <div className={classes.empty}>
                    <Text mb={50}>
                      {t("pages.userRestaurants.notFound")}
                    </Text>
                    <Button color="orange" mx={170} onClick={() => navigate("/restaurants/register")}>
                      {t("pages.userRestaurants.createOne")}
                    </Button>
                  </div>
                </Flex>
              ) 
              : (
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
              )
            }
            
          </>
        )}
      </Container>
    </>
  );
}
