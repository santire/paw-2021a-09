import {
  ActionIcon,
  Badge,
  Button,
  Card,
  Flex,
  Group,
  Image,
  Text,
} from "@mantine/core";
import { IconHeart, IconShare, IconStar } from "@tabler/icons";
import { useTranslation } from "react-i18next";
import useStyles from "./RestaurantCard.styles";

interface RestaurantCardProps {
  image: string;
  tags: string[];
  name: string;
  rating: number;
  likes: number;
}

export function RestaurantCard({
  image,
  tags,
  name,
  rating,
  likes,
}: RestaurantCardProps) {
  const { classes, theme } = useStyles();
  const { t } = useTranslation();

  const features = tags.map((tag, idx) => (
    <Badge color="orange" key={tag + "" + idx}>
      {tag}
    </Badge>
  ));

  return (
    <Card withBorder p="lg" radius="md" className={classes.card}>
      <Card.Section mb="xl">
        <Image
          src={
            image.startsWith("http") ? image : require(`../../assets/${image}`)
          }
          alt={name}
          height={180}
        />
      </Card.Section>

      <Card.Section mt="sm" className={classes.section}>
        <Group position="apart">
          <Text weight={700} className={classes.title}>
            {name}
          </Text>
          <Badge size="lg" color="yellow">
            <Group spacing="xs">
              <Text weight={700} size="sm" align="center">
                {rating}
              </Text>
              <Flex align="center" p={0} m={0}>
                <IconStar
                  size={18}
                  color={theme.colors.yellow[6]}
                  fill={theme.colors.yellow[6]}
                />
              </Flex>
            </Group>
          </Badge>
        </Group>
      </Card.Section>

      <Card.Section className={classes.section}>
        <Text mt="md" className={classes.label} color="dimmed">
          {t("restaurantCard.tagsTitle")}
        </Text>
        <Group spacing={7} mt={5}>
          {features}
        </Group>
      </Card.Section>

      <Flex justify="center" align="center" mt="md">
        <Button color="orange" variant="outline" size="md" fullWidth>
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
              <IconHeart size="md" color={theme.colors.red[6]} stroke={1.5} />
            </ActionIcon>
            <ActionIcon>
              <IconShare size={16} color={theme.colors.blue[6]} stroke={1.5} />
            </ActionIcon>
          </Group>
        </Group>
      </Card.Section>
    </Card>
  );
}
