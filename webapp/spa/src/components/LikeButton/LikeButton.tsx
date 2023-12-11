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
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import { useTranslation } from "react-i18next";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  useDislikeRestaurant,
  useGetLike,
  useLikeRestaurant,
} from "@/hooks/like.hooks";
import { useUser } from "@/hooks/user.hooks";

interface LikeButtonProps {
  restaurant: IRestaurant;
}

export function LikeButton({ restaurant }: LikeButtonProps) {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const theme = useMantineTheme();
  const [opened, setOpened] = useState(false);

  const user = useUser();
  const likeData = useGetLike(restaurant);

  const like = useLikeRestaurant(restaurant);
  const dislike = useDislikeRestaurant(restaurant);

  if (user.isSuccess) {
    const handleMutation = () => {
      if (likeData.isSuccess && likeData.data.like) {
        dislike.mutate({ url: likeData.data.like.self });
      } else {
        like.mutate({
          url: user.data.likes,
          restaurantId: restaurant.id,
        });
      }
    };
    return (
      <Box>
        <ActionIcon
          aria-label="like-button"
          loading={like.isPending || dislike.isPending}
          variant="transparent"
          onClick={handleMutation}
        >
          <IconHeart
            size={18}
            color={theme.colors.red[6]}
            fill={likeData.data?.liked ? "red" : "none"}
            stroke={1.5}
          />
        </ActionIcon>
      </Box>
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
          aria-label="like-button"
          variant="transparent"
          onClick={() => setOpened(true)}
        >
          <IconHeart
            size={18}
            color={theme.colors.red[6]}
            fill={"none"}
            stroke={1.5}
          />
        </ActionIcon>
      </Box>
    </>
  );
}

export default LikeButton;
