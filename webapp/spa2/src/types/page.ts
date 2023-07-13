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

