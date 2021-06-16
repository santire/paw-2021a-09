TRUNCATE TABLE restaurants RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users(user_id, username, password, first_name, last_name, email, phone, is_active)
VALUES (1, 'mluque', '12345678', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821', true);
INSERT INTO users(user_id, username, password, first_name, last_name, email, phone, is_active)
VALUES (2, 'another', '12345678', 'axl', 'rose', 'axlrose@itba.edu.ar', '1135633821', true);

/*Insert Restaurants Values*/
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
 VALUES(1, 'BurgerKing', 'Mendoza 2929', '1123346545', 0, 1);
INSERT INTO restaurants(restaurant_id, name, address, phone_number, rating, user_id)
VALUES(2, 'BurgerQueen', 'Salta 129', '1223346545', 0, 1);

/*Insert Comment*/
INSERT INTO comments(comment_id, date, user_comment, restaurant_id, user_id)
 VALUES(1, TO_DATE('2021/05/15', 'YYYY/MM/DD'),'first', 1, 1);
