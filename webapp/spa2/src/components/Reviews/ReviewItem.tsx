import {
  Text,
  CloseButton,
  Group,
  Paper,
  createStyles,
  Flex,
} from "@mantine/core";
import { useDeleteReview } from "../../hooks/reviews.hooks";
import { IReview } from "../../types/review/review.models";
import { IconUser } from "@tabler/icons-react";
import { useIsOwner } from "../../hooks/user.hooks";

const useStyles = createStyles((theme) => ({
  comment: {
    padding: `${theme.spacing.lg}px ${theme.spacing.xl}px`,
  },

  body: {
    paddingLeft: 0,
    paddingTop: theme.spacing.sm,
    fontSize: theme.fontSizes.sm,
  },

  content: {
    "& > p:last-child": {
      marginBottom: 0,
    },
  },
}));

interface ReviewProps {
  item: IReview;
  restaurantId: number;
}
export function ReviewItem({ item, restaurantId }: ReviewProps) {
  const { classes } = useStyles();
  const isReviewOwner = useIsOwner({ review: item });
  const { mutate, isLoading } = useDeleteReview();

  return (
    <>
      <Paper
        withBorder
        radius="md"
        className={classes.comment}
        key={item.id || item.userComment}
        mb={20}
        w={"100%"}
        miw={400}
      >
        <Flex direction="row" align="center" justify="space-between">
          <Flex direction="column">
            <Group>
              <IconUser />
              <div>
                <Text size="sm">{item.username}</Text>
                <Text size="xs" color="dimmed">
                  {item.date}
                </Text>
              </div>
            </Group>
            <Text className={classes.body}>{item.userComment}</Text>
          </Flex>

          {isReviewOwner ? (
            <CloseButton
              aria-label="Close modal"
              size="xl"
              onClick={() => {
                mutate({ restaurantId, reviewId: item.id });
              }}
              disabled={isLoading}
            />
          ) : null}
        </Flex>
      </Paper>
    </>
  );
}
