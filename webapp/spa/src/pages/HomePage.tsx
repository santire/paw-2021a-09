import { useQuery } from "react-query";
import { getRestaurants } from "../api/services";
import { Restaurant } from "../types";
export function HomePage() {
  const { status, data, error } = useQuery<Restaurant[], Error>(
    [],
    getRestaurants
  );

  if (status === "loading") {
    return <div>...</div>;
  }
  if (status === "error") {
    return <div>{error!.message}</div>;
  }
  return data ? (
    <ul>
      {data.map((r, i) => (
        <li key={i}>{JSON.stringify(r)}</li>
      ))}
    </ul>
  ) : null;
}
