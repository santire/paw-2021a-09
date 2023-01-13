export interface Restaurant {
  id?: number;
  name: string;
  address: string;
  phoneNumber: string;
  likes?: number;
  rating?: number;
  facebook?: string;
  instagram?: string;
  twitter?: string;
  tags?: string[];

  menu?: string;
  reviews?: string;
  image?: string;
  owner?: string;
}

export interface PaginatedRestaurants {
  restaurants: Restaurant[];
  maxPages: number;
}

export const tags = Object.keys({
  "arabe": "Arab",
  "americano": "American",
  "argentino": "Argentinian",
  "armenio": "Armenian",
  "asiatico": "Asian",
  "autoctono": "Local",
  "bodegon": "Bodegon",
  "chino": "Chinese",
  "cocinacasera": "Homemade",
  "contemporanea": "Contemporary",
  "deautor": "Signature",
  "defusion": "Fusion",
  "español": "Spanish",
  "frances": "French",
  "indio": "Indian",
  "internacional": "International",
  "italiano": "Italian",
  "japones": "Japanese",
  "latino": "Latino",
  "mediterraneo": "Mediterranean",
  "mexicano": "Mexican",
  "parrilla": "Grill",
  "peruano": "Peruvian",
  "pescadosymariscos": "Seafood",
  "picadas": "Picadas",
  "pizzeria": "Pizza",
  "vegetariano": "Vegetarian"
});