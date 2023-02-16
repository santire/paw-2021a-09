import {
  Badge,
  Button,
  Card,
  Flex,
  Grid,
  Group,
  Image,
  Modal,
  Text,
  Textarea,
} from "@mantine/core";
import { IconDeviceWatch, IconUser } from "@tabler/icons";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useMutation, useQueryClient } from "react-query";
import { useNavigate } from "react-router-dom";
import {
  cancelReservation,
  confirmReservation,
  denyReservation,
} from "../../api/services";
import { useAuth } from "../../context/AuthContext";
import { useRestaurant } from "../../hooks/useRestaurant";
import { Reservation, Restaurant } from "../../types";
import useStyles from "./ReservationCard.styles";

interface ReservationCardProps {
  reservation: Reservation;
}

export function ReservationCard({ reservation }: ReservationCardProps) {
  const { id, restaurant, quantity, date, confirmed } = reservation;
  const { classes } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [opened, setOpened] = useState(false);
  const [message, setMessage] = useState("");
  const { user } = useAuth();
  const userId = user?.userId;

  const arr = restaurant.owner?.split("/").slice(-1);
  const isOwner = user?.userId?.toString() === arr![0];

  const denyMutation = useMutation(
    ["denyReservation", id],
    async (message: string) =>
      denyReservation(restaurant.id || "", id || "", message),
    {
      onSuccess: () => {
        setOpened(false);
        queryClient.invalidateQueries("reservations");
      },
      onError: (error) => {
        console.log(error);
      },
      onSettled: () => {
        queryClient.invalidateQueries("reservations");
      },
    }
  );

  const cancelMutation = useMutation(
    ["cancelReservation", id],
    async () => cancelReservation(restaurant.id || "", id || ""),
    {
      onSuccess: () => {
        setOpened(false);
        queryClient.invalidateQueries("reservations");
      },
      onError: (error) => {
        console.log(error);
      },
      onSettled: () => {
        queryClient.invalidateQueries("reservations");
      },
    }
  );

  const confirmMutation = useMutation(
    ["confirmReservation", id],
    async () => confirmReservation(restaurant.id || "", id || ""),
    {
      onSuccess: () => {
        setOpened(false);
        queryClient.invalidateQueries("reservations");
      },
      onError: (error) => {
        console.log(error);
      },
      onSettled: () => {
        queryClient.invalidateQueries("reservations");
      },
    }
  );

  const AdminButtons = () => {
    return (
      <Flex justify="center" align="center" mt="md" direction="column">
        <Button
          color="lime"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => confirmMutation.mutate()}
        >
          {t("pages.reservations.accept")}
        </Button>
        <Button
          color="red"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => setOpened(true)}
        >
          {t("pages.reservations.deny")}
        </Button>
      </Flex>
    );
  };

  const UserButton = () => {
    return (
      <Flex justify="center" align="center" mt="md" direction="column">
        <Button
          color="red"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => cancelMutation.mutate()}
        >
          {t("pages.reservations.cancel")}
        </Button>
      </Flex>
    );
  };

  return (
    <Card withBorder p="lg" radius="md" className={classes.card}>
      <Card.Section mb="md">
        <Image src={restaurant.image} alt={restaurant.name} height={180} />
      </Card.Section>

      <Card.Section mt="xs" className={classes.section}>
        <Grid justify="space-between">
          <Grid.Col span={8}>
            <Text weight={700} className={classes.title} lineClamp={2}>
              {restaurant.name}
            </Text>
            <Group>
              <IconDeviceWatch />
              <Text className={classes.tags} lineClamp={2}>
                {date}
              </Text>
            </Group>
            <Group>
              <IconUser />
              <Text className={classes.tags} lineClamp={2}>
                {quantity}
              </Text>
            </Group>
          </Grid.Col>
          <Grid.Col span={4}>
            <Badge size="sm" color={confirmed ? "green" : "yellow"} py={10}>
              <Group spacing="xs">
                <Text weight={700} size="sm" align="center">
                  {confirmed
                    ? t("pages.reservations.confirmed")
                    : t("pages.reservations.pending")}
                </Text>
              </Group>
            </Badge>
          </Grid.Col>
        </Grid>
      </Card.Section>
      {isOwner ? <AdminButtons /> : <UserButton />}

      <Modal
        centered
        opened={opened}
        onClose={() => setOpened(false)}
        title={t("UserRestaurantCard.deleteModal.title")}
      >
        <Text mb={50} size={15}>
          {t("UserRestaurantCard.deleteModal.text")}
        </Text>
        <Textarea
          label={t("pages.reservations.deny.label")}
          my="sm"
          value={message}
          onChange={(e) => setMessage(e.currentTarget.value)}
        />
        <Button
          color="red"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => denyMutation.mutate(message)}
        >
          {t("UserRestaurantCard.deleteModal.confirm")}
        </Button>
      </Modal>
    </Card>
  );
}
