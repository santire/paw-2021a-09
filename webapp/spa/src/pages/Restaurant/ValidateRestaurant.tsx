import { useParams } from "react-router-dom";
import { NotFoundPage } from "../NotFound/NotFound";
import { RestaurantPage } from "./Restaurant";

export function ValidateRestaurant() {
  const params = useParams();
  const restaurantId = params?.restaurantId?.match(/\d+/);
  if (!restaurantId) {
    return <NotFoundPage />;
  }
  return <RestaurantPage restaurantId={parseInt(restaurantId[0])} />;
}
