import { Flex } from "@mantine/core";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { RestaurantForm } from "../components/RestaurantForm/RestaurantForm";
import { useAuth } from "../hooks/useAuth";

export function RegisterRestaurantPage() {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/");
      return;
    }
  }, []);

  return (
    <Flex mt="xl" justify="center">
      <RestaurantForm />
    </Flex>
  );
}
