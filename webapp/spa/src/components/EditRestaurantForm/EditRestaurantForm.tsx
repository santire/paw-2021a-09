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
  Grid,
  Flex,
  Loader
} from "@mantine/core";
import { SubmitHandler, useForm } from "react-hook-form";
import { IconUpload, IconPhoto, IconX } from '@tabler/icons';
import { Dropzone, DropzoneProps, IMAGE_MIME_TYPE, FileWithPath } from '@mantine/dropzone';
import { useMutation, useQueryClient } from "react-query";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import * as z from "zod";
import { isRestaurantNameAvailable, registerRestaurant, updateRestaurant } from "../../api/services";
import { register as registerUser } from "../../api/services/AuthService";
import useStyles from "./EditRestaurantForm.styles";
import { useEffect, useRef, useState } from "react";
import { getTags, Restaurant } from "../../types";
import { userInfo } from "os";
import { useRestaurant } from "../../hooks/useRestaurant";
import { DefaultValue } from "@mantine/core/lib/MultiSelect/DefaultValue/DefaultValue";


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
      tags: z.string().array().min(1)
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
  ;

type EditRestaurantForm = z.infer<typeof registerSchema>;

export function EditRestaurantForm(props: Partial<DropzoneProps>) {
  const theme = useMantineTheme();
  const { classes } = useStyles();
  const queryClient = useQueryClient();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { restaurantId } = useParams();
  const { status, data, error } = useRestaurant(restaurantId || "");



  const [selectedChips, setSelectedChips] = useState<string[]>([]);
  const [allTags, setAllTags] = useState<string[]>([]);

  const [files, setFiles] = useState<FileWithPath[]>([]);
  const [showMessage, setShowMessage] = useState(true);

  const [dataLoaded, setDataLoaded] = useState(false);
    

  useEffect(() => {
    if(allTags.length === 0)
    getTags().then((tags) => setAllTags(tags));
  }, [restaurantId, status]);

  const {
    register,
    handleSubmit,
    reset,
    resetField,
    formState: { errors },
    setValue,
    setError
  } = useForm<EditRestaurantForm>({
    mode: "onTouched",
    resolver: zodResolver(registerSchema),
  });

  useEffect(() => {
    if (data) {
      setSelectedChips(data.tags);
    }
  }, [status, data]);

  if (status === "loading" || !data) {
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
  } = data;
  
  const previews = files.map((file, index) => {
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      const imageUrl = reader.result;
      setValue("image", imageUrl);
      //register("image", imageURL);
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



  const processForm = async (data: EditRestaurantForm) => {
    console.log("process form")
    const {...restaurant } = data;
    try {
      const isNameAvailable = true;
      if(name !== restaurant.name){
        const isNameAvailable = await isRestaurantNameAvailable(restaurant.name);
      }
      if (isNameAvailable && restaurantId) {
        console.log(isNameAvailable)
        console.log({...restaurant})
        await updateRestaurant({...restaurant}, restaurantId)
        queryClient.invalidateQueries("restaurant")
        //reset();
        // TODO: Navigate to restaurant page
        navigate('/users/:userId/restaurants' );
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
    console.log(chipValues);
    const filteredChips = chipValues.filter(chip => chip !== "");
    console.log(filteredChips);
    setSelectedChips(filteredChips);
    setValue("tags", filteredChips);
  };


  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <form className={classes.form} encType="multipart/form-data" onSubmit={handleSubmit(processForm)}>
          <Text className={classes.title} px="sm" mt="sm" mb="xl" align="center">
            {t("pages.editRestaurant.title")}
          </Text>
          <div className={classes.fields}>
            <Divider my="xs" label={t("pages.editRestaurant.loginDivider")} />
            <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                mb="md"
                label={t("pages.registerRestaurant.name.label")}
                placeholder={t("pages.editRestaurant.name.placeholder") || ""}
                required
                error={errors.name?.message}
                defaultValue={name}
                {...register("name")}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.register.contactDivider")} />
            <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.registerRestaurant.address.label")}
                placeholder={t("pages.editRestaurant.address.placeholder") || ""}
                required
                error={errors.address?.message}
                {...register("address")}
                defaultValue={address}
              />
            </SimpleGrid>
            <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                mb="md"
                label={t("pages.registerRestaurant.phone.label")}
                placeholder={t("pages.editRestaurant.phone.placeholder") || ""}
                type="number"
                required
                error={errors.phoneNumber?.message}
                {...register("phoneNumber")}
                defaultValue={phoneNumber}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.editRestaurant.socialMediaDivider")} />
            <SimpleGrid cols={1} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1}]}>
              <TextInput
                label={t("pages.registerRestaurant.facebook.label")}
                placeholder={t("pages.editRestaurant.facebook.placeholder") || ""}
                error={errors.facebook?.message}
                {...register("facebook")}
                defaultValue={facebook}
              />
                <TextInput
                label={t("pages.editRestaurant.instagram.label")}
                placeholder={t("pages.editRestaurant.instagram.placeholder") || ""}
                error={errors.instagram?.message}
                {...register("instagram")}
                defaultValue={instagram}
              />
                <TextInput
                mb="md"
                label={t("pages.editRestaurant.twitter.label")}
                placeholder={t("pages.editRestaurant.twitter.placeholder") || ""}
                error={errors.twitter?.message}
                {...register("twitter")}
                defaultValue={twitter}
              />
            </SimpleGrid>
            <Divider my="xs" label={t("pages.editRestaurant.profileImage")} />

            <Grid align="center">
            <Grid.Col span={4}>
                  <Text className={classes.profileImageText}>
                    Current Profile Image
                  </Text>
                {image?  
                  <div className={classes.imageContainer}>
                    <Image
                      key={image}
                      src={image}
                    />
                  </div> : 
                  <Text>
                    No image found
                  </Text>
                }
              </Grid.Col >

              <Divider orientation="vertical" size={"sm"}/>

              <Grid.Col span={6}>
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
                  {showMessage? 
                  <div className={classes.imageContainer}>
                    <IconPhoto size={70} stroke={1.5}/>
                    <Text size="lg" inline  >
                      {t("pages.editRestaurant.dropImage")}
                    </Text>
                  </div>
                  :
                  <Text mb={15} className={classes.profileImageText}>
                  New Profile Image
                  </Text>
                  }
                  {previews}
                </Dropzone>
              </Grid.Col >
              <Grid.Col span={1}>
                {!showMessage && 
                  <div>
                    <Button variant="outline" size={"sm"} color={"gray"} onClick={() => {
                      setFiles([]);
                      setShowMessage(true)
                    }}>
                      <IconX size={20} stroke={2} />
                    </Button>
                  </div>
                }
              </Grid.Col >
            </Grid>

            <Divider my="xs" label={t("pages.editRestaurant.tagsDivider")} />
            <Text size="xl" inline className={classes.tagsText}>
                  {t("pages.editRestaurant.tagsSelection")}
            </Text>
            <Chip.Group position="center" multiple mt={15} mb="xl" 
            onChange={handleChipChange}
            defaultValue={tags}
            >
              {allTags.map(tag => (
                <Chip key={tag} value={tag}
                >
                  {t("tags." + tag)}</Chip>
              ))}
            </Chip.Group>
          </div>
            <Group position="center" mt="md">
              <Button type="submit" color="orange" fullWidth px="xl">
                {t("pages.editRestaurant.submit")}
              </Button>
            </Group>
        </form>
      </div>
    </Paper>
  );
}
