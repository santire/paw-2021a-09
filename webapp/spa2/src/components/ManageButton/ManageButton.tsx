import { IconCalendarCog } from "@tabler/icons-react";
import { ActionIcon, useMantineTheme } from "@mantine/core";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import { useNavigate } from "react-router-dom";

interface ManageButtonProps {
  restaurant: IRestaurant;
}

export function ManageButton({ restaurant }: ManageButtonProps) {
  const theme = useMantineTheme();
  const navigate = useNavigate();

  return (
    <ActionIcon variant="transparent">
      <IconCalendarCog
        size={20}
        color={theme.colors.yellow[6]}
        // fill={theme.colors.yellow[6]}
        stroke={1.5}
        onClick={() => navigate(`/restaurants/${restaurant.id}/reservations`)}
      />
    </ActionIcon>
  );
}
