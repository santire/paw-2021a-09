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
import { IconHeart, IconShare, IconStar } from "@tabler/icons";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { Restaurant } from "../../types";
import useStyles from "./RestaurantCard.styles";
import {useState} from "react";
import {TAGS} from "../Filter/Filter";

interface RestaurantCardProps {
  restaurant: Restaurant;
  likedByUser?: boolean;
}

export function RestaurantCard({
  restaurant,
  likedByUser,
}: RestaurantCardProps) {
  const { id, image, tags, name, rating, likes } = restaurant;
  const { classes, theme } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { t: en } = useTranslation();
  const { t: es } = useTranslation("es");
  const getOptionsArr = (keyword: string) => {
    return [en(keyword), es(keyword)];
  };

  const getTagValue = (values: string[]) => {
    const tagOptions: { [key: string]: string[] } = {};
    for (let tag of TAGS) {
      console.log("ag: " + es(`tags.${tag}`));
      tagOptions[tag] = getOptionsArr(`tags.${tag}`);
    }
    const toReturn: string[] = [];
    Object.entries(tagOptions).forEach(([_, val], index) => {
      for (let tagVal of values) {
        if (val.map((t) => t.toLowerCase()).includes(tagVal.toLowerCase())) {
          toReturn.push("" + index);
        }
      }
    });

    return toReturn;
  };

  const features = tags?.map((tag, idx) => (
        <Badge component="a" onClick={() => navigate(`/restaurants?tags=${getTagValue([t("tags." + tag.toLowerCase()).toLowerCase()])}`)} color="orange" key={tag + "" + idx} style={{ cursor: 'pointer' }}>
        {t("tags." + tag.toLowerCase())}
        </Badge>
));

  return (
    <Card withBorder p="lg" radius="md" className={classes.card}>
      <Card.Section mb="md">
        <Image
          src={
            image?.startsWith("http") ? image : require(`../../assets/${image}`)
          }
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
            <ActionIcon>
              <IconHeart size={18} color={theme.colors.red[6]} stroke={1.5} />
            </ActionIcon>
            <ActionIcon>
              <IconShare size={18} color={theme.colors.blue[6]} stroke={1.5} />
            </ActionIcon>
          </Group>
        </Group>
      </Card.Section>
    </Card>
  );
}
