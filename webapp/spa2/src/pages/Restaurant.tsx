import {
  Text,
  Flex,
  Grid,
  Group,
  Image,
  createStyles,
  Divider,
  Tabs,
  Skeleton,
  Button,
  Loader,
  Center,
  Badge,
} from "@mantine/core";
import {
  IconCheck,
  IconMapPin,
  IconMenu,
  IconMessageCircle,
  IconPhone,
  IconStar,
} from "@tabler/icons-react";
import LikeButton from "../components/LikeButton/LikeButton";
import { useTranslation } from "react-i18next";
import { useGetRestaurant } from "../hooks/restaurant.hooks";
import { useNavigate, useParams } from "react-router-dom";
import { NotFoundPage } from "./NotFound";
import { IRestaurant } from "../types/restaurant/restaurant.models";
import { SocialsButton } from "../components/SocialsButton/SocialsButton";
import { TagButton } from "../components/TagButton/TagButton";
import { useIsOwner } from "../hooks/user.hooks";
import { ManageButton } from "../components/ManageButton/ManageButton";
import { RatingStars } from "../components/RatingStars/RatingStars";

const useStyles = createStyles((theme) => ({
  title: {
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    fontSize: theme.fontSizes.xl * 2,
    fontWeight: 700,
  },
  address: {
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    fontSize: theme.fontSizes.md,
  },
  socials: {
    backgroundColor:
      theme.colorScheme === "dark"
        ? theme.colors.dark[6]
        : theme.colors.gray[2],
    borderRadius: theme.radius.xl,
  },
  img: {
    maxHeight: 400,
  },
  tags: {
    minHeight: theme.spacing.xl * 2,
  },
  comment: {
    padding: `${theme.spacing.lg}px ${theme.spacing.xl}px`,
  },

  body: {
    paddingLeft: 0,
    paddingTop: theme.spacing.sm,
    fontSize: theme.fontSizes.sm,
  },

  content: {
    "& > p:last-child": {
      marginBottom: 0,
    },
  },
}));

export function RestaurantPage({ restaurantId }: { restaurantId: number }) {
  const { data, isLoading, isError } = useGetRestaurant(restaurantId);
  const { t } = useTranslation();

  if (isLoading) {
    return (
      <Flex direction="column" w="100%">
        <RestaurantSkeleton />
        <Divider m="xl" orientation="horizontal" />
        <Flex justify={"center"}>
          <Tabs
            defaultValue="Menu"
            style={{ width: "80%", justifyContent: "center" }}
          >
            <Tabs.List>
              <Tabs.Tab value="Menu" icon={<IconMenu size={14} />}>
                {t("pages.restaurant.menu.title")}
              </Tabs.Tab>
              <Tabs.Tab value="Reviews" icon={<IconMessageCircle size={14} />}>
                {t("pages.restaurant.reviews.title")}
              </Tabs.Tab>
            </Tabs.List>

            <Tabs.Panel value="Menu" pt="xs">
              <Center mt={50}>
                <Loader color="gray" variant="dots" size="xl" />
              </Center>
            </Tabs.Panel>

            <Tabs.Panel value="Reviews" pt="xs">
              <Center mt={50}>
                <Loader color="gray" variant="dots" size="xl" />
              </Center>
            </Tabs.Panel>
          </Tabs>
        </Flex>
      </Flex>
    );
  }
  if (!isLoading && !isError && data) {
    return (
      <Flex direction="column" w="100%">
        <RestaurantHeader restaurant={data} />
        <Divider m="xl" orientation="horizontal" />
        <Flex justify={"center"}>
          <Tabs
            defaultValue="Menu"
            style={{ width: "80%", justifyContent: "center" }}
          >
            <Tabs.List>
              <Tabs.Tab value="Menu" icon={<IconMenu size={14} />}>
                {t("pages.restaurant.menu.title")}
              </Tabs.Tab>
              <Tabs.Tab value="Reviews" icon={<IconMessageCircle size={14} />}>
                {t("pages.restaurant.reviews.title")}
              </Tabs.Tab>
            </Tabs.List>

            <Tabs.Panel value="Menu" pt="xs">
              <Center mt={50}>
                <Loader color="gray" variant="dots" size="xl" />
              </Center>
            </Tabs.Panel>

            <Tabs.Panel value="Reviews" pt="xs">
              <Center mt={50}>
                <Loader color="gray" variant="dots" size="xl" />
              </Center>
            </Tabs.Panel>
          </Tabs>
        </Flex>
      </Flex>
    );
  }

  return <NotFoundPage />;
}

function RestaurantHeader({ restaurant }: { restaurant: IRestaurant }) {
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
                  <IconMapPin size={18} color="gray" stroke={1.5} />
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
              <IconPhone size={18} color="gray" stroke={1.5} />
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
              {isOwner ? (
                <ManageButton restaurant={restaurant} />
              ) : (
                <LikeButton restaurant={restaurant} />
              )}
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
                <Button
                  mt="xl"
                  color="orange"
                  variant="outline"
                  size="xl"
                  onClick={() => navigate(`/restaurants/${restaurant.id}/edit`)}
                >
                  {t("userRestaurantCard.edit")}
                </Button>
              ) : (
                <Button
                  mt="xl"
                  color="orange"
                  variant="outline"
                  size="xl"
                  // onClick={toggleReservation}
                >
                  {t("pages.restaurant.reservationButton")}
                </Button>
              )}
            </Flex>
          </div>
        </Flex>
      </Grid.Col>
    </Grid>
  );
}

function RestaurantSkeleton() {
  const { classes } = useStyles();
  return (
    <Grid gutter="xl" justify="flex-start" m="xl" align="stretch">
      <Grid.Col span={5}>
        <Group position="center">
          <Skeleton height={"20vw"} width={"40vw"} />
        </Group>
      </Grid.Col>
      <Grid.Col span={7} px={"5%"}>
        <Flex direction="column" justify="space-between" h={"100%"}>
          <div>
            <Text align="left" className={classes.title}>
              <Skeleton height={36} mb={15} radius="xl" width="75%" />
            </Text>
            <Group spacing="xs" mb="xs">
              <IconMapPin size={18} color="gray" stroke={1.5} />
              <Text align="left" className={classes.address}>
                <Skeleton height={18} mt={4} radius="xl" width={"20vw"} />
              </Text>
            </Group>
            <Group spacing="xs" mb="xs">
              <IconPhone size={18} color="gray" stroke={1.5} />
              <Text align="left" className={classes.address}>
                <Skeleton height={18} mt={4} radius="xl" width={"15vw"} />
              </Text>
            </Group>
            <Flex className={classes.socials} maw={90} mt={20}>
              <Group spacing={0} px={3} align="center">
                <Skeleton height={18} mx={4} my={4} radius="xl" width={18} />
                <Skeleton height={18} mx={4} my={4} radius="xl" width={18} />
                <Skeleton height={18} mx={4} my={4} radius="xl" width={18} />
              </Group>
            </Flex>
            <Group spacing={20} px={3}>
              <Skeleton
                visible={false}
                height={30}
                mt={4}
                radius="xl"
                width="100%"
              />
            </Group>
            <Group spacing={7} mt="xs" className={classes.tags}>
              <Skeleton height={26} mt={4} radius="xl" width="100%" />
            </Group>
            <Flex direction="column" justify="space-between" h={"100%"}>
              <Button
                mt="xl"
                color="orange"
                variant="outline"
                size="xl"
                disabled={true}
              >
                <Skeleton
                  height={18}
                  mx={4}
                  my={4}
                  radius="xl"
                  width={"100%"}
                  visible={false}
                />
              </Button>
            </Flex>
          </div>
        </Flex>
      </Grid.Col>
    </Grid>
  );
}

export function ValidateRestaurant() {
  const params = useParams();
  const restaurantId = params?.restaurantId?.match(/\d+/);
  if (!restaurantId) {
    return <NotFoundPage />;
  }
  return <RestaurantPage restaurantId={parseInt(restaurantId[0])} />;
}
