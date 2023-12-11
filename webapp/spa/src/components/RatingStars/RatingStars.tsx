import { Text, Center, Modal, Rating, Button } from "@mantine/core";
import { useIsOwner, useUser } from "@/hooks/user.hooks";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
// import { useRateRestaurant } from "../../hooks/restaurant.hooks";
// import { useGetRating } from "../../hooks/ratings.hooks";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useGetRating, useRateRestaurant } from "@/hooks/rating.hooks";
import { restaurants } from "@/queries/restaurant";

export function RatingStars({ restaurant }: { restaurant: IRestaurant }) {
  const { t } = useTranslation();
  const user = useUser();
  const isOwner = useIsOwner({ restaurant });
  const { mutate, isPending } = useRateRestaurant(restaurant);
  const userRating = useGetRating(restaurant);
  const navigate = useNavigate();
  const [opened, setOpened] = useState(false);
  if (isOwner) {
    return <></>;
  }

  if (user.isSuccess) {
    const handleMutation = (rating: number) => {
      mutate({ url: user.data.ratings, restaurantId: restaurant.id, rating });
    };

    return (
      <Rating
        value={
          userRating.isSuccess && userRating.data.rating
            ? userRating.data.rating.rating
            : 0
        }
        readOnly={isPending}
        onChange={(value) => handleMutation(value)}
      />
    );
  }
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
