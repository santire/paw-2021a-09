TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
INSERT INTO users(username, password, first_name, last_name, email, phone) VALUES('paw','paw','paw','paw','paw','paw');
