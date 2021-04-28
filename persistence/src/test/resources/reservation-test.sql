TRUNCATE TABLE restaurants RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE reservations RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users (username, password, first_name, last_name, email, phone)
VALUES ('mluque', '123456', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821');

/*Insert Restaurants Values*/
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES('BurgerKing', 'Mendoza 2929', '1123346545', 0, 1);
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES('BurgerQueen', 'Salta 129', '1223346545', 0, 1);
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES('KFC', 'La Pampa 319', '1121146545', 0, 1);

/*Insert Reservations Values*/
INSERT INTO reservations(user_id, restaurant_id, date, quantity)
VALUES (1, 1, '2021-05-01 19:10:25', 4)
