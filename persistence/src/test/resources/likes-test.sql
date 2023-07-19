TRUNCATE TABLE restaurants RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE likes RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (20, 'username1', '123456789', 'firstname', 'lastname', 'username1@itba.edu.ar', '123456789');
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (21, 'mluque', '123456', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821');

/*Insert Restaurants Values*/
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(996, 'myResto', 'myaddress', '123456789', 0, 20);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(997, 'BurgerKing', 'Mendoza 2929', '1123346545', 0, 21);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(998, 'BurgerQueen', 'Salta 129', '1223346545', 0, 21);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(999, 'KFC', 'La Pampa 319', '1121146545', 0, 21);


/*Insert Rating Values*/
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(11, 20, 997);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(12, 20, 998);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(13, 20, 999);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(14, 21, 997);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(15, 21, 998);
