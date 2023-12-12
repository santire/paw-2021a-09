import {
  Alert,
  Text,
  Box,
  Button,
  Flex,
  Group,
  Loader,
  NumberInput,
  Pagination,
  SimpleGrid,
  Table,
  TextInput,
} from "@mantine/core";
import {
  useCreateMenuItem,
  useGetRestaurantMenu,
} from "@/hooks/menuItem.hooks";
import { IconAlertCircle } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { MenuItem } from "./MenuItem";
import { useIsOwner } from "@/hooks/user.hooks";
import { usePageSearchParams } from "@/hooks/searchParams.hooks";
import { useForm } from "react-hook-form";
import { IMenuItemRegister } from "@/types/menuItem/menuItem.models";
import { MenuItemRegisterSchema } from "@/types/menuItem/menuItem.schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { IRestaurant } from "@/types/restaurant/restaurant.models";

interface MenuItemsProps {
  restaurant: IRestaurant;
}
export function MenuItems({ restaurant }: MenuItemsProps) {
  const { t } = useTranslation();
  const [pageParams, setPageParams] = usePageSearchParams(
    undefined,
    "menuPage",
  );
  const { data, isLoading } = useGetRestaurantMenu(restaurant, pageParams);
  const isOwner = useIsOwner({ restaurant });

  const createItem = useCreateMenuItem();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
    getValues,
    setValue,
    resetField,
  } = useForm<IMenuItemRegister>({
    mode: "onTouched",
    resolver: zodResolver(MenuItemRegisterSchema),
  });

  function handleMutation(data: IMenuItemRegister) {
    if (isOwner) {
      createItem.mutate(
        {
          url: restaurant.menu,
          item: data,
        },
        {
          onSuccess: () => {
            reset();
          },
        },
      );
    }
  }

  if (isLoading) {
    return (
      <Flex justify="center" align="center" h={"100%"}>
        <Loader color="orange" />
      </Flex>
    );
  }

  if (data && data.meta.total <= 0) {
    return (
      <Flex
        align="flex-start"
        justify={isOwner ? "space-between" : "center"}
        w="100%"
      >
        <Flex direction="column" justify="center">
          <Text size={30} mb={5} align="left">
            {t("pages.restaurant.menu.notFound")}
          </Text>
          {isOwner ? (
            <Text size={19} italic mb={50} align="left">
              {t("pages.restaurant.menu.ownerHint")}
            </Text>
          ) : null}
        </Flex>
        <Box w="30%" hidden={!isOwner}>
          <form onSubmit={handleSubmit((e) => handleMutation(e))}>
            <SimpleGrid cols={1} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
              <TextInput
                label={t("pages.restaurant.menu.name.label")}
                placeholder={t("pages.restaurant.menu.name.placeholder") || ""}
                required
                error={errors.name?.message}
                {...register("name")}
              />
              <TextInput
                label={t("pages.restaurant.menu.description.label")}
                placeholder={
                  t("pages.restaurant.menu.description.placeholder") || ""
                }
                required
                error={errors.description?.message}
                {...register("description")}
              />
              <NumberInput
                label={t("pages.restaurant.menu.price.label")}
                placeholder={t("pages.restaurant.menu.price.placeholder") || ""}
                name={"price"}
                value={getValues("price")}
                min={1}
                max={10000}
                onChange={(e) =>
                  e ? setValue("price", e) : resetField("price")
                }
                required
                error={errors.price?.message}
              />

              <Group position="center" my="md" pt={8}>
                <Button
                  type="submit"
                  color="orange"
                  fullWidth
                  px="xl"
                  disabled={createItem.isPending}
                >
                  {createItem.isPending ? (
                    <Loader variant="dots" color="orange" />
                  ) : (
                    t("pages.restaurant.menu.submit")
                  )}
                </Button>
              </Group>
            </SimpleGrid>
          </form>
        </Box>
      </Flex>
    );
  }

  return (
    <Flex direction="column" align="center" w="100%">
      <>
        <Alert
          mb="xl"
          icon={<IconAlertCircle size={16} />}
          title="Error"
          color="red"
          hidden={true}
          withCloseButton
        >
          {t("pages.restaurant.menu.error")}
        </Alert>

        <Flex
          align="flex-start"
          justify={isOwner ? "space-between" : "center"}
          w="100%"
        >
          <Table highlightOnHover mb={80} w={"65%"}>
            <thead>
              <tr>
                <th>{t("pages.restaurant.menu.name.label")}</th>
                <th>{t("pages.restaurant.menu.description.label")}</th>
                <th>{t("pages.restaurant.menu.price.label")}</th>
              </tr>
            </thead>
            <tbody>
              {data?.data.map((i) => (
                <MenuItem
                  key={i.id}
                  item={i}
                  isOwner={isOwner}
                />
              ))}
            </tbody>
          </Table>
          <Box w="30%" hidden={!isOwner}>
            <form onSubmit={handleSubmit((e) => handleMutation(e))}>
              <SimpleGrid cols={1} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
                <TextInput
                  label={t("pages.restaurant.menu.name.label")}
                  placeholder={
                    t("pages.restaurant.menu.name.placeholder") || ""
                  }
                  required
                  error={errors.name?.message}
                  {...register("name")}
                />
                <TextInput
                  label={t("pages.restaurant.menu.description.label")}
                  placeholder={
                    t("pages.restaurant.menu.description.placeholder") || ""
                  }
                  required
                  error={errors.description?.message}
                  {...register("description")}
                />
                <NumberInput
                  label={t("pages.restaurant.menu.price.label")}
                  placeholder={
                    t("pages.restaurant.menu.price.placeholder") || ""
                  }
                  name={"price"}
                  value={getValues("price")}
                  min={1}
                  max={10000}
                  onChange={(e) =>
                    e ? setValue("price", e) : resetField("price")
                  }
                  required
                  error={errors.price?.message}
                />

                <Group position="center" my="md" pt={8}>
                  <Button
                    type="submit"
                    color="orange"
                    fullWidth
                    px="xl"
                    disabled={createItem.isPending}
                  >
                    {createItem.isPending ? (
                      <Loader variant="dots" color="orange" />
                    ) : (
                      t("pages.restaurant.menu.submit")
                    )}
                  </Button>
                </Group>
              </SimpleGrid>
            </form>
          </Box>
        </Flex>

        <Pagination
          total={data?.meta.maxPages ?? 0}
          mt={40}
          siblings={3}
          page={pageParams?.page || 1}
          onChange={(e) => setPageParams({ page: e })}
          align="center"
          color="orange"
        />
      </>
    </Flex>
  );
}
