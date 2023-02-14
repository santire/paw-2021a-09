import { Flex, Text, Title } from "@mantine/core";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import {
  createSearchParams,
  useNavigate,
  useSearchParams,
} from "react-router-dom";
import { activateUser } from "../api/services/AuthService";
import { RegisterRestaurantForm } from "../components/RegisterRestaurantForm/RegisterRestaurantForm";
import { useAuth } from "../context/AuthContext";

export function RegisterRestaurantPage() {
  const [searchParams] = useSearchParams();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { authed, user } = useAuth();

  useEffect(() => {
    if (!authed) {
      navigate("/");
      return;
    }

    const token = searchParams.get("token");
    const activate = async (token: string) => {
      try {
        await activateUser(token);
        navigate({
          pathname: "/login",
          search: `${createSearchParams({ alert: "confirmed" })}`,
        });
      } catch (e) {
        // TODO: handle error
        navigate("/register");
      }
    };
    if (token) {
      activate(token);
    }
  }, [authed, searchParams, navigate]);

  if (!authed) {
    return null;
  }

  return (
    <Flex mt="xl" justify="center">
      <RegisterRestaurantForm />
    </Flex>
  );
}
