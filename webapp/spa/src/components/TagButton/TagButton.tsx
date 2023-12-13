import { Badge } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useGetTags } from "../../hooks/tags.hooks";
import { useRestaurantFilterAndPage } from "@/context/RestaurantFilterAndPageContext";

export function TagButton({ tag }: { tag: string }) {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const tagList = useGetTags();

  const { setPageParams, setFilterParams } = useRestaurantFilterAndPage();

  const goToTag = () => {
    if (tagList.isSuccess) {
      setPageParams();
      setFilterParams({ tags: ["" + tagList.data.indexOf(tag)] });
      navigate(`/restaurants?tags=${tagList.data.indexOf(tag)}`);
    }
  };

  return (
    <Badge
      component="a"
      onClick={goToTag}
      color="orange"
      key={tag}
      style={{ cursor: "pointer" }}
    >
      {t("tags." + tag.toLowerCase())}
    </Badge>
  );
}
