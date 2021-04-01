
/*
CREATE TABLE IF NOT EXISTS users (
   user_id SERIAL PRIMARY KEY,
   username VARCHAR(100),
  password VARCHAR(100)
 );
 */

CREATE TABLE IF NOT EXISTS users ( 
  user_id SERIAL PRIMARY KEY, 
  username VARCHAR(100),
  password VARCHAR(100), 
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  phone_number VARCHAR(100)
 );

/*
CREATE TABLE IF NOT EXISTS restaurants(
  restaurant_id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  address VARCHAR(100),
  phone_number VARCHAR(100),
  rating FLOAT,
  user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tags(
  tag_id SERIAL PRIMARY KEY,
  category VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS restaurants_tags(
  restaurant_id INTEGER REFERENCES restaurants(restaurant_id),
  tag_id INTEGER REFERENCES tags(tag_id),
  CONSTRAINT restaurants_tags_pkey PRIMARY KEY (restaurant_id, tag_id)
);
*/