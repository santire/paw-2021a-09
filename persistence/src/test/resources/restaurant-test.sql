TRUNCATE TABLE restaurants RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE menu_items RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE verification_tokens RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE password_tokens RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE restaurant_tags RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (999, 'mluque', '123456', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821');
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (997, 'mluque2', '123456', 'manuel', 'luque', 'mluque2@itba.edu.ar', '1135679821');
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (998, 'mluque3', '123456', 'manuel', 'luque', 'mluque3@itba.edu.ar', '1135679821');
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (996, 'mluque4', '123456', 'manuel', 'luque', 'mluque4@itba.edu.ar', '1135679821');

/*Insert Restaurants Values*/
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(996, 'myResto', 'myaddress', '123456789', 0, 999);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(997, 'BurgerKing', 'Mendoza 2929', '1123346545', 0, 999);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(998, 'BurgerQueen', 'Salta 129', '1223346545', 0, 999);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(999, 'KFC', 'La Pampa 319', '1121146545', 0, 999);

/*Insert Menu Item Values*/
INSERT INTO menu_items(menu_item_id, name, description, price, restaurant_id)
VALUES(996, 'Whopper',  'Classic burguer', 4.99, 997);
INSERT INTO menu_items(menu_item_id, name, description, price, restaurant_id)
VALUES(997, 'Fried Chicken Original',  'Delicious fried chicken', 3.99, 999);
INSERT INTO menu_items(menu_item_id, name, description, price, restaurant_id)
VALUES(998, 'Fried Chicken Crispy',  'Delicious fried chicken but crispy', 4.99, 999);
INSERT INTO menu_items(menu_item_id, name, description, price, restaurant_id)
VALUES(999, 'Vegan Friendly Option',  'It''s literally just water', 10.99, 999);

/*Insert likes Values*/
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(3, 999 , 999);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(4, 998 , 999);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(5, 997 , 999);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(1, 999 , 997);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(6, 998 , 997);
INSERT INTO likes(like_id, user_id, restaurant_id)
VALUES(2, 999 , 998);



INSERT INTO verification_tokens(token_id, token, created_at, user_id)
VALUES(21, '0TOKEN0' , '2023-08-08 19:00:00', 999);
INSERT INTO password_tokens(token_id, token, created_at, user_id)
VALUES(21, '0PTOKEN0' , '2023-08-08 19:00:00', 999);

INSERT INTO restaurant_tags(restaurant_id, tag_id)
VALUES(999 , 0);
INSERT INTO restaurant_tags(restaurant_id, tag_id)
VALUES(999 , 1);
INSERT INTO restaurant_tags(restaurant_id, tag_id)
VALUES(998 , 4);
INSERT INTO restaurant_tags(restaurant_id, tag_id)
VALUES(997 , 5);