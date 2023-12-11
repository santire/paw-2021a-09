import useStyles from "./Restaurant.styles";
import {
  Text,
  Flex,
  Grid,
  Group,
  Skeleton,
  Button,
} from "@mantine/core";

import { IconMapPin, IconPhone } from "@tabler/icons-react";
export function RestaurantSkeleton() {
  const { classes } = useStyles();
  return (
    <Grid gutter="xl" justify="flex-start" m="xl" align="stretch">
      <Grid.Col span={5}>
        <Group position="center">
          <Skeleton height={"20vw"} width={"40vw"} />
        </Group>
      </Grid.Col>
      <Grid.Col span={7} px={"5%"}>
        <Flex direction="column" justify="space-between" h={"100%"}>
          <div>
            <Text align="left" className={classes.title}>
              <Skeleton height={36} mb={15} radius="xl" width="75%" />
            </Text>
            <Group spacing="xs" mb="xs">
              <IconMapPin size={18} color="gray" stroke={1.5} />
              <Text align="left" className={classes.address}>
                <Skeleton height={18} mt={4} radius="xl" width={"20vw"} />
              </Text>
            </Group>
            <Group spacing="xs" mb="xs">
              <IconPhone size={18} color="gray" stroke={1.5} />
              <Text align="left" className={classes.address}>
                <Skeleton height={18} mt={4} radius="xl" width={"15vw"} />
              </Text>
            </Group>
            <Flex className={classes.socials} maw={90} mt={20}>
              <Group spacing={0} px={3} align="center">
                <Skeleton height={18} mx={4} my={4} radius="xl" width={18} />
                <Skeleton height={18} mx={4} my={4} radius="xl" width={18} />
                <Skeleton height={18} mx={4} my={4} radius="xl" width={18} />
              </Group>
            </Flex>
            <Group spacing={20} px={3}>
              <Skeleton
                visible={false}
                height={30}
                mt={4}
                radius="xl"
                width="100%"
              />
            </Group>
            <Group spacing={7} mt="xs" className={classes.tags}>
              <Skeleton height={26} mt={4} radius="xl" width="100%" />
            </Group>
            <Flex direction="column" justify="space-between" h={"100%"}>
              <Button
                mt="xl"
                color="orange"
                variant="outline"
                size="xl"
                disabled={true}
              >
                <Skeleton
                  height={18}
                  mx={4}
                  my={4}
                  radius="xl"
                  width={"100%"}
                  visible={false}
                />
              </Button>
            </Flex>
          </div>
        </Flex>
      </Grid.Col>
    </Grid>
  );
}
