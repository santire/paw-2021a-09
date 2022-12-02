import { User } from ".";

export interface Restaurant {
  id?: string;
  name: string;
  owner: User;
}
