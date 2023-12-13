import { Text, Modal, Container, Button, Loader } from "@mantine/core";
import { Dispatch, SetStateAction } from "react";
import { useTranslation } from "react-i18next";
import { useConfirmReservation } from "../../hooks/reservation.hooks";
import { IconCheck } from "@tabler/icons-react";
import { showNotification } from "@mantine/notifications";
import { IReservation } from "@/types/reservation/reservation.models";

interface ConfirmModalProps {
  reservation: IReservation;
  restaurantId: number;
  show: boolean;
  setShow: Dispatch<SetStateAction<boolean>>;
}
export function ConfirmModal({
  reservation,
  restaurantId,
  show,
  setShow,
}: ConfirmModalProps) {
  const { t } = useTranslation();
  const { mutate, isPending } = useConfirmReservation(restaurantId);

  function handleMutation() {
    mutate(reservation.self, {
      onSuccess: () => {
        setShow(false);
        showNotification({
          color: "green",
          icon: <IconCheck />,
          autoClose: 5000,
          message: t("pages.restaurantReservations.confirmModal.success"),
        });
      },
    });
  }
  return (
    <Modal
      opened={show}
      onClose={() => setShow(false)}
      title={t("pages.restaurantReservations.confirmModal.confirmTitle")}
      centered
    >
      <Text mb={"xl"}>{t`pages.restaurantReservations.confirmModal.text`}</Text>
      <Container>
        <Button type="submit" color="green" onClick={handleMutation}>
          {t`pages.restaurantReservations.confirmModal.confirmButton`}
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
    </Modal>
  );
}
