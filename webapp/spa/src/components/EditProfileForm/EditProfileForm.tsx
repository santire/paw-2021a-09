import { zodResolver } from "@hookform/resolvers/zod";
import {
    Button,
    Divider,
    Group,
    Paper,
    SimpleGrid,
    Text,
    TextInput,
    useMantineTheme,
    Grid, Flex, Loader
} from "@mantine/core";
import { SubmitHandler, useForm } from "react-hook-form";
import { Dropzone, DropzoneProps, IMAGE_MIME_TYPE, FileWithPath } from '@mantine/dropzone';

import { useTranslation } from "react-i18next";
import {useNavigate, useParams, useSearchParams} from "react-router-dom";
import * as z from "zod";
import useStyles from "./EditProfileForm.styles";
import {updateProfile} from "../../api/services/UserService";
import {useUser} from "../../hooks/useUser";


const registerSchema = z
    .object({
        username: z
            .string()
            .min(3)
            .max(30)
            .regex(/[a-zA-Z0-9]+/),
        firstName: z.string().min(1).max(100),
        lastName: z.string().min(1).max(100),
        phone: z
            .string()
            .min(6)
            .max(15)
            .regex(/[0-9]+/)
    })

;

type EditProfileForm = z.infer<typeof registerSchema>;

export function EditProfileForm(props: Partial<DropzoneProps>) {
    const theme = useMantineTheme();
    const { classes } = useStyles();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { userId } = useParams();
    const { status, data, error } = useUser(userId || "");

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm<EditProfileForm>({
        mode: "onTouched",
        resolver: zodResolver(registerSchema),
    });

    if (status === "loading" || !data) {
        return (
            <Flex justify="center" align="center" h={"100vh"}>
                <Loader color="orange" />
            </Flex>
        );
    }

    const {
        username,
        firstName,
        lastName,
        phone,
        email,
    } = data;


    const processForm = async (data: EditProfileForm) => {
        console.log("process form")
        const {...user } = data;
        try {
            console.log({...user})
            await updateProfile({userId: userId, email: email, ...user})
            reset();
            //navigate('/' );
        } catch (e) {
            //console.error(e);
        }
    };

    return (
        <Paper shadow="md" radius="lg">
            <div className={classes.wrapper}>
                <form className={classes.form} encType="multipart/form-data" onSubmit={handleSubmit(processForm)}>
                    <Text className={classes.title} px="sm" mt="sm" mb="xl" align="center">
                        {t("pages.profile.profile")}
                    </Text>
                    <div className={classes.fields} >
                        <Divider my="xs" />
                        <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
                            <TextInput
                                mb="md"
                                label={t("pages.profile.username.label")}
                                defaultValue={username}
                                placeholder={t("pages.profile.username.placeholder") || ""}
                                required
                                error={errors.username?.message}
                                {...register("username")}
                            />
                        </SimpleGrid>
                        <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
                            <TextInput
                                mb="md"
                                label={t("pages.profile.firstName.label")}
                                defaultValue={firstName}
                                placeholder={t("pages.profile.firstName.placeholder") || ""}
                                required
                                error={errors.firstName?.message}
                                {...register("firstName")}
                            />
                        </SimpleGrid>
                        <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
                            <TextInput
                                mb="md"
                                label={t("pages.profile.lastName.label")}
                                defaultValue={lastName}
                                placeholder={t("pages.profile.lastName.placeholder") || ""}
                                required
                                error={errors.lastName?.message}
                                {...register("lastName")}
                            />
                        </SimpleGrid>
                        <SimpleGrid cols={2} mt="md" breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
                            <TextInput
                                mb="md"
                                label={t("pages.profile.phoneNumber.label")}
                                defaultValue={phone}
                                placeholder={t("pages.profile.phoneNumber.placeholder") || ""}
                                required
                                error={errors.phone?.message}
                                {...register("phone")}
                            />
                        </SimpleGrid>
                    </div>
                    <Group position="center" mt="md">
                        <Button type="submit" color="orange" fullWidth px="xl">
                            {t("pages.profile.submit")}
                        </Button>
                    </Group>
                </form>
            </div>
        </Paper>
    );
}
