import { Button, Group, Paper, Select, Text } from "@mantine/core";
import { useTranslation } from "react-i18next";
import useStyles from "./Filter.styles";
import { TagsSelector } from "./TagsSelector/TagsSelector";
import { useEffect, useState } from "react";
import {
  useFilterSearchParams,
  usePageSearchParams,
} from "@/hooks/searchParams.hooks";
import { PriceInput } from "./PriceInput/PriceInput";

export function Filter() {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const [filterParams, setFilterParams] = useFilterSearchParams();
  const [_, setPageParams] = usePageSearchParams();

  const [errors, setErrors] = useState<string[]>([]);

  const [tags, setTags] = useState(filterParams.tags);
  const [min, setMin] = useState(filterParams.min);
  const [max, setMax] = useState(filterParams.max);

  const [sort, setSort] = useState(filterParams.sort);
  const [order, setOrder] = useState(filterParams.order);

  // I need to sync filter state with urlState
  useEffect(() => {
    setTags(filterParams?.tags);
    setMin(filterParams?.min);
    setMax(filterParams?.max);
    setSort(filterParams?.sort);
    setOrder(filterParams?.order);
  }, [filterParams]);

  const setParams = () => {
    setPageParams();
    for(let i =0; i< 100000; i++);
    setFilterParams({
      tags,
      min,
      max,
      sort,
      order,
    });
  };
  const clearAll = () => {
    setTags(undefined);
    setMin(undefined);
    setMax(undefined);
    setSort(undefined);
    setOrder(undefined);
    setPageParams();
    for(let i =0; i< 100000; i++);
    setFilterParams({});
  };

  return (
    <Paper shadow="md" radius="lg">
      <div className={classes.wrapper}>
        <div className={classes.form}>
          <Text className={classes.title} px="sm" mt="sm" mb="xl">
            {t("pages.restaurants.filter.title")}
          </Text>
          <div className={classes.fields}>
            <TagsSelector tags={tags} setTags={setTags} />
            <PriceInput
              min={min}
              max={max}
              setMin={setMin}
              setMax={setMax}
              setErrors={setErrors}
            />

            <Select
              label={t("pages.restaurants.filter.sort.label")}
              placeholder={t("pages.restaurants.filter.sort.placeholder") || ""}
              data={[
                {
                  label: t("pages.restaurants.filter.sort.name") || "name",
                  value: "name",
                },
                {
                  label: t("pages.restaurants.filter.sort.hot") || "hot",
                  value: "hot",
                },
                {
                  label: t("pages.restaurants.filter.sort.price") || "price",
                  value: "price",
                },
                {
                  label: t("pages.restaurants.filter.sort.likes") || "likes",
                  value: "likes",
                },
              ]}
              searchable
              clearable
              mb="sm"
              value={sort || null}
              onChange={(e) => setSort(e ?? undefined)}
            />
            <Select
              label={t("pages.restaurants.filter.order.label")}
              placeholder={
                t("pages.restaurants.filter.order.placeholder") || ""
              }
              data={[
                {
                  label: t("pages.restaurants.filter.order.asc") || "asc",
                  value: "asc",
                },
                {
                  label: t("pages.restaurants.filter.order.desc") || "desc",
                  value: "desc",
                },
              ]}
              searchable
              clearable
              mb="sm"
              value={order || null}
              onChange={(e) => setOrder(e ?? undefined)}
            />

            <Group position="center" mt="xl">
              <Button
                onClick={setParams}
                aria-label="filter-button"
                color="orange"
                fullWidth
                px="xl"
                disabled={errors?.length > 0}
              >
                {t("pages.restaurants.filter.submit")}
              </Button>
              <Button
                onClick={clearAll}
                aria-label="clear-button"
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
