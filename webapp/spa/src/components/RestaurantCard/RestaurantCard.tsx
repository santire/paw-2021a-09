import {
  Badge,
  Button,
  Card,
  Flex,
  Grid,
  Group,
  Image,
  Skeleton,
  Text,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import useStyles from "./RestaurantCard.styles";
import { useNavigate } from "react-router-dom";
import { IconStar } from "@tabler/icons-react";
import { LikeButton } from "../LikeButton/LikeButton";
import { useIsOwner } from "@/hooks/user.hooks";
import { ManageButton } from "../ManageButton/ManageButton";
import { TagButton } from "../TagButton/TagButton";
import { queries } from "@/queries";
import { useQuery } from "@tanstack/react-query";

interface RestaurantCardProps {
  restaurant: IRestaurant;
}
export function RestaurantCard({ restaurant }: RestaurantCardProps) {
  const rest = useQuery({
    ...queries.restaurants.detail(restaurant.id),
    initialData: restaurant,
  });
  const { id, image, tags, name, rating, likes } = rest.data;
  const { classes, theme } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const isOwner = useIsOwner({ restaurant });

  return (
    <Card withBorder p="lg" radius="md" className={classes.card}>
      <Card.Section mb="md">
        <Image
          src={image}
          alt={name}
          height={180}
          width={355}
          withPlaceholder
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

      <Card.Section className={classes.section}>
        <Text className={classes.label} color="dimmed">
          {t("restaurantCard.tagsTitle")}
        </Text>
        <Group spacing={7} mt="xs" className={classes.tags}>
          {tags.map((t) => (
            <TagButton key={t} tag={t} />
          ))}
        </Group>
      </Card.Section>

      <Flex justify="center" align="center" mt="md">
        <Button
          color="orange"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => navigate(`/restaurants/${id}`)}
        >
          {t("restaurantCard.more")}
        </Button>
      </Flex>

      <Card.Section className={classes.footer}>
        <Group position="apart">
          <Text size="xs" color="dimmed">
            {t("restaurantCard.liked", { likes: likes })}
          </Text>
          <Group spacing={0}>
            {isOwner ? (
              <ManageButton restaurant={restaurant} />
            ) : (
              <LikeButton restaurant={restaurant} />
            )}
          </Group>
        </Group>
      </Card.Section>
    </Card>
  );
}
