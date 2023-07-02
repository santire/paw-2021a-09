export class RestaurantUtils {
    static userIsOwner(restaurantOwnerUri: string, userId: string) : boolean {
        const arr = restaurantOwnerUri.split("/").slice(-1);
        const isOwner = userId.toString() === arr![0];
        return isOwner;
    }
}