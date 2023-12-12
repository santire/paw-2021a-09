import { useParams } from "react-router-dom";
import { NotFoundPage } from "../NotFound/NotFound";
import { UpdateRestaurantPage } from "./UpdateRestaurant";

export function ValidateRestaurantUpdate() {
  const params = useParams();
  const restaurantId = params?.restaurantId?.match(/\d+/);
  if (!restaurantId) {
    return <NotFoundPage />;
  }
  return <UpdateRestaurantPage restaurantId={parseInt(restaurantId[0])} />;
}
