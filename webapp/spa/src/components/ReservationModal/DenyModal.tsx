import {
  Text,
  Modal,
  Container,
  Button,
  Loader,
  TextInput,
  Textarea,
} from "@mantine/core";
import { Dispatch, SetStateAction } from "react";
import { useTranslation } from "react-i18next";
import { IconCheck } from "@tabler/icons-react";
import { showNotification } from "@mantine/notifications";
import { useDenyReservation } from "../../hooks/reservation.hooks";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  IReservation,
  IReservationCancelMessage,
} from "@/types/reservation/reservation.models";
import { CancelMessageSchema } from "@/types/reservation/reservation.schemas";

interface DenyModalProps {
  reservation: IReservation;
  restaurantId: number;
  show: boolean;
  setShow: Dispatch<SetStateAction<boolean>>;
}

export function DenyModal({
  reservation,
  restaurantId,
  show,
  setShow,
}: DenyModalProps) {
  const { t } = useTranslation();
  const { mutate, isPending } = useDenyReservation(restaurantId);

  function handleMutation(data: IReservationCancelMessage) {
    mutate(
      { url: reservation.self, message: data },
      {
        onSuccess: () => {
          setShow(false);
          reset();
          showNotification({
            color: "green",
            icon: <IconCheck />,
            autoClose: 5000,
            message: t("pages.restaurantReservations.denyModal.success"),
          });
        },
      },
    );
  }

  const {
    handleSubmit,
    register,
    formState: { errors },
    reset,
  } = useForm<IReservationCancelMessage>({
    mode: "onTouched",
    resolver: zodResolver(CancelMessageSchema),
  });

  return (
    <Modal
      opened={show}
      onClose={() => setShow(false)}
      title={t("pages.restaurantReservations.denyModal.denyTitle")}
      centered
    >
      <form onSubmit={handleSubmit((data) => handleMutation(data))}>
        {/* <Text mb={"xl"}>{t`pages.restaurantReservations.denyModal.text`}</Text> */}

        <Textarea
          withAsterisk
          label={t`pages.restaurantReservations.denyModal.message`}
          placeholder={String(
            t`pages.restaurantReservations.denyModal.placeholder`,
          )}
          {...register("message")}
          error={errors.message?.message}
          labelProps={{
            style: { marginBottom: "0.5rem" },
          }}
        />
        <Container mt="lg">
          <Button type="submit" color="red">
            {t`pages.restaurantReservations.denyModal.denyButton`}
          </Button>
          <Button
            onClick={() => setShow(false)}
            variant="default"
            color="gray"
            style={{ marginLeft: "0.5rem" }}
            disabled={isPending}
          >
            {isPending ? (
              <Loader color="orange" variant="dots" />
            ) : (
              t`pages.userReservations.denyModal.goBackButton`
            )}
          </Button>
        </Container>
      </form>
    </Modal>
  );
}
