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
  useMantineTheme,
  Chip
} from "@mantine/core";
import { SubmitHandler, useForm } from "react-hook-form";
import { IconUpload, IconPhoto, IconX } from '@tabler/icons';
import { Dropzone, DropzoneProps, IMAGE_MIME_TYPE, FileWithPath } from '@mantine/dropzone';

import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";
import * as z from "zod";
import { registerRestaurant } from "../../api/services";
import { register as registerUser } from "../../api/services/AuthService";
import useStyles from "./RegisterRestaurantForm.styles";
import { useRef, useState } from "react";
import { Restaurant, tags } from "../../types";

const registerSchema = z
  .object({
    name: z.string().min(1).max(100),
    address: z.string().min(1).max(100),
    phoneNumber: z
      .string()
      .min(6)
      .max(15)
      .regex(/[0-9]+/),
      facebook: z.string().max(100)
        .regex(/^(https?:\/\/)?facebook\.com\/.*$/)
        .optional(),
      instagram: z.string().max(100)
        .regex(/^(https?:\/\/)?instagram\.com\/.*$/)
        .optional(),
      twitter: z.string().max(100)
        .regex(/^(https?:\/\/)?twitter\.com\/.*$/)
        .optional(),
      image: z.any(),
      tags: z.string().array().min(3)
  });

type RegisterRestaurantForm = z.infer<typeof registerSchema>;

export function RegisterRestaurantForm(props: Partial<DropzoneProps>) {
  const theme = useMantineTheme();
  const { classes } = useStyles();
  const { t } = useTranslation();
  const [_, setSearchParams] = useSearchParams();
  const [selectedChips, setSelectedChips] = useState<string[]>([]);

  const [files, setFiles] = useState<FileWithPath[]>([]);


  const previews = files.map((file, index) => {
    const imageUrl = URL.createObjectURL(file);
    return (
      <div className={classes.imageContainer}>
        <Image
          key={index}
          src={imageUrl}
          imageProps={{ onLoad: () => URL.revokeObjectURL(imageUrl) }}
        />
      </div>
    );
  });

  const {
    register,
    handleSubmit,
    reset,
    resetField,
    formState: { errors },
  } = useForm<RegisterRestaurantForm>({
    mode: "onTouched",
    resolver: zodResolver(registerSchema),
  });

  const processForm = async (data: RegisterRestaurantForm) => {
    const {...restaurant } = data;
    console.log({...restaurant})
    try {
      await registerRestaurant({...restaurant})
      reset();
    } catch (e) {
      console.error(e);
    }
  };

  // const processForm = async () => {
  //   console.log("Hello from processForm");
  // };

  const handleChipChange = (chipValues: string[]) => {
    setSelectedChips(chipValues);
    register('tags');
  };
  

  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <form className={classes.form} encType="multipart/form-data" onSubmit={handleSubmit(processForm)}>
          <Text className={classes.title} px="sm" mt="sm" mb="xl" align="center">
            {t("pages.registerRestaurant.title")}
          </Text>
          <div className={classes.fields}>
            <Divider my="xs" label={t("pages.registerRestaurant.loginDivider")} />
            <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                mb="md"
                label={t("pages.registerRestaurant.name.label")}
                placeholder={t("pages.registerRestaurant.name.placeholder") || ""}
                required
                error={errors.name?.message}
                {...register("name")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.register.contactDivider")} />
            <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.registerRestaurant.address.label")}
                placeholder={t("pages.registerRestaurant.address.placeholder") || ""}
                required
                error={errors.address?.message}
                {...register("address")}
              />
            </SimpleGrid>
            <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                mb="md"
                label={t("pages.registerRestaurant.phone.label")}
                placeholder={t("pages.registerRestaurant.phone.placeholder") || ""}
                type="number"
                required
                error={errors.phoneNumber?.message}
                {...register("phoneNumber")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.registerRestaurant.socialMediaDivider")} />
            <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1}]}>
              <TextInput
                label={t("pages.registerRestaurant.facebook.label")}
                placeholder={t("pages.registerRestaurant.facebook.placeholder") || ""}
                error={errors.facebook?.message}
                {...register("facebook")}
              />
                <TextInput
                label={t("pages.registerRestaurant.instagram.label")}
                placeholder={t("pages.registerRestaurant.instagram.placeholder") || ""}
                error={errors.instagram?.message}
                {...register("instagram")}
              />
                <TextInput
                mb="md"
                label={t("pages.registerRestaurant.twitter.label")}
                placeholder={t("pages.registerRestaurant.twitter.placeholder") || ""}
                error={errors.twitter?.message}
                {...register("twitter")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.registerRestaurant.profileImage")} />
            <Dropzone
              mb="md"
              onDrop={setFiles}
              onReject={(files) => console.log('rejected files', files)}
              maxSize={3 * 1024 ** 2}
              accept={IMAGE_MIME_TYPE}
              {...props}
              {...register("image")}
            >
              <SimpleGrid
                breakpoints={[{ maxWidth: 'sm', cols: 1}]}
                mt={previews.length > 0 ? 'sm' : 0}
              >
                {previews}
              </SimpleGrid>
            </Dropzone>
            <Divider my="xs" label={t("pages.registerRestaurant.tagsDivider")} />
            <Chip.Group position="center" multiple mt={15} mb="xl" onChange={handleChipChange}>
              {tags.map(tag => (
                <Chip value={tag}>{t(`tags.${tag}`)}</Chip>
              ))}
            </Chip.Group>
          </div>
            <Group position="center" mt="md">
              <Button type="submit" color="orange" fullWidth px="xl">
                {t("pages.register.submit")}
              </Button>
            </Group>
        </form>
      </div>
    </Paper>
  );
}
