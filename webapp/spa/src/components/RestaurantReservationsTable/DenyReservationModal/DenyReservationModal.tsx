import { useForm } from "react-hook-form";
import { Button, Container, Modal, TextInput } from "@mantine/core";
import { Form } from "react-router-dom";
import { denyReservation } from "../../../api/services";

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
    <Modal opened={show} onClose={onClose} title={isCancellation ? "Cancel reservation" : "Deny reservation"} centered>
      <Container>
        <Form onSubmit={handleSubmit((data) => onSubmit(data, restaurantId, reservationId))}>
        <TextInput
            withAsterisk
            label="Message"
            placeholder="Explain the reason to your client..."
            {...register("message", {
              required: "Message is required",
              minLength: {
                value: 5,
                message: "Message must be at least 5 characters long",
              },
              maxLength: {
                value: 100,
                message: "Message cannot exceed 100 characters",
              },
            })}
            error={errors.message?.message}
          />
          <Button type="submit" color="red" disabled={formState.isSubmitting}>
            {isCancellation ? "Cancel" : "Deny"}
          </Button>
          <Button onClick={onClose} variant="default" color="gray" disabled={formState.isSubmitting}>
            Go back
          </Button>
        </Form>
      </Container>
    </Modal>
  );
}
