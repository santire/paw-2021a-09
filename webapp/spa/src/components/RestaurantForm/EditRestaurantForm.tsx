import { zodResolver } from "@hookform/resolvers/zod";
import {
  Button,
  Divider,
  Group,
  Paper,
  SimpleGrid,
  Text,
  Image,
  TextInput,
  Chip,
  Grid,
  Loader,
} from "@mantine/core";
import { useForm } from "react-hook-form";
import { IconPhoto, IconX } from "@tabler/icons-react";
import { Dropzone, IMAGE_MIME_TYPE, FileWithPath } from "@mantine/dropzone";

import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import useStyles from "./RestaurantForm.styles";
import { useEffect, useState } from "react";
import {
  IRestaurant,
  IRestaurantUpdate,
} from "@/types/restaurant/restaurant.models";
import { RestaurantUdpdateSchema } from "@/types/restaurant/restaurant.schemas";
import { useUpdateRestaurant } from "@/hooks/restaurant.hooks";
import { useGetTags } from "@/hooks/tags.hooks";
import { useIsOwner } from "@/hooks/user.hooks";
import { isServerError } from "@/api/client";

interface EditRestaurantFormProps {
  restaurant: IRestaurant;
}
export function EditRestaurantForm({ restaurant }: EditRestaurantFormProps) {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [chips, setChips] = useState<string[]>([]);

  const [files, setFiles] = useState<FileWithPath[]>([]);
  const [showMessage, setShowMessage] = useState(true);
  const [showPrevious, setShowPrevious] = useState(true);

  const tagsList = useGetTags();

  const isOwner = useIsOwner({ restaurant });

  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
    clearErrors,
    setValue,
  } = useForm<IRestaurantUpdate>({
    mode: "onTouched",
    resolver: zodResolver(RestaurantUdpdateSchema),
  });

  const { mutate, isPending } = useUpdateRestaurant();

  function handleMutation(data: IRestaurantUpdate) {
    mutate(
      { restaurantUrl: restaurant.self, params: data },
      {
        onSuccess: () => {
          navigate(`/restaurants/${restaurant.id}`);
        },

        onError: ({ cause }) => {
          if (isServerError(cause) && cause.code === "invalid_image") {
            setError("image", {
              type: "custom",
              message:
                t("pages.editRestaurant.errors.imageInvalid") ||
                "Invalid image",
            });
          }
        },
      },
    );
  }

  useEffect(() => {
    if (!isOwner) {
      navigate(`/restaurants/${restaurant.id}`);
    }
    setValue("name", restaurant.name);
    setValue("address", restaurant.address);
    setValue("phoneNumber", restaurant.phoneNumber);
    setValue("facebook", restaurant.facebook);
    setValue("twitter", restaurant.twitter);
    setValue("instagram", restaurant.instagram);
    setShowMessage(false);
  }, []);

  useEffect(() => {
    if (tagsList.data) {
      setChips(restaurant.tags.map((t) => tagsList.data.indexOf(t) + ""));
      setValue(
        "tags",
        restaurant.tags.map((t) => tagsList.data.indexOf(t) + ""),
      );
    }
  }, [tagsList.data]);

  const previous = (
    <div className={classes.imageContainer}>
      <Image width="200px" height="200px" src={restaurant.image} />
    </div>
  );
  const previews = files.map((file, index) => {
    setValue("image", file);
    const imageURL = URL.createObjectURL(file);
    return (
      <div key={index} className={classes.imageContainer}>
        <Image
          width="200px"
          height="200px"
          src={imageURL || restaurant.image}
          imageProps={{ onLoad: () => URL.revokeObjectURL(imageURL) }}
        />
      </div>
    );
  });

  const handleChipChange = (chipValues: string[]) => {
    if (chipValues.length > 3) return;
    setChips(chipValues);
    setValue("tags", chipValues);
  };

  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <form
          className={classes.form}
          onSubmit={handleSubmit((data) => handleMutation(data))}
        >
          <Text
            className={classes.title}
            px="sm"
            mt="sm"
            mb="xl"
            align="center"
          >
            {t("pages.editRestaurant.title")}
          </Text>
          <div className={classes.fields}>
            <Divider my="xs" label={t("pages.editRestaurant.loginDivider")} />
            <SimpleGrid
              cols={1}
              mt="md"
              breakpoints={[{ maxWidth: "sm", cols: 1 }]}
            >
              <TextInput
                mb="md"
                label={t("pages.editRestaurant.name.label")}
                placeholder={t("pages.editRestaurant.name.placeholder") || ""}
                required
                error={errors.name?.message}
                {...register("name")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.register.contactDivider")} />
            <SimpleGrid
              cols={1}
              mt="md"
              breakpoints={[{ maxWidth: "sm", cols: 1 }]}
            >
              <TextInput
                label={t("pages.editRestaurant.address.label")}
                placeholder={
                  t("pages.editRestaurant.address.placeholder") || ""
                }
                required
                error={errors.address?.message}
                {...register("address")}
              />
            </SimpleGrid>
            <SimpleGrid
              cols={1}
              mt="md"
              breakpoints={[{ maxWidth: "sm", cols: 1 }]}
            >
              <TextInput
                mb="md"
                label={t("pages.editRestaurant.phone.label")}
                placeholder={t("pages.editRestaurant.phone.placeholder") || ""}
                type="number"
                required
                error={
                  errors.phoneNumber?.message === "errors.invalidPhone"
                    ? t(errors.phoneNumber?.message)
                    : errors.phoneNumber?.message
                }
                {...register("phoneNumber")}
              />
            </SimpleGrid>
            <Divider
              my="xs"
              label={t("pages.editRestaurant.socialMediaDivider")}
            />
            <SimpleGrid
              cols={1}
              mt="md"
              breakpoints={[{ maxWidth: "sm", cols: 1 }]}
            >
              <TextInput
                label={t("pages.editRestaurant.facebook.label")}
                placeholder={
                  t("pages.editRestaurant.facebook.placeholder") || ""
                }
                error={
                  errors.facebook?.message === "errors.facebookRegex"
                    ? t("errors.facebookRegex")
                    : errors.facebook?.message
                }
                {...register("facebook")}
              />
              <TextInput
                label={t("pages.editRestaurant.instagram.label")}
                placeholder={
                  t("pages.editRestaurant.instagram.placeholder") || ""
                }
                error={
                  errors.instagram?.message === "errors.instagramRegex"
                    ? t("errors.instagramRegex")
                    : errors.instagram?.message
                }
                {...register("instagram")}
              />
              <TextInput
                mb="md"
                label={t("pages.editRestaurant.twitter.label")}
                placeholder={
                  t("pages.editRestaurant.twitter.placeholder") || ""
                }
                error={
                  errors.twitter?.message === "errors.twitterRegex"
                    ? t("errors.twitterRegex")
                    : errors.twitter?.message
                }
                {...register("twitter")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.editRestaurant.profileImage")} />

            <Grid align="center">
              <Grid.Col span={9}>
                <Dropzone
                  mb="md"
                  onDrop={(files) => {
                    setFiles(files);
                    setShowMessage(false);
                    clearErrors("image");
                    setShowPrevious(false);
                  }}
                  onReject={(files) => {
                    setFiles([]);
                    setValue("image", null);
                    setShowMessage(true);
                    setShowPrevious(true);
                    for (const err of files[0].errors) {
                      if (err.code === "file-too-large") {
                        setError("image", {
                          type: "custom",
                          message:
                            t("pages.editRestaurant.errors.imageTooLarge") ||
                            "Image too large",
                        });
                      }
                      if (err.code === "file-invalid-type") {
                        setError("image", {
                          type: "custom",
                          message:
                            t("pages.editRestaurant.errors.imageInvalidType") ||
                            "Image type invalid",
                        });
                      }
                    }
                    console.log("rejected files", files);
                  }}
                  maxSize={400 * 1024}
                  maxFiles={1}
                  multiple={false}
                  accept={IMAGE_MIME_TYPE}
                  sx={(theme) => ({
                    minHeight: 120,
                    display: "flex",
                    justifyContent: showMessage ? "start" : "center",
                    alignItems: "center",
                    border: 0,
                    backgroundColor:
                      theme.colorScheme === "dark"
                        ? theme.colors.dark[6]
                        : theme.colors.gray[0],

                    "&[data-accept]": {
                      color: theme.white,
                      backgroundColor: theme.colors.blue[6],
                    },

                    "&[data-reject]": {
                      color: theme.white,
                      backgroundColor: theme.colors.red[6],
                    },
                  })}
                >
                  {showMessage && (
                    <div className={classes.imageContainer}>
                      <IconPhoto size={70} stroke={1.5} />
                      <Text size="lg" inline align="start">
                        {t("pages.editRestaurant.dropImage")}

                        <Text size="sm" color="dimmed" inline mt={7}>
                          {t("pages.editRestaurant.dropImageSize")}
                        </Text>
                      </Text>
                    </div>
                  )}
                  {showPrevious && previous}
                  {previews}
                  {errors.image?.message ? (
                    <Text color="red" mt={5}>
                      {errors.image?.message + ""}
                    </Text>
                  ) : null}
                </Dropzone>
              </Grid.Col>
              <Grid.Col span={3}>
                {!showMessage && (
                  <div>
                    <Button
                      variant="outline"
                      color={"gray"}
                      onClick={() => {
                        setFiles([]);
                        setValue("image", null);
                        clearErrors("image");
                        setShowMessage(true);
                        setShowPrevious(false);
                      }}
                    >
                      <IconX size={20} stroke={2} />
                    </Button>
                  </div>
                )}
              </Grid.Col>
            </Grid>

            <Divider my="xs" label={t("pages.editRestaurant.tagsDivider")} />
            <Text size="xl" inline className={classes.tagsText}>
              {t("pages.editRestaurant.tagsSelection")}
            </Text>
            {errors.tags && (
              <Text color="red" mt={5}>
                {errors.tags.message}
              </Text>
            )}
            <Chip.Group
              position="center"
              multiple
              mt={15}
              mb="xl"
              value={chips}
              onChange={handleChipChange}
            >
              {tagsList.data?.map((tag, i) => (
                <Chip key={tag} value={"" + i}>
                  {t("tags." + tag.toLowerCase())}
                </Chip>
              ))}
            </Chip.Group>
          </div>
          <Group position="center" mt="md">
            <Button
              type="submit"
              color="orange"
              fullWidth
              px="xl"
              disabled={isPending}
            >
              {isPending ? (
                <Loader variant="dots" color="orange" />
              ) : (
                t("pages.editRestaurant.submit")
              )}
            </Button>
          </Group>
        </form>
      </div>
    </Paper>
  );
}
