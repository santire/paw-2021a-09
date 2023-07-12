import React, { useState } from "react";
import { IconHeart } from "@tabler/icons";
import { useMutation, useQueryClient } from "react-query";
import { likeRestaurant, dislikeRestaurant } from "../../api/services";
import { ActionIcon, createStyles } from "@mantine/core";

const useStyles = createStyles((theme) => ({
  }));

interface LikeButtonProps {
  restaurantId: string;
  authed?: boolean;
  userLikesRestaurant: boolean;
  updateLikes: (liked: boolean) => void; 
  likes: number;
  isOwner?: boolean;
}

const LikeButton: React.FC<LikeButtonProps> = ({ restaurantId, authed, userLikesRestaurant, isOwner, updateLikes,
    likes}) => {
  const queryClient = useQueryClient();
  const { classes, theme } = useStyles();
  const [disabled, setDisabled] = useState(isOwner || !authed); // Update the disabled state
  const [liked, setLiked] = useState(userLikesRestaurant);

  const { mutate, status } = useMutation(
    liked ? dislikeRestaurant : likeRestaurant,
    {
      onSuccess: () => {
        queryClient.invalidateQueries("like");
        setLiked(!liked);
        updateLikes(!liked); // Call the updateLikes function with the new liked value
      },
    }
  );

  const handleLikeClick = () => {
    if (isOwner) {
      return;
    }

    if (authed) {
        mutate(restaurantId); // Call the mutate function with the restaurantId
    }
  };

  return (
    <ActionIcon disabled={disabled}>
      <IconHeart
        size={18}
        color={theme.colors.red[6]}
        fill={liked ? "red" : "none"}
        stroke={1.5}
        onClick={handleLikeClick}
      />
    </ActionIcon>
  );
};

export default LikeButton;