import {
  useCreateComment,
  useGetRestaurantComments,
} from "@/hooks/comment.hooks";
import { usePageSearchParams } from "@/hooks/searchParams.hooks";
import { useIsOwner, useUser } from "@/hooks/user.hooks";
import { IRestaurant } from "@/types/restaurant/restaurant.models";
import {
  Box,
  Button,
  Container,
  Flex,
  Group,
  Loader,
  Pagination,
  Textarea,
  Title,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { CommentItem } from "./CommentItem";
import { ICommentRegister } from "@/types/comment/comment.models";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { CommentRegisterSchema } from "@/types/comment/comment.schemas";

interface Props {
  restaurant: IRestaurant;
}

export function Comments({ restaurant }: Props) {
  const { t } = useTranslation();
  const isOwner = useIsOwner({ restaurant });
  const [pageParams, setPageParams] = usePageSearchParams(
    undefined,
    "commentsPage",
  );
  const comments = useGetRestaurantComments(restaurant.id, pageParams);
  const mutation = useCreateComment();

  const user = useUser();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ICommentRegister>({
    mode: "onTouched",
    resolver: zodResolver(CommentRegisterSchema),
  });

  function handleMutation(data: ICommentRegister) {
    if (user.isSuccess && user.data.userId) {
      mutation.mutate(
        {
          userId: user.data.userId,
          restaurantId: restaurant.id,
          comment: data,
        },
        {
          onSuccess: () => {
            reset();
          },
        },
      );
    }
  }

  if (comments.isLoading) {
    return (
      <Flex justify="center" align="center" h={"100%"}>
        <Loader color="orange" />
      </Flex>
    );
  }

  return (
    <Flex direction="column" align="center" w="100%">
      <Flex
        align="flex-start"
        justify={!isOwner && user.isSuccess ? "space-between" : "center"}
        w="100%"
      >
        <Container mt="xl" w={"100%"}>
          {comments?.data?.data.map((i) => (
            <CommentItem key={i.id} item={i} restaurantId={restaurant.id} />
          ))}
        </Container>
        <Box mt="xl" ml={30} w="70%" hidden={isOwner || !user.isSuccess}>
          <form onSubmit={handleSubmit((e) => handleMutation(e))}>
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
                disabled={mutation.isPending}
              >
                {t("pages.restaurant.reviews.submit")}
              </Button>
            </Group>
          </form>
        </Box>
      </Flex>

      <Pagination
        total={comments.data?.meta.maxPages ?? 0}
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
