TRUNCATE TABLE restaurants RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE reservations RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (998, 'username1', '123456789', 'firstname', 'lastname', 'username1@itba.edu.ar', '123456789');
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (999, 'mluque', '123456', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821');

/*Insert Restaurants Values*/
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(997, 'BurgerKing', 'Mendoza 2929', '1123346545', 0, 999);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(998, 'BurgerQueen', 'Salta 129', '1223346545', 0, 999);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(999, 'KFC', 'La Pampa 319', '1121146545', 0, 999);

/*Insert Reservations Values*/
INSERT INTO reservations(reservation_id, user_id, restaurant_id, date, quantity, confirmed)
VALUES (1, 999, 997, '2023-08-08 19:00:00', 4, false)
    INSERT INTO reservations(reservation_id, user_id, restaurant_id, date, quantity, confirmed)
VALUES (2, 999, 998, '2023-08-08 18:00:00', 2, true)
INSERT INTO reservations(reservation_id, user_id, restaurant_id, date, quantity, confirmed)
VALUES (3, 998, 997, '2023-08-08 19:30:00', 4, true)
INSERT INTO reservations(reservation_id, user_id, restaurant_id, date, quantity, confirmed)
VALUES (4, 999, 997, '2023-08-08 20:10:25', 3, true)
INSERT INTO reservations(reservation_id, user_id, restaurant_id, date, quantity, confirmed)
VALUES (5, 998, 999, '2023-08-08 21:00:00', 2, false)
