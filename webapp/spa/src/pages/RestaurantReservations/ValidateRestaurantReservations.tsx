import { useParams } from "react-router-dom";
import { NotFoundPage } from "../NotFound/NotFound";
import { RestaurantReservationsPage } from "./RestaurantReservations";

export function ValidateRestaurantReservation() {
  const params = useParams();
  const restaurantId = params?.restaurantId?.match(/\d+/);
  if (!restaurantId) {
    return <NotFoundPage />;
  }
  return (
    <RestaurantReservationsPage restaurantId={parseInt(restaurantId[0])} />
  );
}
