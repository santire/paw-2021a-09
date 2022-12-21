import {
  Button,
  Group,
  MultiSelect,
  NumberInput,
  Paper,
  Select,
  Text,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import useStyles from "./Filter.styles";
import { FilterParams } from "../../api/services";
import { Dispatch, SetStateAction, useEffect, useState } from "react";

interface FilterParamsProps {
  params: FilterParams;
  setParams: Dispatch<SetStateAction<FilterParams>>;
  apply: () => void;
  clear: () => void;
}

const TAGS = [
  "arabe",
  "americano",
  "argentino",
  "armenio",
  "asiatico",
  "autoctono",
  "bodegon",
  "chino",
  "cocinacasera",
  "contemporanea",
  "deautor",
  "defusion",
  "español",
  "frances",
  "indio",
  "internacional",
  "italiano",
  "japones",
  "latino",
  "mediterraneo",
  "mexicano",
  "parrilla",
  "peruano",
  "pescadosymariscos",
  "picadas",
  "pizzeria",
  "vegetariano",
];

export function Filter({ params, setParams, apply, clear }: FilterParamsProps) {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const { t: en } = useTranslation();
  const { t: es } = useTranslation("es");

  const setParam = <T extends FilterParams, U extends keyof T>(
    key: U,
    value: T[U]
  ) => setParams((prev) => ({ ...prev, [key]: value }));

  const getOptionsArr = (keyword: string) => {
    return [en(keyword), es(keyword)];
  };

  const getOrderValue = (value: string) => {
    const asc = getOptionsArr("pages.restaurants.filter.order.asc");
    return asc.map((t) => t.toLowerCase()).includes(value.toLowerCase())
      ? "asc"
      : "desc";
  };

  const getTagValue = (values: string[]) => {
    const tagOptions: { [key: string]: string[] } = {};
    for (let tag of TAGS) {
      tagOptions[tag] = getOptionsArr(`tags.${tag}`);
    }
    const toReturn: string[] = [];
    Object.entries(tagOptions).forEach(([_, val], index) => {
      for (let tagVal of values) {
        if (val.map((t) => t.toLowerCase()).includes(tagVal.toLowerCase())) {
          toReturn.push("" + index);
        }
      }
    });

    return toReturn;
  };

  const getSortValue = (value: string) => {
    const prefix = "pages.restaurants.filter.sort";
    const sortOptions = {
      name: getOptionsArr(`${prefix}.name`),
      hot: getOptionsArr(`${prefix}.hot`),
      price: getOptionsArr(`${prefix}.price`),
      likes: getOptionsArr(`${prefix}.likes`),
    };
    let toReturn = "name";
    Object.entries(sortOptions).forEach(([key, val]) => {
      if (val.map((t) => t.toLowerCase()).includes(value.toLowerCase())) {
        toReturn = key;
      }
    });

    return toReturn;
  };

  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <div className={classes.form}>
          <Text className={classes.title} px="sm" mt="sm" mb="xl">
            {t("pages.restaurants.filter.title")}
          </Text>
          <div className={classes.fields}>
            <MultiSelect
              label={t("pages.restaurants.filter.tags.label")}
              placeholder={t("pages.restaurants.filter.tags.placeholder") || ""}
              data={TAGS.map((tag) => t(`tags.${tag}`) || "")}
              searchable
              clearable
              mb="sm"
              maxSelectedValues={6}
              value={params.tags?.map((i) => t(`tags.${TAGS[parseInt(i)]}`))}
              onChange={(e) => {
                setParam("tags", getTagValue(e ?? "[]"));
              }}
            />

            <NumberInput
              label={t("pages.restaurants.filter.min.label")}
              placeholder={t("pages.restaurants.filter.min.placeholder") || ""}
              value={params.min}
              min={1}
              max={10000}
              mb="sm"
              onChange={(e) =>
                setParam("min", e && e <= 10000 && e >= 1 ? e : 1)
              }
              error={
                params.min && params.max && params.min > params.max
                  ? "min can't be larger than max"
                  : null
              }
            />
            <NumberInput
              label={t("pages.restaurants.filter.max.label")}
              placeholder={t("pages.restaurants.filter.max.placeholder") || ""}
              value={params.max}
              min={1}
              max={10000}
              mb="sm"
              onChange={(e) =>
                setParam("max", e && e <= 10000 && e >= 1 ? e : 10000)
              }
              error={
                params.min && params.max && params.max < params.min
                  ? "max can't be smaller than min"
                  : null
              }
            />

            <Select
              label={t("pages.restaurants.filter.sort.label")}
              placeholder={t("pages.restaurants.filter.sort.placeholder") || ""}
              data={[
                t("pages.restaurants.filter.sort.name") || "name",
                t("pages.restaurants.filter.sort.hot") || "hot",
                t("pages.restaurants.filter.sort.price") || "price",
                t("pages.restaurants.filter.sort.likes") || "likes",
              ]}
              searchable
              clearable
              mb="sm"
              value={t(`pages.restaurants.filter.sort.${params.sort}`)}
              onChange={(e) => {
                setParam("sort", getSortValue(e ?? ""));
              }}
            />
            <Select
              label={t("pages.restaurants.filter.order.label")}
              placeholder={
                t("pages.restaurants.filter.order.placeholder") || ""
              }
              // TODO: i18n
              data={[
                t("pages.restaurants.filter.order.asc") || "asc",
                t("pages.restaurants.filter.order.desc") || "desc",
              ]}
              searchable
              clearable
              mb="sm"
              value={t(`pages.restaurants.filter.order.${params.order}`)}
              onChange={(e) => {
                setParam("order", getOrderValue(e ?? ""));
              }}
            />

            <Group position="center" mt="xl">
              <Button onClick={apply} color="orange" fullWidth px="xl">
                {t("pages.restaurants.filter.submit")}
              </Button>
              <Button
                onClick={clear}
                color="orange"
                variant="outline"
                fullWidth
                px="xl"
              >
                {t("pages.restaurants.filter.clear")}
              </Button>
            </Group>
          </div>
        </div>
      </div>
    </Paper>
  );
}
