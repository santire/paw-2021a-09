SELECT r.restaurant_id, r.name, r.address, r.phone_number,
      r.rating, r.user_id, image_data,
      AVG(price) as price, COALESCE(q,0) as reservations
FROM (SELECT *
      FROM restaurants r NATURAL JOIN restaurant_tags
      /* if has tags */
      WHERE tag_id in ("t1, t2, t3, ..., tn")
      RIGHT JOIN menu_items m ON r.restaurant_id = m.restaurant_id
      LEFT JOIN (SELECT r.restaurant_id COUNT(quantity) 
                FROM restaurants r 
                LEFT JOIN reservations b ON r.restaurant_id=b.restaurant_id
                GROUP BY r.restaurant_id) AS hot(rid, q) ON r.restaurant_id = hot.rid
                WHERE r.name ILIKE %?%
                GROUP BY r.restaurant_id,
                         r.name,
                         r.phone_number,
                         r.rating,
                         r.user_id,
                         image_data,
                         q
                HAVING AVG(price) > ? AND AVG(price) < ?
                ORDER BY ? ?
      )


SELECT r.restaurant_id, r.name, r.address, r.phone_number,
      r.rating, r.user_id, image_data,
      AVG(price) as price, COALESCE(q,0) as hotness
FROM (
      /* left join para levantar restaurants que no tengan tag tambien */
      SELECT r.* FROM restaurants r LEFT JOIN restaurant_tags rt
      ON r.restaurant_id = rt.restaurant_id
      WHERE rt.tag_id IN ("lista de tags") AND r.name ILIKE %?%
      )
/* hasta aca tengo los restaurantes filtrados por categoria y search */
/* de esta cantidad limitada de restaurantes quiero levantar imgs y menus */
LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id
LEFT JOIN restaurant_images i ON i.restaurant_id = r.restaurant_id
/* hasta aca tengo los restaurantes que cumplen con los tags y searc con menu y fotos */
LEFT JOIN (
            SELECT r.restaurant_id, COUNT(quantity)
            FROM restaurants r
            LEFT JOIN reservations b ON r.restaurant_id=b.restaurant_id
            /* para contar solo las reservas en la ultima semana */
            WHERE b.date > 'now'::timestamp - '1 week'::interval
            GROUP BY r.restaurant_id
          ) AS hot(rid, q)
        ON r.restaurant_id = hot.rid
/* con este ultimo join levanto la cantidad de reservas que tuvo */ 
/* un restaurante como la medida de su hotness */
GROUP BY r.restaurant_id,
         r.name,
         r.phone_number,
         r.rating,
         r.user_id,
         image_data,
         hotness
HAVING AVG(price) BETWEEN ? AND ?
ORDER BY ? ?
OFFSET ? FETCH NEXT ? ROWS ONLY
      


SELECT r.restaurant_id, r.name, COALESCE(q, 0) as hotness
FROM (
      SELECT r1.* FROM restaurants r1 LEFT JOIN restaurant_tags rt
      ON r1.restaurant_id = rt.restaurant_id
) AS r
LEFT JOIN (
      SELECT restaurant_id, COUNT(reservation_id)
      FROM reservations
      WHERE date > 'now'::timestamp - '1 week'::interval
      GROUP BY restaurant_id
) AS hot(rid, q)
ON r.restaurant_id = hot.rid;

SELECT CEILING(COUNT(r2.name)::numeric/6) as c
FROM (
      SELECT r.name, AVG(price) as price
      FROM (
         SELECT r1.* FROM restaurants r1 LEFT JOIN restaurant_tags rt
         ON r1.restaurant_id = rt.restaurant_id
        ) AS r
      LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id
      WHERE r.name ILIKE '%tu%'
      AND price BETWEEN 0 AND 10000
      GROUP BY r.restaurant_id, r.name
) AS r2;

