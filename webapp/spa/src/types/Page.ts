export interface Page<T> {
  data: T[];
  meta: {
    maxPages: number;
    perPage: number;
  };
}
