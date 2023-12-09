import { Button, Loader, Skeleton, Text, useMantineTheme } from "@mantine/core";
import { useGetRestaurant } from "../../hooks/restaurant.hooks";
import { IReservation } from "../../types/reservation/reservation.models";
import { DateUtils } from "../../utils/DateUtils";
import { useTranslation } from "react-i18next";
import { CancelModal } from "../ReservationModal/CancelModal";
import { useState } from "react";
import { Link } from "react-router-dom";

interface UserReservationProps {
  reservation: IReservation;
  isHistory?: boolean;
}

export function UserReservation({
  reservation,
  isHistory,
}: UserReservationProps) {
  const theme = useMantineTheme();
  const { t } = useTranslation();
  const { restaurant } = reservation;
  const [show, setShow] = useState(false);

  const date = new Date(reservation.date);
  const formattedDate = DateUtils.formatDate(date);
  const rid = parseInt(restaurant.substring(restaurant.lastIndexOf("/") + 1));
  const { data: restaurantData } = useGetRestaurant(rid);

  if (restaurantData) {
    return (
      <tr key={reservation.reservationId}>
        <td>{reservation.quantity}</td>
        <td>{formattedDate}</td>
        <td>
          <Link
            style={{
              textDecoration: "none",
              fontWeight: 700,
              color:
                theme.colorScheme === "dark"
                  ? theme.colors.gray[5]
                  : theme.colors.gray[8],
            }}
            to={`/restaurants/${restaurantData.id}`}
          >
            {restaurantData.name}
          </Link>
        </td>
        <td>
          <Text
            style={{
              color: isHistory
                ? theme.colors.gray[7]
                : reservation.confirmed
                ? theme.colors.green[8]
                : theme.colors.yellow[8],
            }}
          >
            {isHistory
              ? t("pages.userReservations.finished")
              : reservation.confirmed
              ? t("pages.userReservations.confirmed")
              : t("pages.userReservations.pending")}
          </Text>
        </td>
        {!isHistory && (
          <td>
            <Button
              color={"red"}
              role="button"
              variant="outline"
              onClick={() => setShow(true)}
            >{t`pages.restaurantReservations.cancelButton`}</Button>
          </td>
        )}
        <CancelModal
          restaurant={restaurantData}
          reservation={reservation}
          show={show}
          setShow={setShow}
        />
      </tr>
    );
  }

  return (
    <tr key={reservation.reservationId}>
      <td>{reservation.quantity}</td>
      <td>{formattedDate}</td>
      <td>
        <Skeleton height={18} mt={4} radius="xl" width="100%" />
      </td>
      <td>
        <Text
          style={{
            color: reservation.confirmed
              ? theme.colors.green[6]
              : theme.colors.red[6],
          }}
        >
          {reservation.confirmed
            ? t("userReservation.confirmed")
            : t("userReservation.pending")}
        </Text>
      </td>
      {!isHistory && (
        <td>
          <Button
            color="red"
            variant="light"
            disabled={true}
            onClick={() => alert("test")}
            // onClick={() => openDenyModal(userId!, reservation.id!)}
          >{t`pages.restaurantReservations.cancelButton`}</Button>
        </td>
      )}
    </tr>
  );
}
