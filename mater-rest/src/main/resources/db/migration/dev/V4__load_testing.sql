DO $$
    DECLARE
        -- rows: 1_000, photo_len: 10_000, video_len: 300_000 --- 18 SECONDS
        -- rows: 10_000, photo_len: 10_000, video_len: 300_000 --- 225 SECONDS
        rows_amount INTEGER := 1000;
        role_names TEXT[] := array['USER', 'SELLER', 'SUPPORT', 'ADMIN'];
        contact_types TEXT[] := array['EMAIL', 'VKONTAKTE', 'TELEGRAM', 'INSTAGRAM', 'PHONE', 'WHATSAPP'];
        photo_base_64_length int4 := 10000;
        video_base_64_length int4 := 300000;
    BEGIN
        -- Insert data into "personal"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.personal (first_name,
                                            is_first_name_set,
                                            last_name,
                                            is_last_name_set,
                                            middle_name,
                                            is_middle_name_set,
                                            birth_date,
                                            is_birth_date_set)
                VALUES (
                           'FirstName ' || i,
                           true,
                           'LastName ' || i,
                           true,
                           'MiddleName ' || i,
                           true,
                           '1970-01-01'::date + (random() * 36525)::int,
                           true
                       );
            END LOOP;

        -- Insert data into "seller"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.seller (score)
                VALUES (random() * 5);
            END LOOP;

        -- Insert data into "contact"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.contact (type, value, seller_id)
                VALUES (
                           contact_types[floor(random() * array_length(contact_types, 1)) + 1],
                           'Contact ' || i,
                           (SELECT id FROM mater.seller ORDER BY random() LIMIT 1)
                       );
            END LOOP;

        -- Insert data into "car"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.car (vehicle_identification_number,
                                       brand,
                                       mileage,
                                       model,
                                       owners_amount,
                                       power,
                                       price,
                                       production_year,
                                       seller_id)
                VALUES (
                           substr(md5(random()::varchar), 0, 18),
                           'Brand ' || i,
                           random() * 100000,
                           'Model ' || i,
                           floor(random() * 15 + 1),
                           floor(random() * 400 + 50),
                           random() * 100000 + 1000,
                           1980 + floor(random() * 40),
                           (SELECT id FROM mater.seller ORDER BY random() LIMIT 1)
                       );
            END LOOP;

        -- Insert data into "photo"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.photo (name, type, base64, car_id)
                VALUES (
                           'PhotoName ' || i,
                           'image/jpeg',
                           md5(REPEAT(random()::varchar, photo_base_64_length)::text),
                           (SELECT id FROM mater.car ORDER BY random() LIMIT 1)
                       );
            END LOOP;

        -- Insert data into "video"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.video (name, type, base64, car_id)
                VALUES (
                           'VideoName ' || i,
                           'video/mp4',
                           md5(REPEAT(random()::varchar, video_base_64_length)::text),
                           (SELECT id FROM mater.car ORDER BY random() LIMIT 1)
                       );
            END LOOP;

        -- Insert data into "user"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.user (email, is_email_set, username, personal_id, seller_id, registered_at, telegram_id)
                VALUES (
                           'user' || i || '@example.com',
                           true,
                           'username_' || i,
                           (SELECT id FROM mater.personal ORDER BY random() LIMIT 1),
                           (SELECT id FROM mater.seller ORDER BY random() LIMIT 1),
                           NOW(),
                           floor(random() * 1000000000)
                       );
            END LOOP;

        -- Insert data into "user_role_link"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.user_role_link (user_id, role_id, created_at)
                VALUES (
                           (SELECT id FROM mater.user ORDER BY random() LIMIT 1),
                           (SELECT id FROM mater.role ORDER BY random() LIMIT 1),
                           NOW()
                       );
            END LOOP;

        -- Insert data into "log"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.log (body, threw_at)
                VALUES (
                           'Log message ' || i,
                           NOW()
                       );
            END LOOP;

        -- Insert data into "update"
        FOR i IN 1..rows_amount LOOP
                INSERT INTO mater.update (value)
                VALUES (
                           jsonb_build_object('field', 'value ' || i)
                       );
            END LOOP;
    END $$;
