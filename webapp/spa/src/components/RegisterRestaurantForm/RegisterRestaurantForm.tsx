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
  Chip,
  Grid
} from "@mantine/core";
import { SubmitHandler, useForm } from "react-hook-form";
import { IconUpload, IconPhoto, IconX } from '@tabler/icons';
import { Dropzone, DropzoneProps, IMAGE_MIME_TYPE, FileWithPath } from '@mantine/dropzone';

import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import * as z from "zod";
import { isRestaurantNameAvailable, registerRestaurant } from "../../api/services";
import { register as registerUser } from "../../api/services/AuthService";
import useStyles from "./RegisterRestaurantForm.styles";
import { useEffect, useRef, useState } from "react";
import { getTags, Restaurant } from "../../types";


const facebookRegex = /^(https?:\/\/)?facebook\.com\/.*$/;
const instagramRegex = /^(https?:\/\/)?instagram\.com\/.*$/;
const twitterRegex = /^(https?:\/\/)?twitter\.com\/.*$/;

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
        .optional(),
      instagram: z.string().max(100)
        .optional(),
      twitter: z.string().max(100)
        .optional(),
      image: z.any(),
      tags: z.string().array().min(3)
  })
  .superRefine(async ({ facebook, instagram, twitter }, ctx) => {
    if (facebook && !facebookRegex.test(facebook)) {
      ctx.addIssue({
        path: ["facebook"],
        code: "custom",
        message: "Invalid Facebook URL",
      });
    }
    if (instagram && !instagramRegex.test(instagram)) {
      ctx.addIssue({
        path: ["instagram"],
        code: "custom",
        message: "Invalid Instagram URL",
      });
    }
    if (twitter && !twitterRegex.test(twitter)) {
      ctx.addIssue({
        path: ["twitter"],
        code: "custom",
        message: "Invalid Twitter URL",
      });
    }
  })
  // .superRefine(async ({ name }, ctx) => {
  //   const isNameAvailable = await isRestaurantNameAvailable(name);
  //   if (!isNameAvailable) {
  //     ctx.addIssue({
  //       path: ["name"],
  //       code: "custom",
  //       message: "The restaurant name is already taken",
  //     });
  //   }
  // })
  ;

type RegisterRestaurantForm = z.infer<typeof registerSchema>;

export function RegisterRestaurantForm(props: Partial<DropzoneProps>) {
  const theme = useMantineTheme();
  const { classes } = useStyles();
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [selectedChips, setSelectedChips] = useState<string[]>([]);
  const [allTags, setAllTags] = useState<string[]>([]);

  useEffect(() => {
    getTags().then((tags) => setAllTags(tags));
  }, []);


  const [files, setFiles] = useState<FileWithPath[]>([]);
  const [showMessage, setShowMessage] = useState(true);

  
  const {
    register,
    handleSubmit,
    reset,
    resetField,
    formState: { errors },
    setValue,
    setError
  } = useForm<RegisterRestaurantForm>({
    mode: "onTouched",
    resolver: zodResolver(registerSchema),
  });
  
  const previews = files.map((file, index) => {
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      const imageUrl = reader.result;
      setValue("image", imageUrl);
    }
    const imageURL = URL.createObjectURL(file);
    return (
      <div className={classes.imageContainer}>
        <Image
          width='200px'
          height='200px'
          key={index}
          src={imageURL}
          imageProps={{ onLoad: () => URL.revokeObjectURL(imageURL) }}
        />
      </div>
    );
  });


  const processForm = async (data: RegisterRestaurantForm) => {
    const {...restaurant } = data;
    try {
      const isNameAvailable = await isRestaurantNameAvailable(restaurant.name);
      if (isNameAvailable) {
        console.log(isNameAvailable)
        console.log({...restaurant})
        await registerRestaurant({...restaurant})
        reset();
        // TODO: Navigate to restaurant page
        navigate('/');
      }
      else{
        setError("name", {
          type: "custom",
          message: "The restaurant name is already taken"
        });    
        setValue("name", restaurant.name);
      }
    } catch (e) {
      //console.error(e);
    }
  };

  const handleChipChange = (chipValues: string[]) => {
    setSelectedChips(chipValues);
    setValue("tags", chipValues);
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
            <SimpleGrid cols={1} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1}]}>
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

            <Grid align="center">
              <Grid.Col span={9}>
                <Dropzone
                  mb="md"
                  onDrop={(files) => {
                    setFiles(files);
                    setShowMessage(false);
                  }}
                  onReject={(files) => console.log('rejected files', files)}
                  maxSize={3 * 1024 ** 2}
                  maxFiles={1}
                  multiple={false}
                  accept={IMAGE_MIME_TYPE}
                  sx={(theme) => ({
                    minHeight: 120,
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    border: 0,
                    backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.colors.gray[0],
            
                    '&[data-accept]': {
                      color: theme.white,
                      backgroundColor: theme.colors.blue[6],
                    },
            
                    '&[data-reject]': {
                      color: theme.white,
                      backgroundColor: theme.colors.red[6],
                    },
                  })}
                >
                  {showMessage && 
                  <div className={classes.imageContainer}>
                    <IconPhoto size={70} stroke={1.5}/>
                    <Text size="lg" inline  >
                      {t("pages.registerRestaurant.dropImage")}
                    </Text>
                  </div>
                  }
                  {previews}
                </Dropzone>
              </Grid.Col >
              <Grid.Col span={3}>
                {!showMessage && 
                  <div>
                    <Button variant="outline" color={"gray"} onClick={() => {
                      setFiles([]);
                      setShowMessage(true)
                    }}>
                      <IconX size={20} stroke={2} />
                    </Button>
                  </div>
                }
              </Grid.Col >
            </Grid>

            <Divider my="xs" label={t("pages.registerRestaurant.tagsDivider")} />
            <Text size="xl" inline className={classes.tagsText}>
                  {t("pages.registerRestaurant.tagsSelection")}
            </Text>
            <Chip.Group position="center" multiple mt={15} mb="xl" onChange={handleChipChange}>
              {allTags.map(tag => (
                <Chip value={tag}>{t(`${tag}`)}</Chip>
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
