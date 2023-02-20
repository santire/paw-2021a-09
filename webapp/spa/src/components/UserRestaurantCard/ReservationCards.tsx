import React from 'react'
import { Reservation } from '../../types';
import {
  Button,
	Text
} from "@mantine/core";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";
import {
  getRestaurantReservations,
} from "../../api/services";
import {ReservationCard} from '../ReservationCard/ReservationCard';


interface ReservationCardsProps {
  id: string
  closeModal: any
}

export default function ReservationCards({ id, closeModal }: ReservationCardsProps) {
  const {
    status,
    data: reservations,
    error,
  } = useQuery<Reservation[], Error>(
    ["reservation"],
    async () => getRestaurantReservations(id || ""),
    { retry: false }
  );

  return (
		<>
    {reservations?.map((reservation:any) => {
          return ( 
            <ReservationCard reservation={reservation} hideRestaurantData={true} closeModal={closeModal} />
    )})}
		</>
  )
}
