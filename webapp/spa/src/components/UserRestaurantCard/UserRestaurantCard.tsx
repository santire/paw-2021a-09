import {
  ActionIcon,
  Badge,
  Button,
  Card,
  Flex,
  Grid,
  Group,
  Image,
  Modal,
  Text,
} from "@mantine/core";
import { IconHeart, IconShare, IconStar } from "@tabler/icons";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useMutation, useQueryClient, useQuery } from "react-query";
import { useNavigate } from "react-router-dom";
import { deleteRestaurantById, getRestaurantReservations, confirmReservation } from "../../api/services";
import { useAuth } from "../../context/AuthContext";
import { Restaurant, Reservation } from "../../types";
import useStyles from "./UserRestaurantCard.styles";
import ReservationCards from "./ReservationCards"


interface UserRestaurantCardProps {
  restaurant: Restaurant;
  likedByUser?: boolean;
}

export function UserRestaurantCard({ restaurant }: UserRestaurantCardProps) {
  const { id, image, name, rating, likes } = restaurant;
  const { classes, theme } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [opened, setOpened] = useState(false);
  const [openReservationModal, setOpenReservationModal] = useState(false)
  const { user } = useAuth();
  const userId = user?.userId;

  const { mutate, isLoading } = useMutation(deleteRestaurantById, {
    onSuccess: () => {
      setOpened(false);
      queryClient.invalidateQueries("ownedRestaurants");
    },
    onError: (error) => {
      console.log(error);
    },
    onSettled: () => {
      queryClient.invalidateQueries("ownedRestaurants");
    },
  });

  if (!id) {
    navigate("/");
    return <></>;
  }

  return (
    <Card withBorder p="lg" radius="md" className={classes.card}>
      <Card.Section mb="md">
        <Image src={image} alt={name} height={180} />
      </Card.Section>

      <Card.Section mt="xs" className={classes.section}>
        <Grid justify="space-between">
          <Grid.Col span={8}>
            <Text weight={700} className={classes.title} lineClamp={2}>
              {name}
            </Text>
          </Grid.Col>
          <Grid.Col span={4}>
            <Badge size="sm" color="yellow" py={10}>
              <Group spacing="xs">
                <Text weight={700} size="sm" align="center">
                  {rating}
                </Text>
                <Flex align="center" p={0} m={0}>
                  <IconStar
                    size={16}
                    color={theme.colors.yellow[6]}
                    fill={theme.colors.yellow[6]}
                  />
                </Flex>
              </Group>
            </Badge>
          </Grid.Col>
        </Grid>
      </Card.Section>

      <Flex justify="center" align="center">
        <Button
          color="orange"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => navigate(`/restaurants/${id}`)}
        >
          {t("UserRestaurantCard.more")}
        </Button>
      </Flex>

      <Flex justify="center" align="center">
        <Button
          color="orange"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => navigate(`/restaurants/${id}/reservations`)}
        >
          {t("UserRestaurantCard.viewReservations")}
        </Button>
      </Flex>

      {/** Modal de reserva */}
      <Modal
        centered
        opened={openReservationModal}
        onClose={() => setOpenReservationModal(false)}
        title="Reservas"
      >
        <ReservationCards id={id} closeModal={() => setOpenReservationModal(false)} />
      </Modal>

      <Flex justify="center" align="center" mt="md">
        <Button
          color="gray"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => navigate(`/restaurants/${id}/edit`)}
        >
          {t("UserRestaurantCard.edit")}
        </Button>
      </Flex>

      <Flex justify="center" align="center" mt="md">
        <Button
          color="red"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => setOpened(true)}
        >
          {t("UserRestaurantCard.delete")}
        </Button>
      </Flex>

      <Modal
        centered
        opened={opened}
        onClose={() => setOpened(false)}
        title={t("UserRestaurantCard.deleteModal.title")}
      >
        <Text mb={50} size={15}>
          {t("UserRestaurantCard.deleteModal.text")}
        </Text>
        <Button
          color="red"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => mutate(id!)}
          disabled={isLoading}
        >
          {t("UserRestaurantCard.deleteModal.confirm")}
        </Button>
      </Modal>

      <Card.Section className={classes.footer}>
        <Group position="apart">
          <Text size="xs" color="dimmed">
            {t("restaurantCard.liked", { likes: likes })}
          </Text>
        </Group>
      </Card.Section>
    </Card>
  );
}
