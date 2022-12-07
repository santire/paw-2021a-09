import { Carousel } from "@mantine/carousel";
import {
  Center,
  Container,
  Flex,
  Group,
  SimpleGrid,
  Title,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { getRestaurants } from "../api/services";
import { RestaurantCard } from "../components/RestaurantCard/RestaurantCard";
import { Restaurant } from "../types";

export function HomePage() {
  const { status, data, error } = useQuery<Restaurant[], Error>(
    [],
    getRestaurants
  );
  const { t } = useTranslation();

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
      <Title order={1} mt="xl" mb="sm">{t`pages.home.highlights`}</Title>
      <Container size="xl" my="xl">
        <Carousel
          slideSize="20%"
          slideGap="sm"
          breakpoints={[
            { maxWidth: "lg", slideSize: "33.333333333%" },
            { maxWidth: "md", slideSize: "50%" },
            { maxWidth: "sm", slideSize: "100%", slideGap: 0 },
          ]}
          align="center"
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
