TRUNCATE TABLE restaurants RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE menu_items RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (999, 'mluque', '123456', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821');

/*Insert Restaurants Values*/
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
 VALUES(997, 'BurgerKing', 'Mendoza 2929', '1123346545', 0, 999);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(998, 'BurgerQueen', 'Salta 129', '1223346545', 0, 999);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(999, 'KFC', 'La Pampa 319', '1121146545', 0, 999);

/*Insert Menu Item Values*/
INSERT INTO menu_items(menu_item_id, name, description, price, restaurant_id)
VALUES(997, 'Fried Chicken Original',  'Delicious fried chicken', 3.99, 999);
INSERT INTO menu_items(menu_item_id, name, description, price, restaurant_id)
VALUES(998, 'Fried Chicken Crispy',  'Delicious fried chicken but crispy', 4.99, 999);
INSERT INTO menu_items(menu_item_id, name, description, price, restaurant_id)
VALUES(999, 'Vegan Friendly Option',  'It''s literally just water', 10.99, 999);
