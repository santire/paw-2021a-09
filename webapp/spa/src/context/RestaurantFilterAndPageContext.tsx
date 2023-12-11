import { RestaurantFilterParams } from "@/types/filters";
import { PageParams } from "@/types/page";
import qs from "qs";
import { createContext, useContext, useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";

interface ParamsContextValue {
  pageParams?: PageParams;
  filterParams: RestaurantFilterParams;
  setPageParams: (newPageParams?: PageParams) => void;
  setFilterParams: (newFilterParams: RestaurantFilterParams) => void;
}

const RestaurantFilterAndPageParamsContext = createContext<
  ParamsContextValue | undefined
>(undefined);

export const RestaurantFilterAndPageParamsProvider = ({
  children,
}: {
  children: JSX.Element;
}) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filterParams, setFilterParams] = useState<RestaurantFilterParams>(
    parseFilterParams(searchParams),
  );
  const [pageParams, setPageParams] = useState<PageParams | undefined>(
    parsePageSearchParams(searchParams, "page"),
  );

  const contextValue: ParamsContextValue = {
    pageParams: pageParams ?? {page: 1},
    filterParams,
    setFilterParams,
    setPageParams,
  };

  useEffect(() => {
    setSearchParams((prev) => {
      const oldParams = qs.parse("" + prev);
      type IOldParams = typeof oldParams;
      const notFilterOrPage: IOldParams = {};

      for (const key in oldParams) {
        if (
          key !== "page" &&
          key !== "pageAmount" &&
          key !== "min" &&
          key !== "max" &&
          key !== "order" &&
          key !== "sort" &&
          key !== "tags" &&
          key !== "search"
        ) {
          notFilterOrPage[key] = oldParams[key];
        }
      }

      return (
        qs.stringify(
          { ...notFilterOrPage, ...filterParams, ...pageParams },
          { arrayFormat: "repeat" },
        ) || []
      );
    });
  }, [filterParams, pageParams]);

  return (
    <RestaurantFilterAndPageParamsContext.Provider value={contextValue}>
      {children}
    </RestaurantFilterAndPageParamsContext.Provider>
  );
};

export const useRestaurantFilterAndPage = () => {
  const context = useContext(RestaurantFilterAndPageParamsContext);
  if (!context) {
    throw new Error(
      "useRestaurantFilterAndPage must be used within a RestaurantFilterAndPageParamsProvider",
    );
  }
  return context;
};

function parsePageSearchParams(searchParams: URLSearchParams, pageP: string) {
  const pageParams: PageParams = { page: 1 };
  searchParams.forEach((value, key) => {
    switch (key) {
      case pageP: {
        const p = parseInt(value);
        const page = p <= 0 ? 1 : p;
        pageParams.page = page;
        break;
      }
    }
  });
  return pageParams;
}

function parseFilterParams(searchParams: URLSearchParams) {
  const auxFilterParams: RestaurantFilterParams = {};
  searchParams.forEach((value, key) => {
    switch (key) {
      case "min": {
        auxFilterParams.min = parseInt(value);
        break;
      }
      case "max": {
        auxFilterParams.max = parseInt(value);
        break;
      }
      case "order": {
        let order = value.toLowerCase();
        if (!["asc", "desc"].includes(order)) {
          order = "asc";
        }
        auxFilterParams.order = order;
        break;
      }
      case "sort": {
        let sort = value.toLowerCase();
        if (!["name", "hot", "price", "likes"].includes(sort)) {
          sort = "name";
        }
        auxFilterParams.sort = sort;
        break;
      }
      case "tags": {
        if (auxFilterParams.tags === undefined) {
          auxFilterParams.tags = [];
        }
        if (value !== "") {
          auxFilterParams.tags.push(value);
        }
        break;
      }
      case "search": {
        auxFilterParams.search = value;
        break;
      }
    }
  });

  return auxFilterParams;
}
