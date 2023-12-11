import { useSearchParams } from "react-router-dom";
import { RestaurantFilterParams } from "../types/filters";
import { PageParams } from "../types/page";
import qs from "qs";
import { useEffect, useState } from "react";

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

  useEffect(() => {
    setFilterParams(parseFilterParams(searchParams));

    return () => setFilterParams({});
  }, [searchParams]);

  // If internal state changes, update Search params
  const setter = (params: RestaurantFilterParams) => {
    // By updating search params, internal state changes with useEffect
    const oldParams = qs.parse("" + searchParams);
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
    setSearchParams(
      qs.stringify(
        { ...filteredFilterParams, ...params },
        { arrayFormat: "repeat" },
      ) || [],
      { replace: true },
    );
  };

  return [filterParams, setter];
}

export function useTabSearchParam(
  initialValue?: string,
): [string | undefined, (tab: string) => void] {
  const [searchParams, setSearchParams] = useSearchParams();
  const [tab, setTab] = useState<string>();

  // If search param changes, update internal state
  useEffect(() => {
    searchParams.forEach((value, key) => {
      switch (key) {
        case "tab": {
          setTab(value);
          break;
        }
      }
    });
    return () => setTab(initialValue);
  }, [searchParams]);

  const setter = (tab: string) => {
    // By updating search params, internal state changes with useEffect
    const oldParams = qs.parse("" + searchParams);
    setSearchParams(
      qs.stringify(
        {
          ...oldParams,
          tab: tab,
        },
        { arrayFormat: "repeat" },
      ),
    );
  };

  return [tab, setter];
}

function parsePageSearchParams(searchParams: URLSearchParams, pageP: string) {
  const pageParams: PageParams = { page: 1 };
  searchParams.forEach((value, key) => {
    switch (key) {
      case pageP: {
        const p = parseInt(value);
        const page = p <= 0 ? 1 : p;
        // setPageParams((prev) => ({ ...prev, page }));
        pageParams.page = page;
        break;
      }
    }
  });
  return pageParams;
}

export function usePageSearchParams(
  pageName?: string,
): [PageParams, (p?: PageParams) => void] {
  const pageP = pageName ?? "page";
  const [searchParams, setSearchParams] = useSearchParams();
  const [pageParams, setPageParams] = useState(
    parsePageSearchParams(searchParams, pageP),
  );

  // If search param changes, update internal state
  useEffect(() => {
    searchParams.forEach((value, key) => {
      switch (key) {
        case pageP: {
          const p = parseInt(value);
          const page = p <= 0 ? 1 : p;
          setPageParams((prev) => ({ ...prev, page }));
          break;
        }
      }
    });
    return () => setPageParams({ page: 1 });
  }, [searchParams, setPageParams]);

  const setter = (params?: PageParams) => {
    // By updating search params, internal state changes with useEffect
    const oldParams = qs.parse("" + searchParams);
    type IOldParams = typeof oldParams;
    const filteredFilterParams: IOldParams = {};
    for (const key in oldParams) {
      if (key !== pageP && key !== "pageAmount") {
        filteredFilterParams[key] = oldParams[key];
      }
    }
    setSearchParams(
      qs.stringify(
        {
          ...filteredFilterParams,
          [pageP]: params?.page,
          pageAmount: params?.pageAmount,
        },
        { arrayFormat: "repeat" },
      ) || [],
      { replace: true },
    );
  };

  return [pageParams, setter];
}
