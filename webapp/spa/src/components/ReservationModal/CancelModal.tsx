import { Text, Modal, Container, Button, Loader } from "@mantine/core";
import { Dispatch, SetStateAction } from "react";
import { IReservation } from "@/types/reservation/reservation.models";
import { useTranslation } from "react-i18next";
import { useCancelReservation } from "../../hooks/reservation.hooks";
import { IconCheck } from "@tabler/icons-react";
import { IRestaurant } from "@/types/restaurant/restaurant.models";
import { showNotification } from "@mantine/notifications";

interface CancelModalProps {
  reservation: IReservation;
  restaurant: IRestaurant;
  show: boolean;
  setShow: Dispatch<SetStateAction<boolean>>;
}
export function CancelModal({
  reservation,
  show,
  setShow,
}: CancelModalProps) {
  const { t } = useTranslation();
  const { mutate, isPending } = useCancelReservation();

  function handleMutation() {
    mutate(reservation.self, {
      onSuccess: () => {
        setShow(false);
        showNotification({
          color: "green",
          icon: <IconCheck />,
          autoClose: 5000,
          message: t("pages.userReservations.denyModal.success"),
        });
      },
    });
  }
  return (
    <Modal
      opened={show}
      onClose={() => setShow(false)}
      title={t("pages.userReservations.denyModal.cancelTitle")}
      centered
    >
      <Text mb={"xl"}>{t`pages.userReservations.denyModal.text`}</Text>
      <Container>
        <Button
          type="submit"
          color="red"
          onClick={handleMutation}
        >
          {t`pages.userReservations.denyModal.cancelButton`}
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
