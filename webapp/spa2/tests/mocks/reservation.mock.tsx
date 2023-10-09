import { IReservation } from '../../src/types/reservation/reservation.models';

const mockReservation: IReservation = {
    "reservationId": 54,
    "date": new Date("2023-10-02T20:30:00"), // Convert the string to a Date object
    "quantity": 1,
    "confirmed": false,
    "user": "http://localhost:8080/app/api/users/2",
    "restaurant": "http://localhost:8080/app/api/restaurants/1"
  };
  
  export default mockReservation;