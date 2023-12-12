import { useDeleteComment } from "@/hooks/comment.hooks";
import { useIsOwner } from "@/hooks/user.hooks";
import { IComment } from "@/types/comment/comment.models";
import {
  Flex,
  Group,
  Text,
  Paper,
  createStyles,
  CloseButton,
} from "@mantine/core";
import { IconUser } from "@tabler/icons-react";

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

interface CommentProps {
  item: IComment;
  restaurantId: number;
}
export function CommentItem({ item, restaurantId }: CommentProps) {
  const { classes } = useStyles();
  const isCommentOwner = useIsOwner({ comment: item });
  const mutation = useDeleteComment(restaurantId);

  return (
    <>
      <Paper
        withBorder
        radius="md"
        className={classes.comment}
        key={item.id || item.message}
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
            <Text className={classes.body}>{item.message}</Text>
          </Flex>

          {isCommentOwner ? (
            <CloseButton
              aria-label="Close modal"
              size="xl"
              onClick={() => {
                mutation.mutate({ comment: item });
              }}
              disabled={mutation.isPending}
            />
          ) : null}
        </Flex>
      </Paper>
    </>
  );
}
