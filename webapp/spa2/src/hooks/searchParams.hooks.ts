import { useSearchParams } from "react-router-dom";
import { RestaurantFilterParams } from "../types/filters";
import { PageParams } from "../types/page";
import qs from "qs";
import { useEffect, useState } from "react";

export function useFilterSearchParams(
  initialFilterParams?: RestaurantFilterParams
): [RestaurantFilterParams | undefined, (p: RestaurantFilterParams) => void] {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filterParams, setFilterParams] = useState(initialFilterParams);

  useEffect(() => {
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
    setFilterParams(auxFilterParams);

    return () => setFilterParams({});
  }, [searchParams]);

  // If internal state changes, update Search params
  const setter = (params: RestaurantFilterParams) => {
    // By updating search params, internal state changes with useEffect
    setSearchParams(
      qs.stringify({ ...searchParams, ...params }, { arrayFormat: "repeat" })
    );
  };

  return [filterParams, setter];
}

export function usePageSearchParams(
  initialPageParams?: PageParams
): [PageParams | undefined, (p?: PageParams) => void] {
  const [searchParams, setSearchParams] = useSearchParams();
  const [pageParams, setPageParams] = useState(initialPageParams);

  // If search param changes, update internal state
  useEffect(() => {
    searchParams.forEach((value, key) => {
      switch (key) {
        case "page": {
          const p = parseInt(value);
          const page = p <= 0 ? 1 : p;
          setPageParams((prev) => ({ ...prev, page }));
          break;
        }
      }
    });
    return () => setPageParams({ page: 1 });
  }, [searchParams]);

  const setter = (params?: PageParams) => {
    // By updating search params, internal state changes with useEffect
    setSearchParams(
      qs.stringify({ ...searchParams, ...params }, { arrayFormat: "repeat" })
    );
  };

  return [pageParams, setter];
}
