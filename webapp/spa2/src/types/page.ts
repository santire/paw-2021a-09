export interface Page<T> {
  data: T;
  meta: {
    maxPages: number;
    perPage: number;
    total: number;
  };
}

export interface PageParams {
  page: number;
  pageAmount: number;
}

export const MAX_PAGE_AMOUNT = 10;
export const DEFAULT_PAGE = {
  page: 1,
  pageAmount: MAX_PAGE_AMOUNT,
};
