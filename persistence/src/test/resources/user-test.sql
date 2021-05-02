TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
INSERT INTO users(username, password, first_name, last_name, email, phone, is_active) VALUES('paw','paw','paw','paw','paw','paw', false);
