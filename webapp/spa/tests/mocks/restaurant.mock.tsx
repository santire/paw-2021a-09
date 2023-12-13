import { IRestaurant } from '../../src/types/restaurant/restaurant.models';

const mockRestaurant: IRestaurant = {
    name: 'Sample Restaurant',
    address: '123 Main St',
    phoneNumber: '555-1234',
    facebook: "facebook",
    instagram: "instagram",
    twitter: "twitter",
    tags: ['tag1', 'tag2'],
    id: 1,
    reservationsCount: 10,
    likes: 10,
    rating: 4.5,
    image: "http://localhost:8080/app/api/restaurants/1/image?v=0",
    self: "http://localhost:8080/app/api/restaurants/1",
    menu: "http://localhost:8080/app/api/restaurants/1/menu",
    owner: "http://localhost:8080/app/api/users/1",
    comments: "http://localhost:8080/app/api/comments?madeTo=1"
  };
  
  export default mockRestaurant;