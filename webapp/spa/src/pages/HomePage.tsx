import { useTranslation } from "react-i18next";
import { useQuery } from "react-query";
import { getRestaurants } from "../api/services";
import i18n from "i18next";
import { Restaurant } from "../types";

const LANGS = [
  { code: "en", nativeName: "English" },
  { code: "es", nativeName: "Español" },
];

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
      <div>
        {LANGS.map((lng) => {
          return (
            <button
              className="m-4 p-2 bg-blue-600 rounded"
              key={lng.code}
              type="submit"
              onClick={() => i18n.changeLanguage(lng.code)}
            >
              {lng.nativeName}
            </button>
          );
        })}
      </div>
    </>
  );
}
