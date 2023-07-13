export interface RestaurantFilterParams {
  search?: string;
  tags?: string[];
  sort?: string;
  min?: number;
  max?: number;
  order?: string;
  filterBy?: string;
}
