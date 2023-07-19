import { Center, Flex, Loader, Paper } from "@mantine/core";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { NotFoundPage } from "./NotFound";
import { useGetRestaurant } from "../hooks/restaurant.hooks";
import { EditRestaurantForm } from "../components/RestaurantForm/EditRestaurantForm";

export function UpdateRestaurantPage({
  restaurantId,
}: {
  restaurantId: number;
}) {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [first, setFirst] = useState(true);
  const { data, isLoading } = useGetRestaurant(restaurantId);
  useEffect(() => {
    // Gives userAuth a chance to check auth before redirecting
    if (first) {
      setFirst(false);
    } else if (!isAuthenticated) {
      navigate("/");
    }
  }, [isAuthenticated]);

  if (isLoading) {
    return (
      <Center mt="xl">
        <Paper
          shadow="md"
          radius="lg"
          h={"60vh"}
          w={"50vw"}
          style={{ border: "1px solid red" }}
        >
          <Flex align="center" justify="center" h="100%">
            <Loader color="orange" size="lg" />
          </Flex>
        </Paper>
      </Center>
    );
  }

  if (data) {
    return (
      <Flex mt="xl" justify="center">
        <EditRestaurantForm restaurant={data} />
      </Flex>
    );
  }

  return <NotFoundPage />;
}

export function ValidateRestaurantUpdate() {
  const params = useParams();
  const restaurantId = params?.restaurantId?.match(/\d+/);
  if (!restaurantId) {
    return <NotFoundPage />;
  }
  return <UpdateRestaurantPage restaurantId={parseInt(restaurantId[0])} />;
}
