import { PageParams } from "@/types/page";
import qs from "qs";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";

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

export function usePageSearchParams(
  initialPageParams?: PageParams,
  pageName?: string
): [PageParams, (p?: PageParams) => void] {
  const [searchParams, setSearchParams] = useSearchParams();
  const [pageParams, setPageParams] = useState(
    initialPageParams || { page: 1 }
  );
  const pageP = pageName ?? "page";

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
  }, [searchParams]);

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
        { arrayFormat: "repeat" }
      ) || [],
      { replace: true }
    );
  };

  return [pageParams, setter];
}
