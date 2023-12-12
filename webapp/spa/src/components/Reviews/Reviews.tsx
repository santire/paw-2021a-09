import { useTranslation } from "react-i18next";
import { usePageSearchParams } from "../../hooks/searchParams.hooks";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import { useCreateReview, useGetReviews } from "../../hooks/reviews.hooks";
import { useForm } from "react-hook-form";
import { IReviewRegister } from "../../types/review/review.models";
import { zodResolver } from "@hookform/resolvers/zod";
import { ReviewRegisterSchema } from "../../types/review/review.schemas";
import {
  Text,
  Flex,
  Grid,
  Group,
  Loader,
  Pagination,
  Container,
  Textarea,
  Button,
  Box,
  Title,
} from "@mantine/core";
import { useAuth } from "../../hooks/useAuth";
import { useIsOwner } from "../../hooks/user.hooks";
import { ReviewItem } from "./ReviewItem";
import { ICommentRegister } from "@/types/comment/comment.models";
import { CommentRegisterSchema } from "@/types/comment/comment.schemas";

interface ReviewsProps {
  restaurant: IRestaurant;
}

export function Reviews({ restaurant }: ReviewsProps) {
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth();
  const isOwner = useIsOwner({ restaurant });
  const [pageParams, setPageParams] = usePageSearchParams(
    undefined,
    "reviewsPage"
  );

  const { data, isLoading } = useGetReviews(restaurant.id, pageParams);
  const createReview = useCreateReview({
    onSuccess: () => {
      reset();
    },
  });

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ICommentRegister>({
    mode: "onTouched",
    resolver: zodResolver(CommentRegisterSchema),
  });

  if (isLoading) {
    return (
      <Flex justify="center" align="center" h={"100%"}>
        <Loader color="orange" />
      </Flex>
    );
  }

  if (data && data.meta.total <= 0) {
    console.log(data);
    return (
      <Flex direction="column" align="center" w="100%">
        <Flex
          align="flex-start"
          justify={!isOwner && isAuthenticated ? "space-between" : "center"}
          w="100%"
        >
          <Flex direction="column" justify="center" mt="xl">
            <Text size={30} mb={5} align="left">
              {t("pages.restaurant.reviews.notFound")}
            </Text>
            {!isOwner && isAuthenticated ? (
              <Text size={19} italic mb={50} align="left">
                {t("pages.restaurant.reviews.hint")}
              </Text>
            ) : null}
          </Flex>
          <Box ml={30} w="50%" hidden={isOwner || !isAuthenticated}>
            <form
              onSubmit={handleSubmit((e) =>
                createReview.mutate({
                  restaurantId: restaurant.id,
                  review: e,
                })
              )}
            >
              <Group position="center">
                <Flex direction="column" w={"100%"}>
                  <Title mb="xs">{t("pages.restaurant.reviews.title")}</Title>
                  <Textarea
                    mb={10}
                    w={"100%"}
                    error={errors.message?.message}
                    {...register("message")}
                  />
                </Flex>
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
            </form>
          </Box>
        </Flex>
      </Flex>
    );
  }

  return (
    <Flex direction="column" align="center" w="100%">
      <Flex
        align="flex-start"
        justify={!isOwner && isAuthenticated ? "space-between" : "center"}
        w="100%"
      >
        <Container mt="xl" w={"100%"}>
          {data?.data.map((i) => (
            <ReviewItem key={i.id} item={i} restaurantId={restaurant.id} />
          ))}
        </Container>
        <Box mt="xl" ml={30} w="70%" hidden={isOwner || !isAuthenticated}>
          <form
            onSubmit={handleSubmit((e) =>
              createReview.mutate({
                restaurantId: restaurant.id,
                review: e,
              })
            )}
          >
            <Group position="center">
              <Flex direction="column" w={"100%"}>
                <Title mb="xs">{t("pages.restaurant.reviews.title")}</Title>
                <Textarea
                  mb={10}
                  w={"100%"}
                  error={errors.review?.message}
                  {...register("review")}
                />
              </Flex>
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
    </Flex>
  );
}
