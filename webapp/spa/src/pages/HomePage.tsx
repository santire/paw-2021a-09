import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { getRestaurants } from "../api/services";
import { RestaurantCard } from "../components/RestaurantCard/RestaurantCard";
import { Restaurant } from "../types";
import { Group } from "@mantine/core";

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

  const restaurants = data?.map((rest) => (
    <RestaurantCard
      image={rest.imgUrl}
      tags={rest.tags}
      name={rest.name}
      rating={rest.rating}
      likes={rest.likes}
      key={rest.name}
    />
  ));
  return (
    <>
      <h1>{t`title`}</h1>
      <Group>{restaurants}</Group>
    </>
  );
}
