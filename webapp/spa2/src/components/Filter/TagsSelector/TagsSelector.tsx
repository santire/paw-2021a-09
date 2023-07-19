import { useTranslation } from "react-i18next";
import { MultiSelect } from "@mantine/core";
import { useGetTags } from "../../../hooks/tags.hooks";
import { Dispatch, SetStateAction } from "react";

interface TagsSelectorProps {
  tags?: string[];
  setTags: Dispatch<SetStateAction<string[] | undefined>>;
}
export function TagsSelector({ tags, setTags }: TagsSelectorProps) {
  const { t } = useTranslation();
  const tagsList = useGetTags();

  if (!tagsList.isSuccess) {
    return (
      <MultiSelect
        label={t("pages.restaurants.filter.tags.label")}
        placeholder={t("pages.restaurants.filter.tags.placeholder") || ""}
        data={[]}
        searchable
        clearable
        mb="sm"
        maxSelectedValues={6}
        value={[]}
      />
    );
  }

  const tagsData: { value: string; label: string }[] = [];
  for (let tagIdx = 0; tagIdx < tagsList.data.length; tagIdx++) {
    tagsData.push({
      value: "" + tagIdx,
      label: t(`tags.${tagsList.data[tagIdx].toLowerCase()}`),
    });
  }

  return (
    <MultiSelect
      label={t("pages.restaurants.filter.tags.label")}
      placeholder={t("pages.restaurants.filter.tags.placeholder") || ""}
      data={tagsData}
      value={tags ?? []}
      searchable
      clearable
      mb="sm"
      maxSelectedValues={6}
      onChange={setTags}
    />
  );
}
