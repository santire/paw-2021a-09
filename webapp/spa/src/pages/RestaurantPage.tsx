import { Container, Flex, Grid, Image, Loader, Text } from "@mantine/core";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useRestaurant } from "../hooks/useRestaurant";

export function RestaurantPage() {
  const { restaurantId } = useParams();
  const navigate = useNavigate();
  const { status, data, error } = useRestaurant(restaurantId || "");

  if (!restaurantId) {
    navigate("/404");
    return <></>;
  }

  if (status === "loading" || !data) {
    return (
      <Flex justify="center" align="center" mt={"50vh"}>
        <Loader color="orange" />
      </Flex>
    );
  }

  const { image, name } = data;
  return (
    <Container size={1920}>
      <Grid justify="center">
        <Grid.Col span={4}>
          <Image src={image} alt={name} height={180} />
        </Grid.Col>
        <Grid.Col span={8}>
          <Text>{name}</Text>
        </Grid.Col>
      </Grid>
    </Container>
  );
}
