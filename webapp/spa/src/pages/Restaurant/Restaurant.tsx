import { Flex, Divider, Tabs, Loader, Center } from "@mantine/core";
import { IconMenu, IconMessageCircle } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { useGetRestaurant } from "@/hooks/restaurant.hooks";
import { NotFoundPage } from "@/pages/NotFound/NotFound";
import { Helmet } from "react-helmet-async";
import { RestaurantSkeleton } from "./Skeleton";
import { RestaurantHeader } from "./Header";

export function RestaurantPage({ restaurantId }: { restaurantId: number }) {
  const { t } = useTranslation();
  // const [tabParam, setTabParam] = useTabSearchParam("menu");
  const { data, isLoading, isError } = useGetRestaurant(restaurantId);

  if (isLoading) {
    return (
      <Flex direction="column" w="100%">
        <RestaurantSkeleton />
        <Divider m="xl" orientation="horizontal" />
        <Flex justify={"center"}>
          <Tabs
            defaultValue="Menu"
            style={{ width: "80%", justifyContent: "center" }}
          >
            <Tabs.List>
              <Tabs.Tab value="Menu" icon={<IconMenu size={14} />}>
                {t("pages.restaurant.menu.title")}
              </Tabs.Tab>
              <Tabs.Tab value="Reviews" icon={<IconMessageCircle size={14} />}>
                {t("pages.restaurant.reviews.title")}
              </Tabs.Tab>
            </Tabs.List>

            <Tabs.Panel value="Menu" pt="xs">
              <Center mt={50}>
                <Loader color="gray" variant="dots" size="xl" />
              </Center>
            </Tabs.Panel>

            <Tabs.Panel value="Reviews" pt="xs">
              <Center mt={50}>
                <Loader color="gray" variant="dots" size="xl" />
              </Center>
            </Tabs.Panel>
          </Tabs>
        </Flex>
      </Flex>
    );
  }
  if (!isLoading && !isError && data) {
    return (
      <>
        <Helmet>
          <title>{data.name}: Gourmetable</title>
        </Helmet>
        <Flex direction="column" w="100%">
          <RestaurantHeader restaurant={data} />
          <Divider m="xl" orientation="horizontal" />
          <Flex justify={"center"}>
            {/* <Tabs */}
            {/*   value={tabParam ?? "menu"} */}
            {/*   onTabChange={setTabParam} */}
            {/*   style={{ width: "80%", justifyContent: "center" }} */}
            {/* > */}
            {/*   <Tabs.List> */}
            {/*     <Tabs.Tab value="menu" icon={<IconMenu size={14} />}> */}
            {/*       {t("pages.restaurant.menu.title")} */}
            {/*     </Tabs.Tab> */}
            {/*     <Tabs.Tab */}
            {/*       value="reviews" */}
            {/*       icon={<IconMessageCircle size={14} />} */}
            {/*     > */}
            {/*       {t("pages.restaurant.reviews.title")} */}
            {/*     </Tabs.Tab> */}
            {/*   </Tabs.List> */}

            {/*   <Tabs.Panel value="menu" pt="xs"> */}
            {/*     <Center mt={50}> */}
            {/*       <MenuItems restaurant={data} /> */}
            {/*     </Center> */}
            {/*   </Tabs.Panel> */}

            {/*   <Tabs.Panel value="reviews" pt="xs"> */}
            {/*     <Center mt={50}> */}
            {/*       <Reviews restaurant={data} /> */}
            {/*     </Center> */}
            {/*   </Tabs.Panel> */}
            {/* </Tabs> */}
          </Flex>
        </Flex>
      </>
    );
  }

  return <NotFoundPage />;
}
