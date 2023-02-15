import { zodResolver } from "@hookform/resolvers/zod";
import {
  Alert,
  Button,
  CloseButton,
  Flex,
  Group,
  Loader,
  Pagination,
  SimpleGrid,
  Table,
  TextInput,
} from "@mantine/core";
import { IconAlertCircle } from "@tabler/icons";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { z } from "zod";
import {
  addMenuItem,
  deleteMenuItem,
  getRestaurantMenu,
} from "../../api/services";
import { MenuItem } from "../../types/MenuItem";
import { Page } from "../../types/Page";

interface MenuItemProps {
  restaurantId: string;
  isOwner?: boolean;
}

function MenuItemComp({
  item,
  props,
}: {
  item: MenuItem;
  props: MenuItemProps;
}) {
  const { isOwner, restaurantId } = props;
  const queryClient = useQueryClient();
  const { mutate, isLoading } = useMutation(
    ["menu", item.id],
    async () => deleteMenuItem(restaurantId, item.id || ""),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("menu");
      },
      onSettled: () => {
        queryClient.invalidateQueries("menu");
      },
    }
  );

  return (
    <tr key={item.id ?? item.name}>
      <td>{item.name}</td>
      <td>{item.description}</td>
      <td>{item.price}</td>
      <td>
        {isOwner ? (
          <Group position="center">
            <CloseButton
              aria-label="Close modal"
              onClick={() => {
                mutate();
              }}
              disabled={isLoading}
            />
          </Group>
        ) : null}
      </td>
    </tr>
  );
}

const menuSchema = z.object({
  price: z
    .string()
    .min(1)
    .max(20)
    .regex(/[0-9]+/),
  name: z.string().min(1).max(100),
  description: z.string().min(1).max(100),
});

type MenuItemForm = z.infer<typeof menuSchema>;

export function MenuItems({ restaurantId, isOwner }: MenuItemProps) {
  const [menuPage, setMenuPage] = useState(1);
  const queryClient = useQueryClient();
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<MenuItemForm>({
    resolver: zodResolver(menuSchema),
  });
  const { t } = useTranslation();
  const [showAlert, setShowAlert] = useState(false);

  const { status, data } = useQuery<Page<MenuItem>, Error>(
    ["menu", menuPage],
    async () => getRestaurantMenu(restaurantId, { page: menuPage }),
    { keepPreviousData: true }
  );

  const { mutate, isLoading } = useMutation(
    ["createMenu"],
    async (data: MenuItem) => addMenuItem(restaurantId, data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("menu");
      },
      onSettled: () => {
        queryClient.invalidateQueries("menu");
      },
      onError: () => {
        setShowAlert(true);
        reset();
      },
    }
  );

  if (status === "loading") {
    return (
      <Flex justify="center" align="center" h={"100%"}>
        <Loader color="orange" />
      </Flex>
    );
  }

  const Menu = () => {
    return (
      <Table highlightOnHover>
        <thead>
          <tr>
            <th>{t("pages.restaurant.menu.name")}</th>
            <th>{t("pages.restaurant.menu.description")}</th>
            <th>{t("pages.restaurant.menu.price")}</th>
          </tr>
        </thead>
        <tbody>
          {data?.data.map((i, index) => (
            <MenuItemComp
              key={index}
              item={i}
              props={{ restaurantId, isOwner }}
            />
          ))}
        </tbody>
      </Table>
    );
  };

  return (
    <Flex direction="column" align="center">
      <>
        <Alert
          mb="xl"
          icon={<IconAlertCircle size={16} />}
          title="Error"
          color="red"
          hidden={!showAlert}
          withCloseButton
          onClose={() => setShowAlert(false)}
        >
          {t("pages.restaurant.menu.error")}
        </Alert>
        <Menu />
        {isOwner ? (
          <form onSubmit={handleSubmit((e) => mutate(e))}>
            <SimpleGrid cols={4} breakpoints={[{ maxWidth: "sm", cols: 1 }]}>
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
              <TextInput
                label={t("pages.restaurant.menu.price.label")}
                placeholder={t("pages.restaurant.menu.price.placeholder") || ""}
                required
                error={errors.price?.message}
                {...register("price")}
              />

              <Group position="center" my="md" pt={8}>
                <Button
                  type="submit"
                  color="orange"
                  fullWidth
                  px="xl"
                  disabled={isLoading}
                >
                  {t("pages.restaurant.menu.submit")}
                </Button>
              </Group>
            </SimpleGrid>
          </form>
        ) : null}
        <Pagination
          total={data?.meta.maxPages ?? 0}
          siblings={3}
          page={menuPage}
          onChange={setMenuPage}
          align="center"
          color="orange"
        />
      </>
    </Flex>
  );
}
