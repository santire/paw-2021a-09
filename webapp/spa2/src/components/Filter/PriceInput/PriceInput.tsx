import { NumberInput } from "@mantine/core";
import { Dispatch, SetStateAction, useEffect } from "react";
import { useTranslation } from "react-i18next";

interface PriceInputProps {
  min?: number;
  setMin: Dispatch<SetStateAction<number | undefined>>;

  max?: number;
  setMax: Dispatch<SetStateAction<number | undefined>>;

  setErrors: Dispatch<SetStateAction<string[]>>;
}
export function PriceInput({
  min,
  setMin,
  max,
  setMax,
  setErrors,
}: PriceInputProps) {
  const { t } = useTranslation();

  useEffect(() => {
    if (min && max && min > max) {
      setErrors((prev) => [...prev, t("filter.priceInput.minLargerThanMax")]);
    } else if (min && max && max < min) {
      setErrors((prev) => [...prev, t("filter.priceInput.maxSmallerThanMin")]);
    } else {
      setErrors([]);
    }
  }, [min, max]);
  return (
    <>
      <NumberInput
        label={t("pages.restaurants.filter.min.label")}
        placeholder={t("pages.restaurants.filter.min.placeholder") || ""}
        value={min}
        min={1}
        max={10000}
        mb="sm"
        onChange={(e) => setMin(e && e <= 10000 && e >= 1 ? e : 1)}
        error={
          min && max && min > max
            ? t("filter.priceInput.minLargerThanMax")
            : null
        }
      />
      <NumberInput
        label={t("pages.restaurants.filter.max.label")}
        placeholder={t("pages.restaurants.filter.max.placeholder") || ""}
        value={max}
        min={1}
        max={10000}
        mb="sm"
        onChange={(e) => setMax(e && e <= 10000 && e >= 1 ? e : 10000)}
        error={
          min && max && max < min
            ? t("filter.priceInput.maxSmallerThanMin")
            : null
        }
      />
    </>
  );
}
