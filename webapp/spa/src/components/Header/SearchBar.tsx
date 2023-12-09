import { Autocomplete } from "@mantine/core";
import { IconSearch } from "@tabler/icons-react";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { createSearchParams, useNavigate } from "react-router-dom";
import useStyles from "./Header.styles";
import { useFilterSearchParams } from "../../hooks/searchParams.hooks";

export function SearchBar() {
  const { classes } = useStyles();
  const { t } = useTranslation();
  const [search, setSearch] = useState("");
  const [filterParams, setFilterParams] = useFilterSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    setSearch(filterParams?.search ?? "");
  }, [filterParams]);

  return (
    <Autocomplete
      aria-label="search-input"
      className={classes.search}
      placeholder={t("header.search") || ""}
      icon={<IconSearch size={16} stroke={1.5} />}
      value={search}
      onChange={(e) => setSearch(e.trim())}
      data={[]}
      onKeyDown={(e) => {
        if (e.key === "Enter") {
          setFilterParams({ search: search });
          navigate({
            pathname: "restaurants",
            search: `${createSearchParams({ search: search })}`,
          });
        }
      }}
      styles={(theme) => ({
        input: {
          background:
            theme.colorScheme === "dark"
              ? theme.colors.dark[4]
              : theme.colors.gray[0],
          color:
            theme.colorScheme === "dark"
              ? theme.colors.dark[1]
              : theme.colors.gray[8],
        },
      })}
    />
  );
}
