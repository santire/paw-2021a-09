import {
  ActionIcon,
  Badge,
  Button,
  Card,
  Flex,
  Grid,
  Group,
  Image,
  Text,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import useStyles from "./RestaurantCard.styles";
import { useNavigate } from "react-router-dom";
import { IconStar } from "@tabler/icons-react";
import { useGetTags } from "../../hooks/tags.hooks";

interface RestaurantCardProps {
  restaurant: IRestaurant;
}
export function RestaurantCard({ restaurant }: RestaurantCardProps) {
  const { id, image, tags, name, rating, likes, likedByUser } = restaurant;
  const { classes, theme } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const tagList = useGetTags();

  const features = tags?.map((tag, idx) => (
    <Badge
      component="a"
      onClick={() =>
        tagList.isSuccess
          ? navigate(`/restaurants?tags=${tagList.data.indexOf(tag)}`)
          : null
      }
      color="orange"
      key={tag + "" + idx}
      style={{ cursor: "pointer" }}
    >
      {t("tags." + tag.toLowerCase())}
    </Badge>
  ));

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
          {features}
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
            {likedByUser ? <em>si</em> : <em>no</em>}
            {/* <LikeButton */}
            {/*   restaurantId={restaurant.id!} */}
            {/*   authed={authed} */}
            {/*   isOwner={isOwner} */}
            {/*   userLikesRestaurant={likedByUser!} */}
            {/*   updateLikes={updateLikes} // Pass the updateLikes function as a prop */}
            {/*   likes={likes} // Pass the likes state as a prop */}
            {/* /> */}
          </Group>
        </Group>
      </Card.Section>
    </Card>
  );
}
