INSERT INTO role (id, name)
VALUES (NEXTVAL('role_id_seq'), 'USER'),
       (NEXTVAL('role_id_seq'), 'SELLER'),
       (NEXTVAL('role_id_seq'), 'SUPPORT'),
       (NEXTVAL('role_id_seq'), 'ADMIN');

-- Вставка данных в таблицу PersonalEntity
INSERT INTO personal (id, middle_name, birth_date, first_name, last_name)
VALUES
    (NEXTVAL('personal_id_seq'), 'Michael', '1985-08-15', 'John', 'Doe'),
    (NEXTVAL('personal_id_seq'), NULL, '1990-02-25', 'Jane', 'Smith');

-- Вставка данных в таблицу UserEntity
INSERT INTO "user" (id, registered_at, username, personal_id, telegram_id, email)
VALUES
    (NEXTVAL('user_id_seq'), NOW(), 'john_doe', (SELECT CURRVAL('personal_id_seq') - 1), 1234567890, 'john.doe@example.com'),
    (NEXTVAL('user_id_seq'), NOW(), 'jane_smith', (SELECT CURRVAL('personal_id_seq')), 987654321, 'jane.smith@example.com');

-- Вставка ролей для пользователей
INSERT INTO user_role_link (user_id, role_id)
VALUES
    ((SELECT id FROM "user" WHERE username = 'john_doe' LIMIT 1), (SELECT id FROM role WHERE name = 'SELLER' LIMIT 1)),
    ((SELECT id FROM "user" WHERE username = 'jane_smith' LIMIT 1), (SELECT id FROM role WHERE name = 'USER' LIMIT 1)),
    ((SELECT id FROM "user" WHERE username = 'john_doe' LIMIT 1), (SELECT id FROM role WHERE name = 'ADMIN' LIMIT 1));

-- Вставка данных в таблицу SellerEntity
INSERT INTO seller (id, user_id, score)
VALUES
    (NEXTVAL('seller_id_seq'), (SELECT id FROM "user" WHERE username = 'john_doe'), 4.5);

-- Вставка данных в таблицу ContactEntity
INSERT INTO contact (id, social_network_reference, seller_id, phone_number, method)
VALUES
    (NEXTVAL('contact_id_seq'), 'https://facebook.com/john_doe', (SELECT id FROM seller WHERE user_id = (SELECT id FROM "user" WHERE username = 'john_doe')), '555-1234', 'PHONE'),
    (NEXTVAL('contact_id_seq'), 'https://twitter.com/jane_smith', (SELECT id FROM seller WHERE user_id = (SELECT id FROM "user" WHERE username = 'jane_smith')), '555-5678', 'EMAIL');

-- Вставка данных в таблицу CarEntity
INSERT INTO car (id, model, brand, owners_amount, mileage, production_year, vehicle_identification_number, seller_id, price, power)
VALUES
    (NEXTVAL('car_id_seq'), 'Model S', 'Tesla', 1, 20000, '2020', '5YJSA1E26JF252370', (SELECT id FROM seller WHERE user_id = (SELECT id FROM "user" WHERE username = 'john_doe')), 75000.00, 762),
    (NEXTVAL('car_id_seq'), 'Corolla', 'Toyota', 2, 50000, '2015', 'JTDBR32E420056742', (SELECT id FROM seller WHERE user_id = (SELECT id FROM "user" WHERE username = 'john_doe')), 15000.00, 132);

-- Вставка данных в таблицу PhotoEntity
INSERT INTO photo (id, car_id, bytes)
VALUES
    (NEXTVAL('photo_id_seq'), (SELECT id FROM car WHERE vehicle_identification_number = '5YJSA1E26JF252370'), decode('FFD8FFE0', 'hex')),  -- Пример фото в формате base64
    (NEXTVAL('photo_id_seq'), (SELECT id FROM car WHERE vehicle_identification_number = 'JTDBR32E420056742'), decode('FFD8FFE1', 'hex'));

-- Вставка данных в таблицу VideoEntity
INSERT INTO video (id, car_id, bytes)
VALUES
    (NEXTVAL('video_id_seq'), (SELECT id FROM car WHERE vehicle_identification_number = '5YJSA1E26JF252370'), decode('00000018', 'hex')),  -- Пример видео в формате base64
    (NEXTVAL('video_id_seq'), (SELECT id FROM car WHERE vehicle_identification_number = 'JTDBR32E420056742'), decode('00000019', 'hex'));