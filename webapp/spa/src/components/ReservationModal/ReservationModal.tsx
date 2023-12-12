import { Text, Modal, NumberInput, Select, Button } from "@mantine/core";
import { DatePicker } from "@mantine/dates";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import { Dispatch, SetStateAction, useState } from "react";
import { useTranslation } from "react-i18next";
import { DateUtils } from "../../utils/DateUtils";
import { IReservationRegister } from "../../types/reservation/reservation.models";
import { useCreateReservation } from "../../hooks/reservation.hooks";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ReservationRegisterSchema } from "../../types/reservation/reservation.schemas";
import { showNotification } from "@mantine/notifications";
import { IconCheck } from "@tabler/icons-react";
import { IUser } from "@/types/user/user.models";

interface Props {
  user: IUser;
  restaurant: IRestaurant;
  show: boolean;
  setShow: Dispatch<SetStateAction<boolean>>;
}
export function ReservationModal({ restaurant, user, show, setShow }: Props) {
  const { t } = useTranslation();
  const [quantity, setQuantity] = useState(1);
  const [time, setTime] = useState(DateUtils.getTimeOptions(new Date())[0]);
  const [date, setDate] = useState(DateUtils.addTimeToDate(new Date(), time));

  const {
    handleSubmit,
    formState: { errors },
    setValue,
    clearErrors,
  } = useForm<IReservationRegister>({
    mode: "onTouched",
    resolver: zodResolver(ReservationRegisterSchema),
  });

  const makeReservation = useCreateReservation();
  function handleMutation(data: IReservationRegister) {
    makeReservation.mutate(
      { userId: user.userId, restaurantId: restaurant.id, params: data },
      {
        onSuccess: () => {
          setShow(false);
          showNotification({
            color: "green",
            icon: <IconCheck />,
            autoClose: 5000,
            title: t("pages.restaurant.notifTitle"),
            message: t("pages.restaurant.pendingReservation"),
          });
        },
      },
    );
  }

  const handleTimeChange = (value: string) => {
    clearErrors("date");
    const [hours, minutes] = value.split(":");
    setDate((prev) => {
      prev.setHours(Number(hours));
      prev.setMinutes(Number(minutes));
      return prev;
    });
    setTime(value);
  };

  const handleDateChange = (value: Date) => {
    clearErrors("date");
    setDate((prev) => {
      value.setMinutes(prev.getMinutes());
      value.setHours(prev.getHours());
      return value;
    });
  };

  const handleReservation = () => {
    const form: IReservationRegister = {
      date: date,
      quantity: quantity,
    };

    setValue("date", form.date);
    setValue("quantity", form.quantity);

    handleSubmit((data) => handleMutation(data))();
  };

  return (
    <Modal
      centered
      opened={show}
      onClose={() => setShow(false)}
      title={t("pages.restaurant.reservationButton")}
    >
      <Text mb={50} size={15}>
        {t("pages.userReservations.partySize")}
      </Text>

      <NumberInput
        label={t("pages.userReservations.guests")}
        withAsterisk
        min={1}
        max={15}
        value={quantity}
        onChange={(value: number) =>
          setQuantity(
            value < 1 || value > 15
              ? value < 1
                ? 1
                : value > 15
                ? 15
                : value
              : value,
          )
        }
        error={errors?.quantity?.message}
      />

      <DatePicker
        placeholder="Pick date"
        label={t("pages.userReservations.pickDate")}
        withAsterisk
        clearable={false}
        minDate={new Date()}
        maxDate={DateUtils.getNextWeek(new Date())}
        defaultValue={date}
        value={date}
        onChange={handleDateChange}
        error={errors.date?.message}
      />

      <Select
        label={t("pages.userReservations.pickTime")}
        withAsterisk
        required
        value={time}
        onChange={handleTimeChange}
        data={DateUtils.getTimeOptions(date)}
        mb={5}
        error={errors.date?.message}
      />

      <Button
        color="orange"
        variant="outline"
        size="md"
        mt="xl"
        fullWidth
        onClick={handleReservation}
      >
        {t("pages.userReservations.confirm")}
      </Button>
    </Modal>
  );
}
