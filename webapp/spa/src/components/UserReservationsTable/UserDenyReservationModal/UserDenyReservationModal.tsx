import { Button, Container, Modal } from "@mantine/core";
import { cancelReservation } from "../../../api/services";
import { useTranslation } from "react-i18next";
import { Text } from '@mantine/core';

interface UserDenyReservationModalProps {
  show: boolean;
  onClose: () => void;
  restaurantId: string;
  reservationId: string;
}

export function UserDenyReservationModal({
  show,
  onClose,
  restaurantId,
  reservationId,
}: UserDenyReservationModalProps) {
  const { t } = useTranslation();

  const onSubmit = async (restaurantId: string, reservationId: string) => {
    try {
      await cancelReservation(restaurantId, reservationId);
      onClose();
    } catch (error) {
      // Handle error
      console.error(error);
    }
  };

  return (
    <Modal opened={show} onClose={onClose} title={t`pages.userReservations.denyModal.cancelTitle`} centered>
      <Text
        mb={"xl"}>
        {t`pages.userReservations.denyModal.text`}
      </Text>
      <Container>
          <Button type="submit" color="red" onSubmit={() => onSubmit(restaurantId, reservationId)}>
            {t`pages.userReservations.denyModal.cancelButton`}
          </Button>
          <Button 
            onClick={onClose} 
            variant="default" 
            color="gray" 
            style={{ marginLeft: "0.5rem" }}>
                {t`pages.userReservations.denyModal.goBackButton`}
          </Button>
      </Container>
    </Modal>
  );
}
