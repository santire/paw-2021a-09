import {
  Text,
  Flex,
  Grid,
  Group,
  Image,
  Skeleton,
  Button,
  Badge,
} from "@mantine/core";
import { IconMapPin, IconPhone, IconStar } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import useStyles from "./Restaurant.styles";
import { useIsOwner } from "@/hooks/user.hooks";
import { useNavigate } from "react-router-dom";
import { IRestaurant } from "@/types/restaurant/restaurant.models";
import { SocialsButton } from "@/components/SocialsButton/SocialsButton";
import LikeButton from "@/components/LikeButton/LikeButton";
import { RatingStars } from "@/components/RatingStars/RatingStars";
import { TagButton } from "@/components/TagButton/TagButton";
import { ReservationButton } from "@/components/ReservationButton/ReservationButton";

export function RestaurantHeader({ restaurant }: { restaurant: IRestaurant }) {
  const { classes, theme } = useStyles();
  const { t } = useTranslation();
  const isOwner = useIsOwner({ restaurant });
  const navigate = useNavigate();

  const {
    name,
    address,
    image,
    phoneNumber,
    twitter,
    facebook,
    instagram,
    tags,
    likes,
    rating,
  } = restaurant;
  return (
    <Grid gutter="xl" justify="flex-start" m="xl" align="stretch">
      <Grid.Col span={5}>
        <Group position="center">
          <Image
            src={image}
            height={"20.7vw"}
            width={"40vw"}
            withPlaceholder
            placeholder={<Skeleton height={"20vw"} width={"40vw"} />}
          />
        </Group>
      </Grid.Col>
      <Grid.Col span={7} px={"5%"}>
        <Flex direction="column" justify="space-between" h={"100%"}>
          <div>
            <Text align="left" className={classes.title}>
              {name}
            </Text>
            <Grid justify="space-between">
              <Grid.Col span={8}>
                <Group spacing="xs" mb="xs">
                  <IconMapPin size={18} color="orange" stroke={1.5} />
                  <Text align="left" className={classes.address}>
                    {address}
                  </Text>
                </Group>
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
            <Group spacing="xs" mb="xs">
              <IconPhone size={18} color="orange" stroke={1.5} />
              <Text align="left" className={classes.address}>
                {phoneNumber}
              </Text>
            </Group>
            <Flex className={classes.socials} maw={90} mt={20} mb={10}>
              <Group spacing={0} px={3} align="center">
                {twitter ? (
                  <SocialsButton type="twitter" url={twitter} />
                ) : null}
                {facebook ? (
                  <SocialsButton type="facebook" url={facebook} />
                ) : null}
                {instagram ? (
                  <SocialsButton type="instagram" url={instagram} />
                ) : null}
              </Group>
            </Flex>

            <Group spacing={20} px={3}>
              {isOwner ? null : <LikeButton restaurant={restaurant} />}
              <RatingStars restaurant={restaurant} />
            </Group>

            <Group spacing={20} px={3} my={"xl"}>
              {tags.map((t) => (
                <TagButton key={t} tag={t} />
              ))}
            </Group>
            <Text size="xs" color="dimmed" px={6}>
              {t("restaurantCard.liked", { likes: likes })}
            </Text>

            <Flex direction="column" justify="space-between" h={"100%"}>
              {isOwner ? (
                <Flex direction="column" align="center" justify="space-between">
                  <Button
                    mt="xl"
                    color="gray"
                    variant="outline"
                    size="lg"
                    w="100%"
                    onClick={() =>
                      navigate(`/restaurants/${restaurant.id}/edit`)
                    }
                  >
                    {t("userRestaurantCard.edit")}
                  </Button>
                  <Button
                    mt="sm"
                    color="orange"
                    variant="outline"
                    size="lg"
                    w="100%"
                    onClick={() =>
                      navigate(`/restaurants/${restaurant.id}/reservations`)
                    }
                  >
                    {t("userRestaurantCard.viewReservations")}
                  </Button>
                </Flex>
              ) : (
                <ReservationButton restaurant={restaurant} />
              )}
            </Flex>
          </div>
        </Flex>
      </Grid.Col>
    </Grid>
  );
}
