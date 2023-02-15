import { Flex, Loader } from "@mantine/core";
import { useEffect } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { RestaurantForm } from "../components/RestaurantForm/RestaurantForm";
import { useAuth } from "../context/AuthContext";
import { useRestaurant } from "../hooks/useRestaurant";

export function EditRestaurantPage() {
  const navigate = useNavigate();
  const { authed, user } = useAuth();
  const { restaurantId } = useParams();
  const { data, isLoading } = useRestaurant(restaurantId ?? "");

  useEffect(() => {
    if (!authed) {
      navigate("/");
      return;
    }
  }, []);
  if (isLoading) {
    return (
      <Flex mt="xl" justify="center">
        {/* <EditRestaurantForm /> */}
        <Loader color="orange" />
      </Flex>
    );
  }

  return (
    <Flex mt="xl" justify="center">
      <RestaurantForm type="update" restaurant={data} />
    </Flex>
  );
}
