import { Text, Modal, NumberInput, Select, Alert, Button } from "@mantine/core";
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

interface Props {
  restaurant: IRestaurant;
  show: boolean;
  setShow: Dispatch<SetStateAction<boolean>>;
}
export function ReservationModal({ restaurant, show, setShow }: Props) {
  const { t } = useTranslation();
  const [quantity, setQuantity] = useState(1);
  const [date, setDate] = useState(new Date());
  const [time, setTime] = useState(new Date());


  const {
    handleSubmit,
    formState: { errors },
    setError,
    setValue,
    clearErrors,
  } = useForm<IReservationRegister>({
    mode: "onTouched",
    resolver: zodResolver(ReservationRegisterSchema),
  });

  const makeReservation = useCreateReservation({
    onSuccess: () => {
      setShow(false);
      showNotification({
        color: 'green',
        icon: <IconCheck />,
        autoClose: 5000,
        title: t("pages.restaurant.notifTitle"),
        message: t("pages.restaurant.pendingReservation"),
      });
    },
    onError: (error) => {
      if (error.code === "validation_error" && error.errors) {
        for (const e of error.errors) {
          switch (e.subject) {
            case "date":
              setError("date", {
                type: "custom",
                message: t("reservationModal.errors.date") || "",
              });
              break;

            case "time":
              setError("time", {
                type: "custom",
                message: t("reservationModal.errors.time") || "",
              });
              break;

            default:
              // TODO: Show default alert?
              console.error("Unexpected error subject: ", e.subject, e.message);
          }
        }
      } else {
        console.error("Unknown error code: ", error.code);
      }
    },
  });

  const getTimeOptions = () => {
    if (!date) return [];
    return DateUtils.getTimeOptions(date);
  };

  const handleTimeChange = (value: string) => {
    clearErrors("time");
    const [hours, minutes] = value.split(":");
    const selectedTime = new Date();
    selectedTime.setHours(Number(hours));
    selectedTime.setMinutes(Number(minutes));
    setTime(selectedTime);
  };

  const handleDateChange = (value: Date) => {
    clearErrors("date");
    setDate(new Date(value));
    const timeOptions = DateUtils.getTimeOptions(date);
    setTime(DateUtils.addTimeToDate(date, timeOptions[0])); // Set the first option as the default time
  };

  const handleReservation = () => {
    const day = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
    const realMonth = date.getMonth() + 1;
    const month = realMonth < 10 ? `0${realMonth}` : realMonth;
    const hours = time.toLocaleTimeString("es");

    const form: IReservationRegister = {
      date: `${day}/${month}/${date.getFullYear()}`,
      time: hours.slice(0, 5),
      quantity: quantity,
    };

    setValue("date", form.date);
    setValue("time", form.time);
    setValue("quantity", form.quantity);

    handleSubmit((data) =>
      makeReservation.mutate({ restaurantId: restaurant.id, reservation: data })
    )();
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
        onChange={(value: number) => setQuantity(value)}
        error={errors?.quantity?.message}
      />

      <DatePicker
        placeholder="Pick date"
        label={t("pages.userReservations.pickDate")}
        withAsterisk
        minDate={new Date()}
        maxDate={DateUtils.getNextWeek(new Date())}
        value={date}
        onChange={handleDateChange}
        error={errors.date?.message}
      />

      <Select
        label={t("pages.userReservations.pickDate")}
        withAsterisk
        value={
          time
            ? `${time.getHours()}:${time
                .getMinutes()
                .toString()
                .padStart(2, "0")}`
            : ""
        }
        onChange={handleTimeChange}
        data={getTimeOptions()}
        mb={5}
        error={errors.time?.message}
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
