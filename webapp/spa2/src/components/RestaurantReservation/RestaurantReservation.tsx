import { Button, Skeleton, Text, useMantineTheme } from "@mantine/core";
import { IReservation } from "../../types/reservation/reservation.models";
import { DateUtils } from "../../utils/DateUtils";
import { useTranslation } from "react-i18next";
import { useState } from "react";
import { useGetUserById } from "../../hooks/user.hooks";
import { ConfirmModal } from "../ReservationModal/ConfirmModal";
import { DenyModal } from "../ReservationModal/DenyModal";

interface RestaurantReservationProps {
  reservation: IReservation;
  isHistory?: boolean;
}

export function RestaurantReservation({
  reservation,
  isHistory,
}: RestaurantReservationProps) {
  const theme = useMantineTheme();
  const { t } = useTranslation();
  const { user } = reservation;
  const [showConfirm, setShowConfirm] = useState(false);
  const [showDeny, setShowDeny] = useState(false);

  const date = new Date(reservation.date);
  const formattedDate = DateUtils.formatDate(date);
  const rid = parseInt(user.substring(user.lastIndexOf("/") + 1));
  const { data: userData, isLoading } = useGetUserById(rid);

  if (!isLoading && userData) {
    return (
      <tr>
        <td>{userData.firstName + " " + userData.lastName}</td>
        <td>{userData.phone}</td>
        <td>{reservation.quantity}</td>
        <td>{formattedDate}</td>
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
        <td>
          <Button
            color={"green"}
            variant="outline"
            disabled={isHistory || reservation.confirmed}
            onClick={() => setShowConfirm(true)}
          >{t`pages.restaurantReservations.confirmButton`}</Button>
        </td>
        <td>
          <Button
            color={"red"}
            variant="outline"
            disabled={isHistory || reservation.confirmed}
            onClick={() => setShowDeny(true)}
          >{t`pages.restaurantReservations.denyButton`}</Button>
        </td>
        <ConfirmModal
          restaurantId={rid}
          reservationId={reservation.reservationId}
          show={showConfirm}
          setShow={setShowConfirm}
        />
        <DenyModal
          restaurantId={rid}
          reservationId={reservation.reservationId}
          show={showDeny}
          setShow={setShowDeny}
        />
      </tr>
    );
  }

  return (
    <tr key={reservation.reservationId}>
      <td>
        <Skeleton height={18} mt={4} radius="xl" width={60} />
      </td>
      <td>
        <Skeleton height={18} mt={4} radius="xl" width={30} />
      </td>
      <td>{reservation.quantity}</td>
      <td>{formattedDate}</td>
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
      <td>
        <Button
          color={"green"}
          variant="outline"
          disabled={isHistory}
          onClick={() => setShowConfirm(true)}
        >{t`pages.restaurantReservations.confirmButton`}</Button>
      </td>
      <td>
        <Button
          color={"red"}
          variant="outline"
          disabled={isHistory}
          onClick={() => setShowDeny(true)}
        >{t`pages.restaurantReservations.denyButton`}</Button>
      </td>
      {/* <CancelModal */}
      {/*   restaurant={restaurantData} */}
      {/*   reservation={reservation} */}
      {/*   show={show} */}
      {/*   setShow={setShow} */}
      {/* /> */}
      {/* <CancelModal */}
      {/*   restaurant={restaurantData} */}
      {/*   reservation={reservation} */}
      {/*   show={show} */}
      {/*   setShow={setShow} */}
      {/* /> */}
    </tr>
  );
}
