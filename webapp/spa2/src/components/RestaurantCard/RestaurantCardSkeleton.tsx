import {
  Badge,
  Button,
  Card,
  Flex,
  Grid,
  Group,
  Skeleton,
  Text,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import useStyles from "./RestaurantCard.styles";
import { useNavigate } from "react-router-dom";
import { IconStar } from "@tabler/icons-react";

export function RestaurantCardSkeleton() {
  const { classes } = useStyles();
  const { t } = useTranslation();

  return (
    <Card withBorder p="lg" radius="md" className={classes.card}>
      <Card.Section mb="md">
        <Skeleton height={180} width={355} />
      </Card.Section>

      <Card.Section mt="xs" className={classes.section}>
        <Grid justify="space-between">
          <Grid.Col span={8}>
            <Text weight={700} className={classes.title} lineClamp={2}>
              <Skeleton height={18} mt={4} radius="xl" width="100%" />
            </Text>
          </Grid.Col>
          <Grid.Col span={4}>
            <Flex align="center" justify="right">
              <Badge size="sm" color="gray" py={10}>
                <Group spacing="xs">
                  <Text weight={700} size="sm" align="center">
                    <Skeleton height={8} width={18} />
                  </Text>
                  <Flex align="center" p={0} m={0}>
                    <IconStar size={16} color={"gray"} fill={"gray"} />
                  </Flex>
                </Group>
              </Badge>
            </Flex>
          </Grid.Col>
        </Grid>
      </Card.Section>

      <Card.Section className={classes.section}>
        <Text className={classes.label} color="dimmed">
          {t("restaurantCard.tagsTitle")}
        </Text>
        <Skeleton height={20} mb={15} mt={23} width="100%" radius="xl" />
      </Card.Section>

      <Flex justify="center" align="center" mt="md">
        <Button
          color="orange"
          variant="outline"
          size="md"
          fullWidth
          disabled
        ></Button>
      </Flex>

      <Card.Section className={classes.footer}>
        <Skeleton height={18} width="100%" />
      </Card.Section>
    </Card>
  );
}
