export interface User {
  userId?: string | null;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  password?: string;
  url?: string;
}
