

/*Insert Users values*/
/*INSERT INTO users (username, password, first_name, last_name, email, phone)
VALUES ('mluque', '123456', 'manuel', 'luque', 'mluque@itba.edu.ar', '1135679821');
*/

/************************/

/* INSERT QUERY NO: 1 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Oporto Almacen', '11 de Septiembre de 1888 4152  C1429 Buenos Aires', 47123456, 9, 1
);

/* INSERT QUERY NO: 2 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Tucson Soho', 'Uriarte 1641  C1414 Buenos Aires', 47123456, 8.9, 1
);

/* INSERT QUERY NO: 3 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Cucina D'' onore', 'Alicia Moreau de Justo 1768  Buenos Aires', 47123456, 9.4, 1
);

/* INSERT QUERY NO: 4 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Koh Lanta (Palermo)', 'Gorriti 4647  C1414BJI Buenos Aire', 47123456, 9, 1
);

/* INSERT QUERY NO: 5 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Mumbai', 'Honduras 5684  C1414BNF Buenos Aires', 47123456, 8.7, 1
);

/* INSERT QUERY NO: 6 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Viejo Mundo (La Paternal)', 'Av. Warnes 2702  C1427DPT CABA', 47123456, 9, 1
);

/* INSERT QUERY NO: 7 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Izakaya by Sushi Pop (La Plata)', 'Diagonal 74 1594  B1900 La Plata', 47123456, 9.2, 1
);

/* INSERT QUERY NO: 8 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Cortez Restaurant & Bar de Vinos', 'Avenida 44 n° 1102 La Plata 1900  La Plata', 47123456, 8.5, 1
);

/* INSERT QUERY NO: 9 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'San Isidro Golf Restaurant By La Estaca', 'Av. Juan Segundo Fernández 386  B1642AMQ San Isidro (Buenos Aires)', 47123456, 8.7, 1
);

/* INSERT QUERY NO: 10 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Blossom (San Isidro)', 'Av. del Libertador 16236  B1642 San Isidro (Buenos Aires)', 47123456, 9.2, 1
);

/* INSERT QUERY NO: 11 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Deriva', 'Dardo Rocha 2290  Zona Norte  B1640FTD San Isidro (Buenos Aires', 47123456, 8.5, 1
);

/* INSERT QUERY NO: 12 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Dandy (Acassuso)', 'Av. del Libertador 14805  B1641ANC San Isidro (Buenos Aires', 47123456, 8.1, 1
);

/* INSERT QUERY NO: 13 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Sudeste', 'Av Tiscornia 962  B1642DVP San Isidro (Buenos Aires)', 47123456, 9.5, 1
);

/* INSERT QUERY NO: 14 */
INSERT INTO restaurants(name, address, phone_number, rating, user_id)
VALUES
(
'Amada Cocina Mexica', 'Eduardo Costa 1010  B1641AFN San Isidro (Buenos Aires)', 47123456, 9, 1
);


/******************************/


/* INSERT QUERY NO: 1 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 1
);

/* INSERT QUERY NO: 2 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 1
);

/* INSERT QUERY NO: 3 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 1
);

/* INSERT QUERY NO: 4 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 1
);

/* INSERT QUERY NO: 5 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 1
);

/* INSERT QUERY NO: 6 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 1
);

/* INSERT QUERY NO: 7 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 1
);

/* INSERT QUERY NO: 8 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 1
);

/* INSERT QUERY NO: 9 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 1
);

/* INSERT QUERY NO: 10 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 1
);

/* INSERT QUERY NO: 11 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 1
);

/* INSERT QUERY NO: 12 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 1
);

/* INSERT QUERY NO: 13 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 1
);

/* INSERT QUERY NO: 14 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 1
);

/* INSERT QUERY NO: 15 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 1
);

/* INSERT QUERY NO: 16 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 1
);

/* INSERT QUERY NO: 17 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 1
);

/* INSERT QUERY NO: 18 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 1
);

/* INSERT QUERY NO: 19 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 1
);

/* INSERT QUERY NO: 20 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 1
);

/* INSERT QUERY NO: 21 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 1
);

/* INSERT QUERY NO: 22 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 1
);

/* INSERT QUERY NO: 23 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 1
);

/* INSERT QUERY NO: 24 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 1
);

/* INSERT QUERY NO: 25 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 1
);

/* INSERT QUERY NO: 26 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 1
);

/* INSERT QUERY NO: 27 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 1
);

/* INSERT QUERY NO: 28 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 1
);

/* INSERT QUERY NO: 29 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 1
);

/* INSERT QUERY NO: 30 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 1
);

/* INSERT QUERY NO: 31 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 1
);

/* INSERT QUERY NO: 32 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 1
);

/* INSERT QUERY NO: 33 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 1
);

/* INSERT QUERY NO: 34 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 1
);

/* INSERT QUERY NO: 35 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 1
);

/* INSERT QUERY NO: 36 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 1
);

/* INSERT QUERY NO: 37 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 1
);

/* INSERT QUERY NO: 38 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 1
);

/* INSERT QUERY NO: 39 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 2
);

/* INSERT QUERY NO: 40 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 2
);

/* INSERT QUERY NO: 41 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 2
);

/* INSERT QUERY NO: 42 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 2
);

/* INSERT QUERY NO: 43 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 2
);

/* INSERT QUERY NO: 44 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 2
);

/* INSERT QUERY NO: 45 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 2
);

/* INSERT QUERY NO: 46 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 2
);

/* INSERT QUERY NO: 47 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 2
);

/* INSERT QUERY NO: 48 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 2
);

/* INSERT QUERY NO: 49 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 2
);

/* INSERT QUERY NO: 50 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 2
);

/* INSERT QUERY NO: 51 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 2
);

/* INSERT QUERY NO: 52 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 2
);

/* INSERT QUERY NO: 53 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 2
);

/* INSERT QUERY NO: 54 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 2
);

/* INSERT QUERY NO: 55 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 2
);

/* INSERT QUERY NO: 56 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 2
);

/* INSERT QUERY NO: 57 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 2
);

/* INSERT QUERY NO: 58 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 2
);

/* INSERT QUERY NO: 59 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 2
);

/* INSERT QUERY NO: 60 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 2
);

/* INSERT QUERY NO: 61 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 2
);

/* INSERT QUERY NO: 62 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 2
);

/* INSERT QUERY NO: 63 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 2
);

/* INSERT QUERY NO: 64 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 2
);

/* INSERT QUERY NO: 65 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 2
);

/* INSERT QUERY NO: 66 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 2
);

/* INSERT QUERY NO: 67 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 2
);

/* INSERT QUERY NO: 68 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 2
);

/* INSERT QUERY NO: 69 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 2
);

/* INSERT QUERY NO: 70 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 2
);

/* INSERT QUERY NO: 71 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 2
);

/* INSERT QUERY NO: 72 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 2
);

/* INSERT QUERY NO: 73 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 2
);

/* INSERT QUERY NO: 74 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 2
);

/* INSERT QUERY NO: 75 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 2
);

/* INSERT QUERY NO: 76 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 2
);

/* INSERT QUERY NO: 77 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 3
);

/* INSERT QUERY NO: 78 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 3
);

/* INSERT QUERY NO: 79 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 3
);

/* INSERT QUERY NO: 80 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 3
);

/* INSERT QUERY NO: 81 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 3
);

/* INSERT QUERY NO: 82 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 3
);

/* INSERT QUERY NO: 83 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 3
);

/* INSERT QUERY NO: 84 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 3
);

/* INSERT QUERY NO: 85 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 3
);

/* INSERT QUERY NO: 86 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 3
);

/* INSERT QUERY NO: 87 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 3
);

/* INSERT QUERY NO: 88 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 3
);

/* INSERT QUERY NO: 89 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 3
);

/* INSERT QUERY NO: 90 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 3
);

/* INSERT QUERY NO: 91 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 3
);

/* INSERT QUERY NO: 92 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 3
);

/* INSERT QUERY NO: 93 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 3
);

/* INSERT QUERY NO: 94 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 3
);

/* INSERT QUERY NO: 95 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 3
);

/* INSERT QUERY NO: 96 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 3
);

/* INSERT QUERY NO: 97 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 3
);

/* INSERT QUERY NO: 98 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 3
);

/* INSERT QUERY NO: 99 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 3
);

/* INSERT QUERY NO: 100 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 3
);

/* INSERT QUERY NO: 101 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 3
);

/* INSERT QUERY NO: 102 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 3
);

/* INSERT QUERY NO: 103 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 3
);

/* INSERT QUERY NO: 104 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 3
);

/* INSERT QUERY NO: 105 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 3
);

/* INSERT QUERY NO: 106 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 3
);

/* INSERT QUERY NO: 107 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 3
);

/* INSERT QUERY NO: 108 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 3
);

/* INSERT QUERY NO: 109 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 3
);

/* INSERT QUERY NO: 110 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 3
);

/* INSERT QUERY NO: 111 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 3
);

/* INSERT QUERY NO: 112 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 3
);

/* INSERT QUERY NO: 113 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 3
);

/* INSERT QUERY NO: 114 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 3
);

/* INSERT QUERY NO: 115 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 4
);

/* INSERT QUERY NO: 116 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 4
);

/* INSERT QUERY NO: 117 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 4
);

/* INSERT QUERY NO: 118 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 4
);

/* INSERT QUERY NO: 119 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 4
);

/* INSERT QUERY NO: 120 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 4
);

/* INSERT QUERY NO: 121 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 4
);

/* INSERT QUERY NO: 122 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 4
);

/* INSERT QUERY NO: 123 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 4
);

/* INSERT QUERY NO: 124 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 4
);

/* INSERT QUERY NO: 125 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 4
);

/* INSERT QUERY NO: 126 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 4
);

/* INSERT QUERY NO: 127 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 4
);

/* INSERT QUERY NO: 128 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 4
);

/* INSERT QUERY NO: 129 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 4
);

/* INSERT QUERY NO: 130 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 4
);

/* INSERT QUERY NO: 131 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 4
);

/* INSERT QUERY NO: 132 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 4
);

/* INSERT QUERY NO: 133 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 4
);

/* INSERT QUERY NO: 134 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 4
);

/* INSERT QUERY NO: 135 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 4
);

/* INSERT QUERY NO: 136 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 4
);

/* INSERT QUERY NO: 137 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 4
);

/* INSERT QUERY NO: 138 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 4
);

/* INSERT QUERY NO: 139 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 4
);

/* INSERT QUERY NO: 140 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 4
);

/* INSERT QUERY NO: 141 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 4
);

/* INSERT QUERY NO: 142 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 4
);

/* INSERT QUERY NO: 143 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 4
);

/* INSERT QUERY NO: 144 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 4
);

/* INSERT QUERY NO: 145 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 4
);

/* INSERT QUERY NO: 146 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 4
);

/* INSERT QUERY NO: 147 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 4
);

/* INSERT QUERY NO: 148 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 4
);

/* INSERT QUERY NO: 149 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 4
);

/* INSERT QUERY NO: 150 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 4
);

/* INSERT QUERY NO: 151 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 4
);

/* INSERT QUERY NO: 152 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 4
);

/* INSERT QUERY NO: 153 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 5
);

/* INSERT QUERY NO: 154 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 5
);

/* INSERT QUERY NO: 155 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 5
);

/* INSERT QUERY NO: 156 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 5
);

/* INSERT QUERY NO: 157 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 5
);

/* INSERT QUERY NO: 158 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 5
);

/* INSERT QUERY NO: 159 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 5
);

/* INSERT QUERY NO: 160 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 5
);

/* INSERT QUERY NO: 161 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 5
);

/* INSERT QUERY NO: 162 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 5
);

/* INSERT QUERY NO: 163 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 5
);

/* INSERT QUERY NO: 164 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 5
);

/* INSERT QUERY NO: 165 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 5
);

/* INSERT QUERY NO: 166 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 5
);

/* INSERT QUERY NO: 167 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 5
);

/* INSERT QUERY NO: 168 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 5
);

/* INSERT QUERY NO: 169 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 5
);

/* INSERT QUERY NO: 170 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 5
);

/* INSERT QUERY NO: 171 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 5
);

/* INSERT QUERY NO: 172 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 5
);

/* INSERT QUERY NO: 173 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 5
);

/* INSERT QUERY NO: 174 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 5
);

/* INSERT QUERY NO: 175 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 5
);

/* INSERT QUERY NO: 176 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 5
);

/* INSERT QUERY NO: 177 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 5
);

/* INSERT QUERY NO: 178 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 5
);

/* INSERT QUERY NO: 179 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 5
);

/* INSERT QUERY NO: 180 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 5
);

/* INSERT QUERY NO: 181 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 5
);

/* INSERT QUERY NO: 182 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 5
);

/* INSERT QUERY NO: 183 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 5
);

/* INSERT QUERY NO: 184 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 5
);

/* INSERT QUERY NO: 185 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 5
);

/* INSERT QUERY NO: 186 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 5
);

/* INSERT QUERY NO: 187 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 5
);

/* INSERT QUERY NO: 188 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 5
);

/* INSERT QUERY NO: 189 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 5
);

/* INSERT QUERY NO: 190 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 5
);

/* INSERT QUERY NO: 191 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 6
);

/* INSERT QUERY NO: 192 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 6
);

/* INSERT QUERY NO: 193 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 6
);

/* INSERT QUERY NO: 194 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 6
);

/* INSERT QUERY NO: 195 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 6
);

/* INSERT QUERY NO: 196 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 6
);

/* INSERT QUERY NO: 197 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 6
);

/* INSERT QUERY NO: 198 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 6
);

/* INSERT QUERY NO: 199 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 6
);

/* INSERT QUERY NO: 200 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 6
);

/* INSERT QUERY NO: 201 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 6
);

/* INSERT QUERY NO: 202 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 6
);

/* INSERT QUERY NO: 203 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 6
);

/* INSERT QUERY NO: 204 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 6
);

/* INSERT QUERY NO: 205 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 6
);

/* INSERT QUERY NO: 206 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 6
);

/* INSERT QUERY NO: 207 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 6
);

/* INSERT QUERY NO: 208 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 6
);

/* INSERT QUERY NO: 209 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 6
);

/* INSERT QUERY NO: 210 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 6
);

/* INSERT QUERY NO: 211 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 6
);

/* INSERT QUERY NO: 212 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 6
);

/* INSERT QUERY NO: 213 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 6
);

/* INSERT QUERY NO: 214 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 6
);

/* INSERT QUERY NO: 215 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 6
);

/* INSERT QUERY NO: 216 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 6
);

/* INSERT QUERY NO: 217 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 6
);

/* INSERT QUERY NO: 218 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 6
);

/* INSERT QUERY NO: 219 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 6
);

/* INSERT QUERY NO: 220 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 6
);

/* INSERT QUERY NO: 221 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 6
);

/* INSERT QUERY NO: 222 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 6
);

/* INSERT QUERY NO: 223 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 6
);

/* INSERT QUERY NO: 224 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 6
);

/* INSERT QUERY NO: 225 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 6
);

/* INSERT QUERY NO: 226 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 6
);

/* INSERT QUERY NO: 227 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 6
);

/* INSERT QUERY NO: 228 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 6
);

/* INSERT QUERY NO: 229 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 7
);

/* INSERT QUERY NO: 230 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 7
);

/* INSERT QUERY NO: 231 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 7
);

/* INSERT QUERY NO: 232 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 7
);

/* INSERT QUERY NO: 233 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 7
);

/* INSERT QUERY NO: 234 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 7
);

/* INSERT QUERY NO: 235 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 7
);

/* INSERT QUERY NO: 236 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 7
);

/* INSERT QUERY NO: 237 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 7
);

/* INSERT QUERY NO: 238 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 7
);

/* INSERT QUERY NO: 239 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 7
);

/* INSERT QUERY NO: 240 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 7
);

/* INSERT QUERY NO: 241 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 7
);

/* INSERT QUERY NO: 242 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 7
);

/* INSERT QUERY NO: 243 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 7
);

/* INSERT QUERY NO: 244 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 7
);

/* INSERT QUERY NO: 245 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 7
);

/* INSERT QUERY NO: 246 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 7
);

/* INSERT QUERY NO: 247 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 7
);

/* INSERT QUERY NO: 248 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 7
);

/* INSERT QUERY NO: 249 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 7
);

/* INSERT QUERY NO: 250 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 7
);

/* INSERT QUERY NO: 251 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 7
);

/* INSERT QUERY NO: 252 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 7
);

/* INSERT QUERY NO: 253 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 7
);

/* INSERT QUERY NO: 254 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 7
);

/* INSERT QUERY NO: 255 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 7
);

/* INSERT QUERY NO: 256 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 7
);

/* INSERT QUERY NO: 257 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 7
);

/* INSERT QUERY NO: 258 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 7
);

/* INSERT QUERY NO: 259 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 7
);

/* INSERT QUERY NO: 260 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 7
);

/* INSERT QUERY NO: 261 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 7
);

/* INSERT QUERY NO: 262 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 7
);

/* INSERT QUERY NO: 263 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 7
);

/* INSERT QUERY NO: 264 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 7
);

/* INSERT QUERY NO: 265 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 7
);

/* INSERT QUERY NO: 266 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 7
);

/* INSERT QUERY NO: 267 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 8
);

/* INSERT QUERY NO: 268 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 8
);

/* INSERT QUERY NO: 269 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 8
);

/* INSERT QUERY NO: 270 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 8
);

/* INSERT QUERY NO: 271 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 8
);

/* INSERT QUERY NO: 272 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 8
);

/* INSERT QUERY NO: 273 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 8
);

/* INSERT QUERY NO: 274 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 8
);

/* INSERT QUERY NO: 275 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 8
);

/* INSERT QUERY NO: 276 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 8
);

/* INSERT QUERY NO: 277 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 8
);

/* INSERT QUERY NO: 278 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 8
);

/* INSERT QUERY NO: 279 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 8
);

/* INSERT QUERY NO: 280 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 8
);

/* INSERT QUERY NO: 281 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 8
);

/* INSERT QUERY NO: 282 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 8
);

/* INSERT QUERY NO: 283 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 8
);

/* INSERT QUERY NO: 284 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 8
);

/* INSERT QUERY NO: 285 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 8
);

/* INSERT QUERY NO: 286 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 8
);

/* INSERT QUERY NO: 287 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 8
);

/* INSERT QUERY NO: 288 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 8
);

/* INSERT QUERY NO: 289 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 8
);

/* INSERT QUERY NO: 290 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 8
);

/* INSERT QUERY NO: 291 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 8
);

/* INSERT QUERY NO: 292 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 8
);

/* INSERT QUERY NO: 293 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 8
);

/* INSERT QUERY NO: 294 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 8
);

/* INSERT QUERY NO: 295 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 8
);

/* INSERT QUERY NO: 296 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 8
);

/* INSERT QUERY NO: 297 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 8
);

/* INSERT QUERY NO: 298 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 8
);

/* INSERT QUERY NO: 299 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 8
);

/* INSERT QUERY NO: 300 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 8
);

/* INSERT QUERY NO: 301 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 8
);

/* INSERT QUERY NO: 302 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 8
);

/* INSERT QUERY NO: 303 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 8
);

/* INSERT QUERY NO: 304 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 8
);

/* INSERT QUERY NO: 305 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 9
);

/* INSERT QUERY NO: 306 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 9
);

/* INSERT QUERY NO: 307 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 9
);

/* INSERT QUERY NO: 308 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 9
);

/* INSERT QUERY NO: 309 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 9
);

/* INSERT QUERY NO: 310 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 9
);

/* INSERT QUERY NO: 311 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 9
);

/* INSERT QUERY NO: 312 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 9
);

/* INSERT QUERY NO: 313 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 9
);

/* INSERT QUERY NO: 314 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 9
);

/* INSERT QUERY NO: 315 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 9
);

/* INSERT QUERY NO: 316 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 9
);

/* INSERT QUERY NO: 317 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 9
);

/* INSERT QUERY NO: 318 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 9
);

/* INSERT QUERY NO: 319 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 9
);

/* INSERT QUERY NO: 320 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 9
);

/* INSERT QUERY NO: 321 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 9
);

/* INSERT QUERY NO: 322 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 9
);

/* INSERT QUERY NO: 323 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 9
);

/* INSERT QUERY NO: 324 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 9
);

/* INSERT QUERY NO: 325 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 9
);

/* INSERT QUERY NO: 326 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 9
);

/* INSERT QUERY NO: 327 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 9
);

/* INSERT QUERY NO: 328 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 9
);

/* INSERT QUERY NO: 329 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 9
);

/* INSERT QUERY NO: 330 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 9
);

/* INSERT QUERY NO: 331 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 9
);

/* INSERT QUERY NO: 332 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 9
);

/* INSERT QUERY NO: 333 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 9
);

/* INSERT QUERY NO: 334 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 9
);

/* INSERT QUERY NO: 335 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 9
);

/* INSERT QUERY NO: 336 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 9
);

/* INSERT QUERY NO: 337 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 9
);

/* INSERT QUERY NO: 338 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 9
);

/* INSERT QUERY NO: 339 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 9
);

/* INSERT QUERY NO: 340 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 9
);

/* INSERT QUERY NO: 341 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 9
);

/* INSERT QUERY NO: 342 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 9
);

/* INSERT QUERY NO: 343 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 10
);

/* INSERT QUERY NO: 344 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 10
);

/* INSERT QUERY NO: 345 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 10
);

/* INSERT QUERY NO: 346 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 10
);

/* INSERT QUERY NO: 347 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 10
);

/* INSERT QUERY NO: 348 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 10
);

/* INSERT QUERY NO: 349 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 10
);

/* INSERT QUERY NO: 350 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 10
);

/* INSERT QUERY NO: 351 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 10
);

/* INSERT QUERY NO: 352 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 10
);

/* INSERT QUERY NO: 353 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 10
);

/* INSERT QUERY NO: 354 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 10
);

/* INSERT QUERY NO: 355 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 10
);

/* INSERT QUERY NO: 356 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 10
);

/* INSERT QUERY NO: 357 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 10
);

/* INSERT QUERY NO: 358 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 10
);

/* INSERT QUERY NO: 359 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 10
);

/* INSERT QUERY NO: 360 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 10
);

/* INSERT QUERY NO: 361 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 10
);

/* INSERT QUERY NO: 362 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 10
);

/* INSERT QUERY NO: 363 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 10
);

/* INSERT QUERY NO: 364 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 10
);

/* INSERT QUERY NO: 365 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 10
);

/* INSERT QUERY NO: 366 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 10
);

/* INSERT QUERY NO: 367 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 10
);

/* INSERT QUERY NO: 368 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 10
);

/* INSERT QUERY NO: 369 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 10
);

/* INSERT QUERY NO: 370 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 10
);

/* INSERT QUERY NO: 371 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 10
);

/* INSERT QUERY NO: 372 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 10
);

/* INSERT QUERY NO: 373 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 10
);

/* INSERT QUERY NO: 374 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 10
);

/* INSERT QUERY NO: 375 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 10
);

/* INSERT QUERY NO: 376 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 10
);

/* INSERT QUERY NO: 377 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 10
);

/* INSERT QUERY NO: 378 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 10
);

/* INSERT QUERY NO: 379 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 10
);

/* INSERT QUERY NO: 380 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 10
);

/* INSERT QUERY NO: 381 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 11
);

/* INSERT QUERY NO: 382 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 11
);

/* INSERT QUERY NO: 383 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 11
);

/* INSERT QUERY NO: 384 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 11
);

/* INSERT QUERY NO: 385 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 11
);

/* INSERT QUERY NO: 386 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 11
);

/* INSERT QUERY NO: 387 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 11
);

/* INSERT QUERY NO: 388 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 11
);

/* INSERT QUERY NO: 389 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 11
);

/* INSERT QUERY NO: 390 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 11
);

/* INSERT QUERY NO: 391 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 11
);

/* INSERT QUERY NO: 392 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 11
);

/* INSERT QUERY NO: 393 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 11
);

/* INSERT QUERY NO: 394 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 11
);

/* INSERT QUERY NO: 395 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 11
);

/* INSERT QUERY NO: 396 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 11
);

/* INSERT QUERY NO: 397 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 11
);

/* INSERT QUERY NO: 398 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 11
);

/* INSERT QUERY NO: 399 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 11
);

/* INSERT QUERY NO: 400 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 11
);

/* INSERT QUERY NO: 401 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 11
);

/* INSERT QUERY NO: 402 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 11
);

/* INSERT QUERY NO: 403 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 11
);

/* INSERT QUERY NO: 404 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 11
);

/* INSERT QUERY NO: 405 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 11
);

/* INSERT QUERY NO: 406 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 11
);

/* INSERT QUERY NO: 407 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 11
);

/* INSERT QUERY NO: 408 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 11
);

/* INSERT QUERY NO: 409 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 11
);

/* INSERT QUERY NO: 410 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 11
);

/* INSERT QUERY NO: 411 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 11
);

/* INSERT QUERY NO: 412 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 11
);

/* INSERT QUERY NO: 413 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 11
);

/* INSERT QUERY NO: 414 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 11
);

/* INSERT QUERY NO: 415 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 11
);

/* INSERT QUERY NO: 416 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 11
);

/* INSERT QUERY NO: 417 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 11
);

/* INSERT QUERY NO: 418 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 11
);

/* INSERT QUERY NO: 419 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 12
);

/* INSERT QUERY NO: 420 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 12
);

/* INSERT QUERY NO: 421 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 12
);

/* INSERT QUERY NO: 422 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 12
);

/* INSERT QUERY NO: 423 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 12
);

/* INSERT QUERY NO: 424 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 12
);

/* INSERT QUERY NO: 425 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 12
);

/* INSERT QUERY NO: 426 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 12
);

/* INSERT QUERY NO: 427 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 12
);

/* INSERT QUERY NO: 428 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 12
);

/* INSERT QUERY NO: 429 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 12
);

/* INSERT QUERY NO: 430 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 12
);

/* INSERT QUERY NO: 431 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 12
);

/* INSERT QUERY NO: 432 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 12
);

/* INSERT QUERY NO: 433 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 12
);

/* INSERT QUERY NO: 434 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 12
);

/* INSERT QUERY NO: 435 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 12
);

/* INSERT QUERY NO: 436 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 12
);

/* INSERT QUERY NO: 437 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 12
);

/* INSERT QUERY NO: 438 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 12
);

/* INSERT QUERY NO: 439 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 12
);

/* INSERT QUERY NO: 440 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 12
);

/* INSERT QUERY NO: 441 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 12
);

/* INSERT QUERY NO: 442 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 12
);

/* INSERT QUERY NO: 443 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 12
);

/* INSERT QUERY NO: 444 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 12
);

/* INSERT QUERY NO: 445 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 12
);

/* INSERT QUERY NO: 446 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 12
);

/* INSERT QUERY NO: 447 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 12
);

/* INSERT QUERY NO: 448 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 12
);

/* INSERT QUERY NO: 449 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 12
);

/* INSERT QUERY NO: 450 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 12
);

/* INSERT QUERY NO: 451 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 12
);

/* INSERT QUERY NO: 452 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 12
);

/* INSERT QUERY NO: 453 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 12
);

/* INSERT QUERY NO: 454 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 12
);

/* INSERT QUERY NO: 455 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 12
);

/* INSERT QUERY NO: 456 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 12
);

/* INSERT QUERY NO: 457 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 13
);

/* INSERT QUERY NO: 458 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 13
);

/* INSERT QUERY NO: 459 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 13
);

/* INSERT QUERY NO: 460 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 13
);

/* INSERT QUERY NO: 461 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 13
);

/* INSERT QUERY NO: 462 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 13
);

/* INSERT QUERY NO: 463 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 13
);

/* INSERT QUERY NO: 464 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 13
);

/* INSERT QUERY NO: 465 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 13
);

/* INSERT QUERY NO: 466 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 13
);

/* INSERT QUERY NO: 467 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 13
);

/* INSERT QUERY NO: 468 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 13
);

/* INSERT QUERY NO: 469 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 13
);

/* INSERT QUERY NO: 470 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 13
);

/* INSERT QUERY NO: 471 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 13
);

/* INSERT QUERY NO: 472 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 13
);

/* INSERT QUERY NO: 473 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 13
);

/* INSERT QUERY NO: 474 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 13
);

/* INSERT QUERY NO: 475 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 13
);

/* INSERT QUERY NO: 476 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 13
);

/* INSERT QUERY NO: 477 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 13
);

/* INSERT QUERY NO: 478 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 13
);

/* INSERT QUERY NO: 479 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 13
);

/* INSERT QUERY NO: 480 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 13
);

/* INSERT QUERY NO: 481 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 13
);

/* INSERT QUERY NO: 482 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 13
);

/* INSERT QUERY NO: 483 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 13
);

/* INSERT QUERY NO: 484 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 13
);

/* INSERT QUERY NO: 485 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 13
);

/* INSERT QUERY NO: 486 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 13
);

/* INSERT QUERY NO: 487 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 13
);

/* INSERT QUERY NO: 488 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 13
);

/* INSERT QUERY NO: 489 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 13
);

/* INSERT QUERY NO: 490 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 13
);

/* INSERT QUERY NO: 491 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 13
);

/* INSERT QUERY NO: 492 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 13
);

/* INSERT QUERY NO: 493 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 13
);

/* INSERT QUERY NO: 494 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 13
);

/* INSERT QUERY NO: 495 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'EMPANADAS DE CARNE FRITA', 'con salsa picante', 300, 14
);

/* INSERT QUERY NO: 496 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'TORTILLA DE PAPA', 'con alioli', 300, 14
);

/* INSERT QUERY NO: 497 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATÉ AL OPORTO', 'con pan de especias', 440, 14
);

/* INSERT QUERY NO: 498 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BERENJENAS EN ESCABECHE', '.', 250, 14
);

/* INSERT QUERY NO: 499 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'ACEITUNAS MACERADAS', 'en oliva y piel de limón', 240, 14
);

/* INSERT QUERY NO: 500 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BURRATA ENTERA', '.', 760, 14
);

/* INSERT QUERY NO: 501 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON CRUDO', '.', 570, 14
);

/* INSERT QUERY NO: 502 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'JAMON HORNEADO', '.', 490, 14
);

/* INSERT QUERY NO: 503 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'BUÑUELOS DE ESPINACA', 'con mayonesa casera y salsa de tomate', 590, 14
);

/* INSERT QUERY NO: 504 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PROVOLETA DE BUFALA', 'con ensalada de rúcula tomate confitado y cebolla', 600, 14
);

/* INSERT QUERY NO: 505 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MATAMBRE ARROLLADO', 'con ensalada rusa y arrope de tomate', 920, 14
);

/* INSERT QUERY NO: 506 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CARPACCIO DE LOMO', 'con queso petit suisse y alcaparras fritas', 630, 14
);

/* INSERT QUERY NO: 507 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CAMARON', 'con palta salsa golf y tostada de pan brioche', 710, 14
);

/* INSERT QUERY NO: 508 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'OJO DE BIFE', 'con pak choi y papas dauphine', 1280, 14
);

/* INSERT QUERY NO: 509 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PLATO DETOX', 'risotto de quinoa con jengibre coliflor grillado pak choi y pasta de castañas', 790, 14
);

/* INSERT QUERY NO: 510 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PESCA DEL DIA', 'con vegetales glaseados y espuma de mantenca noisette', 1300, 14
);

/* INSERT QUERY NO: 511 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO', 'con spaghettini a la manteca y queso', 870, 14
);

/* INSERT QUERY NO: 512 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MILANESA DE LOMO NAPOLITANA', 'con mix de verdes y almendras tostadas', 920, 14
);

/* INSERT QUERY NO: 513 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'RAVIOLES DE BERENJENA', 'con salsa de tomate y albahaca', 810, 14
);

/* INSERT QUERY NO: 514 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'POLLO CRISPY', 'pollo frito apanado con cereal lechuga morada rúcula castañas aderezo de maní y huevo mollet', 460, 14
);

/* INSERT QUERY NO: 515 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'VERANIEGA', 'mix de verdes rabanitos palta mango fresco queso feta semillas de girasol cebolla morada y aderezo de mango', 950, 14
);

/* INSERT QUERY NO: 516 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUINOA', 'mix de verdes quinoa tomates cherrys granos de choclo aceitunas nueces tostadas aceite de albahaca y vinagreta de oliva y limón', 700, 14
);

/* INSERT QUERY NO: 517 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'MOUSSE DE CHOCOLATE Y CREMA FRESCA', '.', 500, 14
);

/* INSERT QUERY NO: 518 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PANNA COTTA CHOCOLATE BLANCO', 'y coulis de frutos rojos', 450, 14
);

/* INSERT QUERY NO: 519 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CREME BRULEE', '.', 380, 14
);

/* INSERT QUERY NO: 520 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'FLAN DE DULCE DE LECHE', 'con crema fresca', 310, 14
);

/* INSERT QUERY NO: 521 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'QUESO Y DULCE', 'batata y membrillo', 250, 14
);

/* INSERT QUERY NO: 522 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'COPA DE VINO', '.', 250, 14
);

/* INSERT QUERY NO: 523 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'AGUA CON / SIN GAS', 'Local', 100, 14
);

/* INSERT QUERY NO: 524 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GASEOSAS', '.', 150, 14
);

/* INSERT QUERY NO: 525 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'LIMONADA', 'con jengibre y menta', 200, 14
);

/* INSERT QUERY NO: 526 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'APEROL SPRITZ', '.', 350, 14
);

/* INSERT QUERY NO: 527 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'NEGRONI', '.', 400, 14
);

/* INSERT QUERY NO: 528 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'GIN TONIC', '.', 400, 14
);

/* INSERT QUERY NO: 529 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'CORONA', 'México', 300, 14
);

/* INSERT QUERY NO: 530 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'STELLA ARTOIS TIRADA', 'Argentina', 250, 14
);

/* INSERT QUERY NO: 531 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA AMBER LAGER', 'Argentina', 250, 14
);

/* INSERT QUERY NO: 532 */
INSERT INTO menu_items(name, description, price, restaurant_id)
VALUES
(
'PATAGONIA BOHEMIAN PILSENER', 'Argentina', 250, 14
);

