import { Flex } from "@mantine/core";
import { useEffect } from "react";
import {
  useNavigate,
} from "react-router-dom";
import { RegisterRestaurantForm } from "../components/RegisterRestaurantForm/RegisterRestaurantForm";
import { useAuth } from "../context/AuthContext";

export function RegisterRestaurantPage() {
  const navigate = useNavigate();
  const { authed } = useAuth();

  useEffect(() => {
    if (!authed) {
      navigate("/");
      return;
    }
  }, []);

  return (
    <Flex mt="xl" justify="center">
      <RegisterRestaurantForm />
    </Flex>
  );
}
