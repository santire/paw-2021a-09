import { IUser } from "@/types/user/user.models";

export const MOCK_USER: IUser = {
  userId: 1,
  username: "mockUser",
  firstName: "Mock",
  lastName: "user",
  email: "mock@gourmetable.com",
  phone: "12312312",
  self: "http://localhost:8080/app/api/users/1",
  likes: "http://localhost:8080/app/api/users/1/likes",
  ratings: "http://localhost:8080/app/api/users/1/ratings",
  reservations: "http://localhost:8080/app/api/reservations?madeBy=1",
  comments: "http://localhost:8080/app/api/comments?madeBy=1",
  restaurants: "http://localhost:8080/app/api/restaurants?ownedBy=1",
};
