import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { getRestaurants } from "../api/services";
import { Restaurant } from "../types";
export function HomePage() {
  const { status, data, error } = useQuery<Restaurant[], Error>(
    [],
    getRestaurants
  );
  const { t } = useTranslation();

  if (status === "loading") {
    return <div>...</div>;
  }
  if (status === "error") {
    return <div>{error!.message}</div>;
  }
  return (
    <>
      <h1>{t`title`}</h1>
      {data ? (
        <ul>
          {data.map((r, i) => (
            <li key={i}>{JSON.stringify(r)}</li>
          ))}
        </ul>
      ) : null}
    </>
  );
}
