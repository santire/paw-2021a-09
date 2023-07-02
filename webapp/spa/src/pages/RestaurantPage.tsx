import {
  ActionIcon,
  Badge,
  Button,
  createStyles,
  Divider,
  Flex,
  Grid,
  Group,
  Image,
  Loader,
  Modal,
  NumberInput,
  Tabs,
  Text,
} from "@mantine/core";
import {
  IconBrandFacebook,
  IconBrandInstagram,
  IconBrandTwitter,
  IconClock,
  IconHeart,
  IconMapPin,
  IconMenu,
  IconMessageCircle,
  IconPhone,
} from "@tabler/icons";
import { DatePicker, TimeInput } from "@mantine/dates";
import { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { Rating } from "@mantine/core";
import { useQuery, useQueryClient } from "react-query";
import { useNavigate, useParams } from "react-router-dom";
import {  makeReservation,
  ReservationForm,
  dislikeRestaurant,
  getRestaurantLike,
  getRestaurantRating,
  likeRestaurant,
  rateRestaurant,
} from "../api/services";
import { useRestaurant } from "../hooks/useRestaurant";
import { useAuth } from "../context/AuthContext";
import { Like } from "../types/Like";
import { Rate } from "../types/Rate";
import { MenuItems } from "../components/MenuItems/MenuItems";
import { Reviews } from "../components/Reviews/Reviews";
import {TAGS} from "../components/Filter/Filter";

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

export function RestaurantPage() {
  const { classes, theme } = useStyles();
  const queryClient = useQueryClient();
  const { restaurantId } = useParams();
  const { t } = useTranslation();
  const ref = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const { authed, user } = useAuth();
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
  const {
    status: restaurantStatus,
    data: restaurantData,
    error: restaurantError,
  } = useRestaurant(restaurantId || "");

  const {
    status: likeStatus,
    data: likeData,
    error: likeError,
  } = useQuery<Like, Error>(
    ["like"],
    async () => getRestaurantLike(restaurantId || "", user?.userId || ""),
    { retry: false }
  );
  const {
    status: rateStatus,
    data: rateData,
    error: rateError,
  } = useQuery<Rate, Error>(
    ["rating"],
    async () => getRestaurantRating(restaurantId || "", user?.userId || ""),
    { retry: false }
  );

  const [quantity, setQuantity] = useState<number>(1);
  const [reservationModal, setReservationModal] = useState(false);
  const [date, setDate] = useState(new Date());
  const [time, setTime] = useState(new Date());

  if (!restaurantId) {
    navigate("/");
    return <></>;
  }

  if (restaurantStatus === "loading" || !restaurantData) {
    return (
      <Flex justify="center" align="center" h={"100vh"}>
        <Loader color="orange" />
      </Flex>
    );
  }

  const arr = restaurantData.owner?.split("/").slice(-1);
  const isOwner = user?.userId?.toString() === arr![0];

  const {
    image,
    name,
    address,
    phoneNumber,
    facebook,
    twitter,
    instagram,
    tags,
  } = restaurantData;

  const SocialsButton = ({ type, url }: { type: string; url: string }) => {
    switch (type) {
      case "twitter": {
        return (
          <ActionIcon>
            <IconBrandTwitter
              size={18}
              color={theme.colors.blue[3]}
              stroke={1.5}
              onClick={() => window.open("http://" + url, "_blank")}
            />
          </ActionIcon>
        );
      }
      case "instagram": {
        return (
          <ActionIcon>
            <IconBrandInstagram
              size={18}
              color={theme.colors.pink[6]}
              stroke={1.5}
              onClick={() => window.open("http://" + url, "_blank")}
            />
          </ActionIcon>
        );
      }
      case "facebook": {
        return (
          <ActionIcon>
            <IconBrandFacebook
              size={18}
              color={theme.colors.blue[6]}
              stroke={1.5}
              onClick={() => window.open("http://" + url, "_blank")}
            />
          </ActionIcon>
        );
      }
      default: {
        return <></>;
      }
    }
  };

  const features = tags?.map((tag, idx) => (
    <Badge component="a" onClick={() => navigate(`/restaurants?tags=${getTagValue([t("tags." + tag.toLowerCase()).toLowerCase()])}`)} color="orange" key={tag + "" + idx} style={{ cursor: 'pointer' }}>
      {t("tags." + tag.toLowerCase())}
    </Badge>
  ));

  const handleReservation = () => {
    if (restaurantId === undefined) {
      console.log("null restaurant id");
    } else {
      const day = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
      const realMonth = date.getMonth() + 1;
      const month = realMonth < 10 ? `0${realMonth}` : realMonth;
      const hours = time.toLocaleTimeString("es");

      const form: ReservationForm = {
        date: `${day}/${month}/${date.getFullYear()}`,
        time: hours.slice(0, 5),
        quantity: quantity,
      };
      makeReservation(restaurantId, form).then((status) => {
        if (status === 201 || status === 204) {
          setReservationModal(false);
        }
      });
    }
  };

  const LikeButton = () => {
    if (authed && !isOwner) {
      return (
        <ActionIcon>
          <IconHeart
            size={18}
            color={theme.colors.red[6]}
            stroke={1.5}
            fill={likeData?.liked ? theme.colors.red[6] : "none"}
            onClick={() => {
              if (isOwner) {
                return;
              }
              if (likeData?.liked === false) {
                likeRestaurant(restaurantId || "").then(() => {
                  queryClient.invalidateQueries("like");
                });
              } else if (likeData?.liked === true) {
                dislikeRestaurant(restaurantId || "").then(() => {
                  queryClient.invalidateQueries("like");
                });
              }
            }}
          />
        </ActionIcon>
      );
    }
    if (isOwner) {
      <IconHeart
        size={18}
        color={theme.colors.red[6]}
        stroke={1.5}
        fill={likeData?.liked ? theme.colors.red[6] : "none"}
        onClick={() => {
          if (isOwner) {
            return;
          }
          if (likeData?.liked === false) {
            likeRestaurant(restaurantId || "").then(() => {
              queryClient.invalidateQueries("like");
            });
          } else if (likeData?.liked === true) {
            dislikeRestaurant(restaurantId || "").then(() => {
              queryClient.invalidateQueries("like");
            });
          }
        }}
      />;
    }
    return <></>;
  };

  const RatingStars = () => {
    if (isOwner || !authed) {
      return (
        <Rating
          readOnly
          defaultValue={restaurantData.rating}
          onChange={(value) => {
            rateRestaurant(restaurantId, { rating: value }).then(() => {
              queryClient.invalidateQueries("rating");
            });
          }}
        />
      );
    } else {
      return (
        <Rating
          defaultValue={rateData?.rating}
          onChange={(value) => {
            rateRestaurant(restaurantId, { rating: value }).then(() => {
              queryClient.invalidateQueries("rating");
            });
          }}
        />
      );
    }
  };


  return (
    <>
      <Modal
        centered
        opened={reservationModal}
        onClose={() => setReservationModal(false)}
        title={t("pages.restaurant.reservationButton")}
      >
        <Text mb={50} size={15}>
          {t("pages.userReservations.partySize")}
        </Text>

        <NumberInput
          label={t("pages.userReservations.guests")}
          withAsterisk
          defaultValue={quantity | 0}
          value={quantity | 0}
          onChange={(value: number) => setQuantity(value || 0)}
        />

        <DatePicker
          placeholder="Pick date"
          label={t("pages.userReservations.reservationDate")}
          withAsterisk
          value={date}
          onChange={(value: Date) => setDate(new Date(value))}
        />

        <TimeInput
          label="Pick time"
          format="24"
          defaultValue={new Date()}
          onChange={(value: Date) => setTime(new Date(value))}
        />
        <TimeInput
          label="Click icon to show browser picker"
          ref={ref}
          rightSection={
            <ActionIcon onClick={() => {
              ref.current?.showPicker()
              console.log("TIME PICKER! " + ref.current ? ref.current?.showPicker() : "null pa");
            }
            }>
              <IconClock size="1rem" stroke={1.5} />
            </ActionIcon>
          }
          maw={400}
          mx="auto"
        />

        <Button
          color="orange"
          variant="outline"
          size="md"
          mt="xl"
          fullWidth
          onClick={handleReservation}
        >
          {t("pages.userReservations.confirm")}
        </Button>
      </Modal>
      <Grid gutter="xl" justify="flex-start" m="xl" align="stretch">
        <Grid.Col span={5}>
          <Group position="center">
            <Image src={image} alt={name} fit="cover" maw={700} />
          </Group>
        </Grid.Col>
        <Grid.Col span={7} px={"5%"}>
          <Flex direction="column" justify="space-between" h={"100%"}>
            <div>
              <Text align="left" className={classes.title}>
                {name}
              </Text>
              <Group spacing="xs" mb="xs">
                <IconMapPin size={18} color="orange" stroke={1.5} />
                <Text align="left" className={classes.address}>
                  {address}
                </Text>
              </Group>
              <Group spacing="xs" mb="xs">
                <IconPhone size={18} color="orange" stroke={1.5} />
                <Text align="left" className={classes.address}>
                  {phoneNumber}
                </Text>
              </Group>
              <Flex className={classes.socials} maw={90}>
                <Group spacing={0} px={3}>
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
                <LikeButton />
                <RatingStars />
              </Group>
              <Group spacing={7} mt="xs" className={classes.tags}>
                {features}
              </Group>
              <Flex direction="column" justify="space-between" h={"100%"}>
                <Button
                  mt="xl"
                  color="orange"
                  variant="outline"
                  size="xl"
                  disabled={isOwner}
                  onClick={() => setReservationModal(true)}
                >
                  {t("pages.restaurant.reservationButton")}
                </Button>
              </Flex>
            </div>
          </Flex>
        </Grid.Col>

        <Grid.Col span={12}>
          <Divider m="xl" orientation="horizontal" />
        </Grid.Col>

        <Grid.Col span={12}>
          <Flex justify={"center"}>
            <Tabs
              defaultValue="Menu"
              style={{ width: "80%", justifyContent: "center" }}
            >
              <Tabs.List>
                <Tabs.Tab value="Menu" icon={<IconMenu size={14} />}>
                  {t("pages.restaurant.menu.title")}
                </Tabs.Tab>
                <Tabs.Tab
                  value="Reviews"
                  icon={<IconMessageCircle size={14} />}
                >
                  {t("pages.restaurant.reviews.title")}
                </Tabs.Tab>
              </Tabs.List>

              <Tabs.Panel value="Menu" pt="xs">
                <MenuItems restaurantId={restaurantId} isOwner={isOwner} />
              </Tabs.Panel>

              <Tabs.Panel value="Reviews" pt="xs">
                <Reviews restaurantId={restaurantId} isOwner={isOwner} />
              </Tabs.Panel>
            </Tabs>
          </Flex>
        </Grid.Col>
      </Grid>
    </>
  );
}
