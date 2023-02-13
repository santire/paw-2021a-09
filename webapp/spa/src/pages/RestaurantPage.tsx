import {
  ActionIcon,
  Badge,
  Box,
  Button,
  createStyles,
  Divider,
  Flex,
  Grid,
  Group,
  Image,
  Loader,
  Table,
  Tabs,
  Text,
} from "@mantine/core";
import {
  IconBrandFacebook,
  IconBrandInstagram,
  IconBrandTwitter,
  IconMapPin,
  IconMenu,
  IconMessageCircle,
  IconPhone,
  IconPhoto,
  IconSettings,
} from "@tabler/icons";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { useNavigate, useParams } from "react-router-dom";
import { getRestaurantMenu } from "../api/services";
import { useRestaurant } from "../hooks/useRestaurant";
import { Restaurant } from "../types";
import { MenuItem } from "../types/MenuItem";
import { Page } from "../types/Page";

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
}));

export function RestaurantPage() {
  const { classes, theme } = useStyles();
  const { restaurantId } = useParams();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { status, data, error } = useRestaurant(restaurantId || "");

  const [menuStatus, setMenuStatus] = useState("idle");
  const [menuError, setMenuError] = useState<Error>();
  const [rows, setRows] = useState<React.ReactElement[]>([]);

  
  useEffect(() => {
    if (restaurantId) {
      setMenuStatus("loading");
  
      getRestaurantMenu(restaurantId)
        .then(menuItems => {
            setMenuStatus("success");
            if(menuItems){
              setRows(menuItems.data.map((item: MenuItem) => (
                <tr key={item.name}>
                  <td>{item.name}</td>
                  <td>{item.description}</td>
                  <td>{item.price}</td>
                </tr>
              )));
            }
        })
        .catch(error => {
            console.log(error);
            setMenuError(error);
            setMenuStatus("error");
        });
    }
  }, [restaurantId]);

  if (!restaurantId) {
    navigate("/404");
    return <></>;
  }

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

  const SocialsButton = ({ type, url }: { type: string; url: string }) => {
    switch (type) {
      case "twitter": {
        return (
          <ActionIcon>
            <IconBrandTwitter
              size={18}
              color={theme.colors.blue[3]}
              stroke={1.5}
              onClick={() => navigate(url)}
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
              onClick={() => navigate(url)}
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
              onClick={() => navigate(url)}
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

  return (
    <>
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
              <Group spacing={7} mt="xs" className={classes.tags}>
                {features}
              </Group>
            </div>
          </Flex>
        </Grid.Col>

        
        <Grid.Col span={12}>
          <Divider m="xl" orientation="horizontal"/>
        </Grid.Col>

        <Grid.Col span={6}>
          <Tabs defaultValue="Menu">
            <Tabs.List>
              <Tabs.Tab value="Menu" icon={<IconMenu size={14} />}>Menu</Tabs.Tab>
              <Tabs.Tab value="Reviews" icon={<IconMessageCircle size={14} />}>Reviews</Tabs.Tab>
            </Tabs.List>

            <Tabs.Panel value="Menu" pt="xs">
              <Table>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                  </tr>
                </thead>
                <tbody>{rows}</tbody>
              </Table>
            </Tabs.Panel>

            <Tabs.Panel value="Reviews" pt="xs">
              Reviews tab content
            </Tabs.Panel>
          </Tabs>
        </Grid.Col>
        

        <Grid.Col span={4} px={"5%"}>
          <Divider orientation="vertical"/>
          <Flex direction="column" justify="space-between" h={"100%"}>
            <Button mt="xl" color="orange" variant="outline" size="xl">
              {t("pages.restaurant.reservationButton")}
            </Button>
          </Flex>
        </Grid.Col>
      </Grid>

    </>
  );
}
