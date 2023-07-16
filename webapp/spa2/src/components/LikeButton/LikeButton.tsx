import { IconHeart } from "@tabler/icons-react";
import {
  ActionIcon,
  Box,
  Text,
  Modal,
  useMantineTheme,
  Button,
  Center,
} from "@mantine/core";
import {
  useDislikeRestaurant,
  useLikeRestaurant,
} from "../../hooks/restaurant.hooks";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import { useAuth } from "../../hooks/useAuth";
import { useTranslation } from "react-i18next";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

interface LikeButtonProps {
  restaurant: IRestaurant;
}

export function LikeButton({ restaurant }: LikeButtonProps) {
  const { id, likedByUser } = restaurant;
  const { isAuthenticated } = useAuth();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const theme = useMantineTheme();
  const [opened, setOpened] = useState(false);

  const like = useLikeRestaurant();
  const dislike = useDislikeRestaurant();

  const mutation = likedByUser ? dislike : like;

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
            {t("restaurantCard.likeTooltip")}
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
      <Box>
        <ActionIcon
          loading={mutation.isLoading}
          variant="transparent"
          onClick={() =>
            isAuthenticated ? mutation.mutate(id) : setOpened(true)
          }
        >
          <IconHeart
            size={18}
            color={theme.colors.red[6]}
            fill={likedByUser ? "red" : "none"}
            stroke={1.5}
          />
        </ActionIcon>
      </Box>
    </>
  );
}

export default LikeButton;
