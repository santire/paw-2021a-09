TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE verification_tokens RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE password_tokens RESTART IDENTITY AND COMMIT NO CHECK;

/*Insert Users values*/
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (999, 'mluque', '123456', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821');
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (997, 'mluque2', '123456', 'manuel', 'luque', 'mluque2@itba.edu.ar', '1135679821');
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (998, 'mluque3', '123456', 'manuel', 'luque', 'mluque3@itba.edu.ar', '1135679821');
INSERT INTO users (user_id, username, password, first_name, last_name, email, phone)
VALUES (996, 'mluque4', '123456', 'manuel', 'luque', 'mluque4@itba.edu.ar', '1135679821')