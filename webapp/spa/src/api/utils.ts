
import { AxiosResponse } from "axios";
import { Page } from "@/types/page";

export function pagedResponse<T>(response: AxiosResponse<T[]>) {
  const links = parseLinkHeaders(response.headers["link"]);
  const page: Page<T[]> = {
    data: response.data,
    meta: {
      perPage: response.data.length,
      maxPages: links.last,
      total: response.headers["x-total-count"] || 0,
    },
  };

  return page;
}
export function parseLinkHeaders(linksStr?: string) {
  const links = {
    first: 0,
    last: 0,
    next: 0,
    prev: 0,
  };

  linksStr?.split(",").forEach((str) => {
    const linkInfo = /<([^>]+)>;\s+rel="([^"]+)"/gi.exec(str);
    if (linkInfo != null) {
      const pageInfo = /page=([^&]*)/gi.exec(linkInfo[1]);
      if (pageInfo != null) {
        // Redudant switch because typesript
        // if (["first", "last", "next", "prev"].includes(linkInfo[2]))
        // links[linkInfo[2]] = parseInt(pageInfo[1]);
        switch (linkInfo[2]) {
          case "first": {
            links["first"] = parseInt(pageInfo[1]);
            break;
          }
          case "last": {
            links["last"] = parseInt(pageInfo[1]);
            break;
          }
          case "next": {
            links["next"] = parseInt(pageInfo[1]);
            break;
          }
          case "prev": {
            links["prev"] = parseInt(pageInfo[1]);
            break;
          }
        }
      }
    }
  });

  return links;
}
