import { IRestaurant } from '../../src/types/restaurant/restaurant.models';

const mockRestaurant: IRestaurant = {
    name: 'Sample Restaurant',
    address: '123 Main St',
    phoneNumber: '555-1234',
    tags: ['tag1', 'tag2'],
    id: 1,
    reservationsCount: 10,
    likes: 100,
    rating: 4.5,
    image: 'sample-image-url',
    reviews: 'Sample reviews',
    menu: 'Sample menu',
    owner: 'Sample Owner',
    likedByUser: false,
  };
  
  export default mockRestaurant;