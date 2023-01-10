import { zodResolver } from "@hookform/resolvers/zod";
import {
  Button,
  Divider,
  Group,
  Paper,
  PasswordInput,
  SimpleGrid,
  Text,
  TextInput,
  useMantineTheme,
  Chip
} from "@mantine/core";
import { useForm } from "react-hook-form";
import { IconUpload, IconPhoto, IconX } from '@tabler/icons';
import { Dropzone, DropzoneProps, IMAGE_MIME_TYPE } from '@mantine/dropzone';

import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";
import * as z from "zod";
import { registerRestaurant } from "../../api/services";
import { register as registerUser } from "../../api/services/AuthService";
import useStyles from "./RegisterRestaurantForm.styles";
import { useState } from "react";

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
      image: z.string(),
      tags: z.string().array()
  });

type RegisterRestaurantForm = z.infer<typeof registerSchema>;

export function RegisterRestaurantForm(props: Partial<DropzoneProps>) {
  const theme = useMantineTheme();
  const { classes } = useStyles();
  const { t } = useTranslation();
  const [_, setSearchParams] = useSearchParams();
  const [selectedChips, setSelectedChips] = useState<string[]>([]);
  const tags = Object.keys({
    "arabe": "Arab",
    "americano": "American",
    "argentino": "Argentinian",
    "armenio": "Armenian",
    "asiatico": "Asian",
    "autoctono": "Local",
    "bodegon": "Bodegon",
    "chino": "Chinese",
    "cocinacasera": "Homemade",
    "contemporanea": "Contemporary",
    "deautor": "Signature",
    "defusion": "Fusion",
    "español": "Spanish",
    "frances": "French",
    "indio": "Indian",
    "internacional": "International",
    "italiano": "Italian",
    "japones": "Japanese",
    "latino": "Latino",
    "mediterraneo": "Mediterranean",
    "mexicano": "Mexican",
    "parrilla": "Grill",
    "peruano": "Peruvian",
    "pescadosymariscos": "Seafood",
    "picadas": "Picadas",
    "pizzeria": "Pizza",
    "vegetariano": "Vegetarian"
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
    try {
      const {...restaurant } = data;
      await registerRestaurant({...restaurant})
      reset();
    } catch (e) {
      console.error(e);
    }
  };

  const handleChipChange = (chipValues: string[]) => {
    setSelectedChips(chipValues);
    register('tags');
  };

  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <form className={classes.form} onSubmit={handleSubmit(processForm)}>
          <Text className={classes.title} px="sm" mt="sm" mb="xl">
            {t("pages.registerRestaurant.title")}
          </Text>
          <div className={classes.fields}>
            <Divider my="xs" label={t("pages.registerRestaurant.loginDivider")} />
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.registerRestaurant.name.label")}
                placeholder={t("pages.registerRestaurant.name.placeholder") || ""}
                required
                error={errors.name?.message}
                {...register("name")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.register.contactDivider")} />
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.registerRestaurant.address.label")}
                placeholder={t("pages.registerRestaurant.address.placeholder") || ""}
                required
                error={errors.address?.message}
                {...register("address")}
              />
            </SimpleGrid>
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.registerRestaurant.phone.label")}
                placeholder={t("pages.registerRestaurant.phone.placeholder") || ""}
                type="number"
                required
                error={errors.phoneNumber?.message}
                {...register("phoneNumber")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.registerRestaurant.socialMediaDivider")} />
            <SimpleGrid cols={2} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
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
                label={t("pages.registerRestaurant.twitter.label")}
                placeholder={t("pages.registerRestaurant.twitter.placeholder") || ""}
                error={errors.twitter?.message}
                {...register("twitter")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.registerRestaurant.profileImage")} />
            <Dropzone
              onDrop={(files) => console.log('accepted files', files)}
              onReject={(files) => console.log('rejected files', files)}
              maxSize={3 * 1024 ** 2}
              accept={IMAGE_MIME_TYPE}
              {...props}
              {...register("image")}
            >
              <Group position="center" spacing="xl" style={{ minHeight: 220, pointerEvents: 'none' }}>
                <Dropzone.Accept>
                  <IconUpload
                    size={50}
                    stroke={1.5}
                    color={theme.colors[theme.primaryColor][theme.colorScheme === 'dark' ? 4 : 6]}
                  />
                </Dropzone.Accept>
                <Dropzone.Reject>
                  <IconX
                    size={50}
                    stroke={1.5}
                    color={theme.colors.red[theme.colorScheme === 'dark' ? 4 : 6]}
                  />
                </Dropzone.Reject>
                <Dropzone.Idle>
                  <IconPhoto size={50} stroke={1.5} />
                </Dropzone.Idle>

                <div>
                  <Text size="xl" inline>
                    Drag images here or click to select files
                  </Text>
                  <Text size="sm" color="dimmed" inline mt={7}>
                    Attach as many files as you like, each file should not exceed 5mb
                  </Text>
                </div>
              </Group>
            </Dropzone>
            <Divider my="xs" label={t("pages.registerRestaurant.tagsDivider")} />
            <Chip.Group position="center" multiple mt={15} onChange={handleChipChange}>
              {tags.map(tag => (
                <Chip value={tag}>{t(`tags.${tag}`)}</Chip>
              ))}
            </Chip.Group>

            <Group position="center" mt="md">
              <Button type="submit" color="orange" fullWidth px="xl">
                {t("pages.register.submit")}
              </Button>
            </Group>
          </div>
        </form>
      </div>
    </Paper>
  );
}
