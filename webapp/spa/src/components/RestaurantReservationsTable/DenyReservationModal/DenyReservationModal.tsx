import { useForm } from "react-hook-form";
import { Button, Container, Modal, TextInput } from "@mantine/core";
import { Form } from "react-router-dom";
import { denyReservation } from "../../../api/services";
import { useTranslation } from "react-i18next";

interface DenyReservationForm {
  message: string;
}

interface DenyReservationModalProps {
  show: boolean;
  onClose: () => void;
  restaurantId: string;
  reservationId: string;
  isCancellation: boolean;
}

export function DenyReservationModal({
  show,
  onClose,
  restaurantId,
  reservationId,
  isCancellation
}: DenyReservationModalProps) {
  const {
    handleSubmit,
    register,
    formState: { errors },
    reset,
    formState,
  } = useForm<DenyReservationForm>();
  const { t } = useTranslation();

  const onSubmit = async (data: DenyReservationForm, restaurantId: string, reservationId: string) => {
    try {
      await denyReservation(restaurantId, reservationId, data.message);
      console.log("I have been called!");
      onClose();
    } catch (error) {
      // Handle error
      console.error(error);
    }
  };

  return (
    <Modal opened={show} onClose={onClose} title={isCancellation ? t`pages.restaurantReservations.denyModal.cancelTitle` : t`pages.restaurantReservations.denyModal.denyTitle`} centered>
      <Container>
        <Form onSubmit={handleSubmit((data) => onSubmit(data, restaurantId, reservationId))}>
        <TextInput
            withAsterisk
            label={t`pages.restaurantReservations.denyModal.message`}
            placeholder={String(t`pages.restaurantReservations.denyModal.placeholder`)}
            {...register("message", {
                required: String(t`pages.restaurantReservations.denyModal.form.requiredError`),
                minLength: {
                value: 5,
                message: t`pages.restaurantReservations.denyModal.form.minLengthError`,
              },
              maxLength: {
                value: 100,
                message: t`pages.restaurantReservations.denyModal.form.maxLengthError`,
              },
            })}
            error={errors.message?.message}
            labelProps={{
                style: { marginBottom: "0.5rem" },
              }}
          />
          <div style={{ marginTop: "2rem", textAlign: "center" }}></div>
          <Button type="submit" color="red" disabled={formState.isSubmitting}>
            {isCancellation ? t`pages.restaurantReservations.denyModal.cancelButton` : t`pages.restaurantReservations.denyModal.denyButton`}
          </Button>
          <Button 
            onClick={onClose} 
            variant="default" 
            color="gray" 
            disabled={formState.isSubmitting}
            style={{ marginLeft: "0.5rem" }}>
                {t`pages.restaurantReservations.denyModal.goBackButton`}
          </Button>
        </Form>
      </Container>
    </Modal>
  );
}
