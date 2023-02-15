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
import { useNavigate } from "react-router-dom";
import { deleteRestaurantById, getRestaurantImage } from "../../api/services";
import { Restaurant } from "../../types";
import useStyles from "./UserRestaurantCard.styles";

interface UserRestaurantCardProps {
  restaurant: Restaurant;
  likedByUser?: boolean;
  onDelete?: (id?: string) => void;
}

export function UserRestaurantCard({
  restaurant,
  onDelete,
}: UserRestaurantCardProps) {
  const { id, image, name, rating, likes } = restaurant;
  const { classes, theme } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [opened, setOpened] = useState(false);
  const [imageUrl, setImageUrl] = useState<string>("");

  useEffect(() => {
    restaurant.id
      ? getRestaurantImage(restaurant.id).then((data) => {
          setImageUrl(data);
        })
      : console.log("");
  }, []);

  return (
    <Card withBorder p="lg" radius="md" className={classes.card}>
      <Card.Section mb="md">
        <Image
          src={image?.startsWith("http") ? image : imageUrl}
          alt={name}
          height={180}
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
          onClick={() => {
            if (restaurant.id === undefined) {
              console.log("null restaurant id");
            } else {
              deleteRestaurantById(restaurant.id).then((response) => {
                if (response === 202) {
                  if (onDelete) {
                    onDelete(restaurant.id);
                  }
                }
              });
            }
          }}
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
