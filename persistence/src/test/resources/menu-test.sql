TRUNCATE TABLE menu_items RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE restaurants RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users (username, password, first_name, last_name, email, phone)
VALUES ('santire', '5up3254f3p455w02d', 'santiago', 'reyes', 'sreyes@itba.edu.ar', '1122223333');

/*Insert Restaurants Values*/
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES('KFC', 'La Pampa 319', '1121146545', 5.0, 1);


/*Insert Menu Item Values*/
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES('Fried Chicken Original',  'Delicious fried chicken', 3.99, 1);
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES('Fried Chicken Crispy',  'Delicious fried chicken but crispy', 4.99, 1);
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES('Vegan Friendly Option',  'It''s literally just water', 10.99, 1);


