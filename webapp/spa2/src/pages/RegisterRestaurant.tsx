import { Flex } from "@mantine/core";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { RestaurantForm } from "../components/RestaurantForm/RestaurantForm";
import { useAuth } from "../hooks/useAuth";

export function RegisterRestaurantPage() {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [first, setFirst] = useState(true);
  useEffect(() => {
    // Gives userAuth a chance to check auth before redirecting
    if (first) {
      setFirst(false);
    } else if (!isAuthenticated) {
      navigate("/");
    }
  }, [isAuthenticated]);

  return (
    <Flex mt="xl" justify="center">
      <RestaurantForm />
    </Flex>
  );
}
