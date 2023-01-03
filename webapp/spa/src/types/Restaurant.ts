export interface Restaurant {
  id?: string;
  name: string;
  address: string;
  phoneNumber: string;
  likes: number;
  rating: number;
  facebook: string;
  instagram: string;
  twitter: string;
  tags: string[];

  menu: string;
  reviews: string;
  image: string;
  owner: string;
}
