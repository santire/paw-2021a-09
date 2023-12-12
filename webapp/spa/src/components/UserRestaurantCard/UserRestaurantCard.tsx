import {
  Badge,
  Button,
  Card,
  Flex,
  Grid,
  Group,
  Image,
  Modal,
  Skeleton,
  Text,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import useStyles from "./UserRestaurantCard.styles";
import { useNavigate } from "react-router-dom";
import { IconStar } from "@tabler/icons-react";
import { ManageButton } from "../ManageButton/ManageButton";
import { useState } from "react";
import { useDeleteRestaurant } from "../../hooks/restaurant.hooks";

interface RestaurantCardProps {
  restaurant: IRestaurant;
}
export function UserRestaurantCard({ restaurant }: RestaurantCardProps) {
  const { id, image, name, rating, reservationsCount } = restaurant;
  const { classes, theme } = useStyles();
  const [opened, setOpened] = useState(false);
  const { t } = useTranslation();
  const navigate = useNavigate();

  const { mutate, isPending } = useDeleteRestaurant(restaurant);

  function handleMutation() {
    mutate(restaurant.self);
  }

  return (
    <Card withBorder p="lg" radius="md" className={classes.card}>
      <Card.Section mb="md">
        <Image
          src={image}
          alt={name}
          height={180}
          width={355}
          placeholder={<Skeleton height={180} width={355} />}
        />
      </Card.Section>

      <Card.Section mt="xs" className={classes.section}>
        <Grid justify="space-between">
          <Grid.Col span={8}>
            <Text weight={700} className={classes.title} lineClamp={2}>
              {name}
            </Text>
          </Grid.Col>
          <Grid.Col span={4}>
            <Flex align="center" justify="right">
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
            </Flex>
          </Grid.Col>
        </Grid>
      </Card.Section>

      <Flex justify="center" align="center">
        <Button
          color="cyan"
          variant="outline"
          size="sm"
          fullWidth
          onClick={() => navigate(`/restaurants/${id}`)}
        >
          {t("userRestaurantCard.more")}
        </Button>
      </Flex>
      <Flex justify="center" align="center" mt="md">
        <Button
          color="orange"
          variant="outline"
          size="sm"
          fullWidth
          onClick={() => navigate(`/restaurants/${id}/reservations`)}
        >
          {t("userRestaurantCard.viewReservations")}
        </Button>
      </Flex>
      <Flex justify="center" align="center" mt="md">
        <Button
          color="gray"
          variant="outline"
          size="sm"
          fullWidth
          onClick={() => navigate(`/restaurants/${id}/edit`)}
        >
          {t("userRestaurantCard.edit")}
        </Button>
      </Flex>
      <Flex justify="center" align="center" mt="md">
        <Button
          color="red"
          variant="outline"
          size="sm"
          fullWidth
          onClick={() => setOpened(true)}
        >
          {t("userRestaurantCard.delete")}
        </Button>
      </Flex>

      <Modal
        centered
        opened={opened}
        onClose={() => setOpened(false)}
        title={t("userRestaurantCard.deleteModal.title")}
      >
        <Text mb={50} size={15}>
          {t("userRestaurantCard.deleteModal.text")}
        </Text>
        <Button
          color="red"
          variant="outline"
          size="sm"
          fullWidth
          onClick={() => handleMutation()}
          disabled={isPending}
        >
          {t("userRestaurantCard.deleteModal.confirm")}
        </Button>
      </Modal>

      <Card.Section className={classes.footer}>
        <Group position="apart">
          <Text size="xs" color="yellow">
            {t("userRestaurantCard.pending", {
              reservationsCount: reservationsCount,
            })}
          </Text>
          <Group spacing={0}>
            <ManageButton restaurant={restaurant} />
          </Group>
        </Group>
      </Card.Section>
    </Card>
  );
}
