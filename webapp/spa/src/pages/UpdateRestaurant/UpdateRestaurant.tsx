import { Center, Flex, Loader, Paper } from "@mantine/core";
import { EditRestaurantForm } from "@/components/RestaurantForm/EditRestaurantForm";
import { useGetRestaurant } from "@/hooks/restaurant.hooks";
import { NotFoundPage } from "../NotFound/NotFound";

export function UpdateRestaurantPage({
  restaurantId,
}: {
  restaurantId: number;
}) {
  const { data, isLoading } = useGetRestaurant(restaurantId);

  if (isLoading) {
    return (
      <Center mt="xl">
        <Paper shadow="md" radius="lg" h={"60vh"} w={"50vw"}>
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
