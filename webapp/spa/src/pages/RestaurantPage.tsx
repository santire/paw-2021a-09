import {
  ActionIcon,
  Avatar,
  Badge,
  Box,
  Button,
  createStyles,
  Divider,
  Flex,
  Grid,
  Group,
  Image,
  Input,
  Loader,
  Modal,
  NumberInput,
  Paper,
  Table,
  Tabs,
  Text,
  Textarea,
  TypographyStylesProvider,
} from "@mantine/core";
import {
  IconBrandFacebook,
  IconBrandInstagram,
  IconBrandTwitter,
  IconHeart,
  IconMapPin,
  IconMenu,
  IconMessageCircle,
  IconPhone,
  IconPhoto,
  IconSettings,
  IconUser,
} from "@tabler/icons";
import { DatePicker, TimeInput } from "@mantine/dates";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Rating } from "@mantine/core";
import { useQuery, useQueryClient } from "react-query";
import { useNavigate, useParams } from "react-router-dom";
import {
  reviewRestaurant,
  makeReservation,
  ReservationForm,
  addMenuItem,
  dislikeRestaurant,
  getRestaurantLike,
  getRestaurantMenu,
  getRestaurantRating,
  getRestaurantReviews,
  getRestaurants,
  likeRestaurant,
  rateRestaurant,
} from "../api/services";
import { useRestaurant } from "../hooks/useRestaurant";
import { Restaurant } from "../types";
import { MenuItem } from "../types/MenuItem";
import { Page } from "../types/Page";
import { Review } from "../types/Review";
import { useAuth } from "../context/AuthContext";
import { Like } from "../types/Like";
import { Rate } from "../types/Rate";

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
  const navigate = useNavigate();
  const { authed, user } = useAuth();
  const {
    status: restaurantStatus,
    data: restaurantData,
    error: restaurantError,
  } = useRestaurant(restaurantId || "");
  const [menuStatus, setMenuStatus] = useState("idle");
  const [menuError, setMenuError] = useState<Error>();
  const [rows, setRows] = useState<React.ReactElement[]>([]);
  const [reviewRows, setReviewRows] = useState<Review[]>([]);

  const [reviewText, setReviewText] = useState("");
  const [menuItemName, setMenuItemName] = useState("");
  const [menuItemDescription, setMenuItemDescription] = useState("");
  const [menuItemPrice, setMenuItemPrice] = useState("");
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
    ["rate"],
    async () => getRestaurantRating(restaurantId || "", user?.userId || ""),
    { retry: false }
  );

  const [quantity, setQuantity] = useState<number>(1);
  const [reservationModal, setReservationModal] = useState(false);
  const [date, setDate] = useState(new Date());
  const [time, setTime] = useState("");

  useEffect(() => {
    if (restaurantId) {
      setMenuStatus("loading");

      getRestaurantMenu(restaurantId)
        .then((menuItems) => {
          setMenuStatus("success");
          if (menuItems) {
            setRows(
              menuItems.data.map((item: MenuItem) => (
                <tr key={item.name}>
                  <td>{item.name}</td>
                  <td>{item.description}</td>
                  <td>{item.price}</td>
                </tr>
              ))
            );
          }
        })
        .catch((error) => {
          console.log(error);
          setMenuError(error);
          setMenuStatus("error");
        });

      getRestaurantReviews(restaurantId).then((reviews) => {
        if (reviews) {
          setReviewRows(reviews.data);
        }
      });
    }
  }, [restaurantId]);

  if (!restaurantId) {
    navigate("/404");
    return <></>;
  }

  if (restaurantStatus === "loading" || !restaurantData) {
    return (
      <Flex justify="center" align="center" h={"100vh"}>
        <Loader color="orange" />
      </Flex>
    );
  }

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
    <Badge color="orange" key={tag + "" + idx}>
      {t("tags." + tag)}
    </Badge>
  ));

  const handleReview = () => {
    const rev = {
      userComment: reviewText,
    };
    reviewRestaurant(restaurantId, rev);
    console.log(reviewText);
  };

  const handleAddMenu = () => {
    const menuItem = {
      name: menuItemName,
      description: menuItemDescription,
      price: menuItemPrice,
    };
    console.log(menuItem);
    addMenuItem(restaurantId, menuItem);
    console.log(reviewText);
  };

  const handleReservation = () => {
    if (restaurantId === undefined) {
      console.log("null restaurant id");
    } else {
      const day = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
      const realMonth = date.getMonth() + 1;
      const month = realMonth < 10 ? `0${realMonth}` : realMonth;
      console.log(day, month);

      const form: ReservationForm = {
        date: `${day}/${month}/${date.getFullYear()}`,
        time: "12:00",
        quantity: quantity,
      };
      makeReservation(restaurantId, form).then((status) => {
        if (status === 201) {
          console.log("Status code is OK");
        }
      });
    }
  };

  const LikeButton = () => {
    if (authed) {
      return (
        <ActionIcon>
          <IconHeart
            size={18}
            color={theme.colors.red[6]}
            stroke={1.5}
            fill={likeData?.liked ? theme.colors.red[6] : "none"}
            onClick={() => {
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
    } else {
      return <></>;
    }
  };

  const RatingStars = () => {
    if (authed) {
      return (
        <Rating
          defaultValue={rateData?.rating}
          onChange={(value) => {
            rateRestaurant(restaurantId, { rating: value }).then(() => {
              queryClient.invalidateQueries("rate");
            });
          }}
        />
      );
    } else {
      return <></>;
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
          format="12"
          defaultValue={new Date()} 
          onChange={(value: Date) => setTime(value)}
          />

        <Button
          color="red"
          variant="outline"
          size="md"
          fullWidth
          onClick={handleReservation}
        >
          {t("pages.userReservations.confirm")}
        </Button>
      </Modal>
      <Grid gutter="xl" justify="flex-start" m="xl" align="stretch">
        <Grid.Col span={5}>
          <div className={classes.img}>
            <Image src={image} alt={name} fit="cover" />
          </div>
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
                <Table>
                  <thead>
                    <tr>
                      <th>{t("pages.restaurant.menu.name")}</th>
                      <th>{t("pages.restaurant.menu.description")}</th>
                      <th>{t("pages.restaurant.menu.price")}</th>
                    </tr>
                  </thead>
                  <tbody>{rows}</tbody>
                </Table>

                <Grid mt={100}>
                  <Grid.Col span={3}>
                    <Input
                      onChange={(e) => {
                        if (e != undefined) {
                          setMenuItemName(e.target.value);
                        } else {
                          setMenuItemName("");
                        }
                      }}
                      placeholder="Name"
                      styles={(theme) => ({
                        input: {
                          "&:focus-within": {
                            borderColor: theme.colors.orange[7],
                          },
                        },
                      })}
                    />
                  </Grid.Col>
                  <Grid.Col span={7}>
                    <Input
                      onChange={(e) => {
                        if (e != undefined) {
                          setMenuItemDescription(e.target.value);
                        } else {
                          setMenuItemDescription("");
                        }
                      }}
                      placeholder="Description"
                      styles={(theme) => ({
                        input: {
                          "&:focus-within": {
                            borderColor: theme.colors.orange[7],
                          },
                        },
                      })}
                    />
                  </Grid.Col>
                  <Grid.Col span={2}>
                    <Input
                      placeholder="Price"
                      onChange={(e) => {
                        if (e != undefined) {
                          setMenuItemPrice(e.target.value);
                        } else {
                          setMenuItemPrice("");
                        }
                      }}
                      styles={(theme) => ({
                        input: {
                          "&:focus-within": {
                            borderColor: theme.colors.orange[7],
                          },
                        },
                      })}
                    />
                  </Grid.Col>
                  <Grid.Col span={12}>
                    <Flex justify={"center"}>
                      <Button
                        color="yellow"
                        radius="md"
                        onClick={handleAddMenu}
                      >
                        Add to menu
                      </Button>
                    </Flex>
                  </Grid.Col>
                </Grid>
              </Tabs.Panel>

              <Tabs.Panel value="Reviews" pt="xs">
                {reviewRows.length > 0 && (
                  <>
                    {reviewRows.map((review, index) => (
                      <>
                        <Paper
                          withBorder
                          radius="md"
                          className={classes.comment}
                          key={index}
                          mb={20}
                        >
                          <Group>
                            <IconUser />
                            <div>
                              <Text size="sm">{review.username}</Text>
                              <Text size="xs" color="dimmed">
                                {review.date}
                              </Text>
                            </div>
                          </Group>
                          <TypographyStylesProvider className={classes.body}>
                            <div
                              className={classes.content}
                              dangerouslySetInnerHTML={{
                                __html: review.userComment,
                              }}
                            />
                          </TypographyStylesProvider>
                        </Paper>
                      </>
                    ))}
                  </>
                )}
                <Textarea
                  label={t("pages.restaurant.reviews.leaveTitle")}
                  mb={10}
                  onChange={(e) => setReviewText(e.target.value)}
                />
                <Button color="yellow" radius="md" onClick={handleReview}>
                  {t("pages.restaurant.reviews.send")}
                </Button>
              </Tabs.Panel>
            </Tabs>
          </Flex>
        </Grid.Col>
      </Grid>
    </>
  );
}
