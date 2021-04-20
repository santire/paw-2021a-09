TRUNCATE TABLE restaurants RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE ratings RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users(username, password, first_name, last_name, email, phone)
VALUES ('mluque', '12345678', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821');
INSERT INTO users(username, password, first_name, last_name, email, phone)
VALUES ('another', '12345678', 'axl', 'rose', 'axlrose@itba.edu.ar', '1135633821');

/*Insert Restaurants Values*/
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
 VALUES('BurgerKing', 'Mendoza 2929', '1123346545', 0, 1);
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES('BurgerQueen', 'Salta 129', '1223346545', 0, 1);

/*Insert Rating Values*/
INSERT INTO ratings(user_id, restaurant_id, rating)
 VALUES(1, 1, 5);
INSERT INTO ratings(user_id, restaurant_id, rating)
 VALUES(1, 2, 3);
INSERT INTO ratings(user_id, restaurant_id, rating)
VALUES(2, 1, 3);