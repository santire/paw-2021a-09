/*CREATE EXTENSION pg_trgm;*/

CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL PRIMARY KEY, 
  username VARCHAR(100),
  password VARCHAR(100), 
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  phone VARCHAR(100),
  is_active BOOLEAN
 );
/* ALTER TABLE IF EXISTS users ADD COLUMN is_active boolean; */

CREATE TABLE IF NOT EXISTS restaurants(
  restaurant_id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  address VARCHAR(100),
  phone_number VARCHAR(100),
  rating FLOAT,
  facebook VARCHAR(100),
  instagram VARCHAR(100),
  twitter VARCHAR(100),
  user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE
);

/*CREATE extension IF NOT EXISTS pg_trgm with schema pg_catalog;
CREATE INDEX IF NOT EXISTS trgm_idx ON restaurants USING gin (name gin_trgm_ops);*/

CREATE TABLE IF NOT EXISTS reservations(
  reservation_id SERIAL PRIMARY KEY,
  user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
  restaurant_id INTEGER REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
  date TIMESTAMP,
  quantity INTEGER,
  confirmed BOOLEAN
);

CREATE TABLE IF NOT EXISTS menu_items(
  menu_item_id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  description VARCHAR(300),
  price FLOAT,
  restaurant_id INTEGER NOT NULL REFERENCES restaurants(restaurant_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ratings(
  rating_id SERIAL PRIMARY KEY,
  rating FLOAT,
  user_comment TEXT,
  user_id INTEGER,
  restaurant_id INTEGER,
  UNIQUE(user_id, restaurant_id),
  FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY(restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE
);

/* ALTER TABLE IF EXISTS ratings ALTER COLUMN rating TYPE FLOAT; */

CREATE TABLE IF NOT EXISTS likes(
  like_id SERIAL PRIMARY KEY,
  user_id INTEGER,
  restaurant_id INTEGER,
  UNIQUE(user_id, restaurant_id),
  FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY(restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS restaurant_images(
 image_id SERIAL PRIMARY KEY,
 image_data BYTEA,
 restaurant_id INTEGER UNIQUE REFERENCES restaurants(restaurant_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS verification_tokens(
  token_id SERIAL PRIMARY KEY,
  token VARCHAR(36) UNIQUE,
  created_at TIMESTAMP,
  user_id INTEGER UNIQUE NOT NULL REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS password_tokens(
  token_id SERIAL PRIMARY KEY,
  token VARCHAR(36) UNIQUE,
  created_at TIMESTAMP,
  user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments(
    comment_id SERIAL PRIMARY KEY,
    user_id INTEGER,
    restaurant_id INTEGER,
    user_comment TEXT,
    UNIQUE(user_id, restaurant_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY(restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE
);

/* CREATE TABLE IF NOT EXISTS reviews( */
  /* review_id SERIAL PRIMARY KEY, */
  /* user_rating INTEGER, */
  /* user_comment TEXT, */
  /* user_id INTEGER, */
  /* restaurant_id INTEGER, */
  /* UNIQUE(user_id, restaurant_id), */
  /* FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE, */
  /* FOREIGN KEY(restaurant_id) REFERENCES restaurants(restaurant_id) ON DELETE CASCADE */
/* ); */

/*
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
CREATE TABLE IF NOT EXISTS restaurant_tags(
    restaurant_id INTEGER REFERENCES restaurants(restaurant_id) ON DELETE CASCADE,
    tag_id INTEGER,
    CONSTRAINT restaurants_tags_pkey PRIMARY KEY (restaurant_id, tag_id)
    );
