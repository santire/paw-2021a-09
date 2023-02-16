import { zodResolver } from "@hookform/resolvers/zod";
import {
  Alert,
  Button,
  CloseButton,
  Container,
  createStyles,
  Flex,
  Grid,
  Group,
  Loader,
  Pagination,
  Paper,
  SimpleGrid,
  Text,
  Textarea,
} from "@mantine/core";
import { IconAlertCircle, IconUser } from "@tabler/icons";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { z } from "zod";
import {
  deleteReview,
  getRestaurantReviews,
  reviewRestaurant,
} from "../../api/services";
import { useAuth } from "../../context/AuthContext";
import { Review } from "../../types";
import { Page } from "../../types/Page";

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
  restaurantId: string;
  isOwner?: boolean;
}

function ReviewItemComp({ item, props }: { item: Review; props: ReviewProps }) {
  const { isOwner, restaurantId } = props;
  const queryClient = useQueryClient();
  const { classes } = useStyles();

  const { mutate, isLoading } = useMutation(
    ["review", item.id],
    async () => deleteReview(restaurantId, item.id || ""),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("reviews");
      },
      onSettled: () => {
        queryClient.invalidateQueries("reviews");
      },
    }
  );

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
        <Group>
          <IconUser />
          <div>
            <Text size="sm">{item.username}</Text>
            <Text size="xs" color="dimmed">
              {item.date}
            </Text>
          </div>

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
        </Group>
        <Text className={classes.body}>{item.userComment}</Text>
      </Paper>
    </>
  );
}

const reviewSchema = z.object({
  review: z.string().min(1).max(100),
});

export type ReviewForm = z.infer<typeof reviewSchema>;

export function Reviews({ restaurantId, isOwner }: ReviewProps) {
  const [reviewPage, setReviewPage] = useState(1);
  const queryClient = useQueryClient();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ReviewForm>({
    resolver: zodResolver(reviewSchema),
  });
  const { t } = useTranslation();
  const [showAlert, setShowAlert] = useState(false);
  const {authed} = useAuth();

  const { status, data } = useQuery<Page<Review>, Error>(
    ["reviews", reviewPage],
    async () => getRestaurantReviews(restaurantId, { page: reviewPage }),
    // { keepPreviousData: true }
  );

  const { mutate, isLoading } = useMutation(
    ["createReview"],
    async (data: ReviewForm) => reviewRestaurant(restaurantId, data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("reviews");
        reset();
      },
      onSettled: () => {
        queryClient.invalidateQueries("reviews");
        reset();
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

  const Reviews = () => {
    return (
      <Container mt="xl" w={"100%"}>
        {data?.data.map((i, index) => (
          <ReviewItemComp
            key={index}
            item={i}
            props={{ restaurantId, isOwner }}
          />
        ))}
      </Container>
    );
  };

  return (
    <Flex direction="column" align="center">
      <Alert
        mb="xl"
        icon={<IconAlertCircle size={16} />}
        title="Error"
        color="red"
        hidden={!showAlert}
        withCloseButton
        onClose={() => setShowAlert(false)}
      >
        {t("pages.restaurant.reviews.error")}
      </Alert>
      {!isOwner && authed ? (
        <form onSubmit={handleSubmit((e) => mutate(e))}>
          <Grid >
            <Grid.Col span={7}>
              <Reviews />
            </Grid.Col>
            <Grid.Col span={5}>
              <Group position="center" my="md" pt={8}>
                <Textarea
                  label={t("pages.restaurant.reviews.title")}
                  mb={10}
                  w={"100%"}
                  error={errors.review?.message}
                  {...register("review")}
                />
                <Button
                  type="submit"
                  color="orange"
                  fullWidth
                  px="xl"
                  disabled={isLoading}
                >
                  {t("pages.restaurant.reviews.submit")}
                </Button>
              </Group>
            </Grid.Col>
          </Grid>
        </form>
      ) : (
        <Reviews />
      )}
      <Pagination
        total={data?.meta.maxPages ?? 0}
        siblings={3}
        page={reviewPage}
        onChange={setReviewPage}
        align="center"
        color="orange"
      />
    </Flex>
  );
}
