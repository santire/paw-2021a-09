import { Text, Center, Modal, Rating, Button } from "@mantine/core";
import { useAuth } from "../../hooks/useAuth";
import { useIsOwner } from "../../hooks/user.hooks";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import { useRateRestaurant } from "../../hooks/restaurant.hooks";
import { useGetRating } from "../../hooks/ratings.hooks";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useTranslation } from "react-i18next";

export function RatingStars({ restaurant }: { restaurant: IRestaurant }) {
  const { t } = useTranslation();
  const isOwner = useIsOwner({ restaurant });
  const { isAuthenticated } = useAuth();
  const { mutate, isLoading } = useRateRestaurant();
  const userRating = useGetRating(restaurant.id);
  const navigate = useNavigate();
  const [opened, setOpened] = useState(false);
  if(isOwner) {
    return <></>;
  }

  if (!isAuthenticated) {
    return (
      <>
        <Modal
          title={"Oops!"}
          opened={opened}
          onClose={() => setOpened(false)}
          centered
        >
          <Center>
            <Text size="md" mb="xl">
              {t("restaurantCard.rateTooltip")}
            </Text>
          </Center>

          <Button
            color="orange"
            variant="outline"
            size="md"
            fullWidth
            onClick={() => navigate("/login")}
          >
            {t("pages.login.signIn")}
          </Button>
        </Modal>
        <Rating
          value={0}
          onChange={() => {
            setOpened(true);
          }}
        />
      </>
    );
  }
  return (
    <Rating
      value={userRating.data?.rating ?? 0}
      readOnly={isLoading}
      onChange={(value) =>
        mutate({ restaurantId: restaurant.id, rating: value })
      }
    />
  );
}
