import { RestaurantFilterParams } from "@/types/filters";
import { PageParams } from "@/types/page";
import qs from "qs";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";

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
export function useFilterSearchParams(): [
  RestaurantFilterParams,
  (p: RestaurantFilterParams) => void,
] {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filterParams, setFilterParams] = useState(
    parseFilterParams(searchParams),
  );
  const [updateParams, setUpdateParams] = useState(false);

  //
  useEffect(() => {
    console.log("updating filter params....");
    setFilterParams(parseFilterParams(searchParams));
    if (setUpdateParams) {
      setUpdateParams(false);
    }
    return () => setFilterParams({});
  }, [searchParams, updateParams]);

  const setParams = (p: RestaurantFilterParams) => {
    // console.log("filterSearch: ", { ...searchParams, ...p });
    setSearchParams(
      (prev) => {
        console.log("prev searchParams (en filter): ", prev);
        const oldParams = qs.parse("" + prev);
        type IOldParams = typeof oldParams;
        const filteredFilterParams: IOldParams = {};
        for (const key in oldParams) {
          if (
            key !== "min" &&
            key !== "max" &&
            key !== "order" &&
            key !== "sort" &&
            key !== "tags" &&
            key !== "search"
          ) {
            filteredFilterParams[key] = oldParams[key];
          }
        }

        console.log("filterfilterparams (en filter): ", filteredFilterParams);
        console.log("newparams (en filter): ", p);
        return (
          qs.stringify(
            { ...filteredFilterParams, ...p },
            { arrayFormat: "repeat" },
          ) || []
        );
      },
      { replace: true },
    );
  };

  const setter = (p: RestaurantFilterParams) => {
    setFilterParams(p);
    setUpdateParams(true);
  };

  return [filterParams, setter];
}

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

export function usePageSearchParams(
  pageName?: string,
): [PageParams | undefined, (p?: PageParams) => void] {
  const pageP = pageName ?? "page";
  const [searchParams, setSearchParams] = useSearchParams();
  const [pageParams, setPageParams] = useState<PageParams | undefined>(
    parsePageSearchParams(searchParams, pageP),
  );

  const [updateParams, setUpdateParams] = useState(false);

  useEffect(() => {
    console.log("updating page params....");
    setPageParams(parsePageSearchParams(searchParams, pageP));
    if (updateParams) {
      setUpdateParams(false);
    }
    return () => setPageParams({ page: 1 });
  }, [searchParams, updateParams]);

  const setParams = (p?: PageParams) => {
    // console.log("pageSearch: ", { ...searchParams, ...p });
    setSearchParams(
      (prev) => {
        console.log("prev searchParams (en page): ", prev);
        const oldParams = qs.parse("" + prev);
        type IOldParams = typeof oldParams;
        const filteredFilterParams: IOldParams = {};
        for (const key in oldParams) {
          if (key !== pageP && key !== "pageAmount") {
            filteredFilterParams[key] = oldParams[key];
          }
        }

        console.log("filterfilterparams (en page): ", filteredFilterParams);
        console.log("newparams (en page): ", p);
        return (
          qs.stringify(
            { ...filteredFilterParams, ...p },
            { arrayFormat: "repeat" },
          ) || []
        );
      },
      { replace: true },
    );
  };

  const setter = (p?: PageParams) => {
    setParams(p);
    setUpdateParams(true);
  };

  return [pageParams, setter];
}
