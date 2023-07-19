import { Badge } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useGetTags } from "../../hooks/tags.hooks";

export function TagButton({ tag }: { tag: string }) {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const tagList = useGetTags();

  return (
    <Badge
      component="a"
      onClick={() =>
        tagList.isSuccess
          ? navigate(`/restaurants?tags=${tagList.data.indexOf(tag)}`)
          : null
      }
      color="orange"
      key={tag}
      style={{ cursor: "pointer" }}
    >
      {t("tags." + tag.toLowerCase())}
    </Badge>
  );
}
