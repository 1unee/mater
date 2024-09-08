-- Таблица и последовательность для seller
DROP SEQUENCE IF EXISTS seller_id_seq;
CREATE SEQUENCE seller_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS seller;
CREATE TABLE seller (
    id int8 NOT NULL DEFAULT nextval('seller_id_seq'),
    score float4,
    PRIMARY KEY (id)
);

-- Таблица и последовательность для car
DROP SEQUENCE IF EXISTS car_id_seq;
CREATE SEQUENCE car_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS car;
CREATE TABLE car (
    id int8 NOT NULL DEFAULT nextval('car_id_seq'),
    vehicle_identification_number varchar(255),
    brand varchar(255),
    mileage numeric(38,2),
    model varchar(255),
    owners_amount int4,
    power int4,
    price numeric(38,2),
    production_year int4,
    seller_id int8,
    PRIMARY KEY (id),
    CONSTRAINT fk_from_car_to_seller_id
        FOREIGN KEY (seller_id)
            REFERENCES seller (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Таблица и последовательность для contact
DROP SEQUENCE IF EXISTS contact_id_seq;
CREATE SEQUENCE contact_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS contact;
CREATE TABLE contact (
    id int8 NOT NULL DEFAULT nextval('contact_id_seq'),
    type varchar(255),
    value varchar(1024),
    seller_id int8,
    PRIMARY KEY (id),
    CONSTRAINT fk_from_contact_to_seller_id
        FOREIGN KEY (seller_id)
            REFERENCES seller (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Таблица и последовательность для personal
DROP SEQUENCE IF EXISTS personal_id_seq;
CREATE SEQUENCE personal_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS personal;
CREATE TABLE personal (
    id int8 NOT NULL DEFAULT nextval('personal_id_seq'),
    first_name varchar(255),
    is_first_name_set boolean DEFAULT FALSE,
    last_name varchar(255),
    is_last_name_set boolean DEFAULT FALSE,
    middle_name varchar(255),
    is_middle_name_set boolean DEFAULT FALSE,
    birth_date date,
    is_birth_date_set boolean DEFAULT FALSE,
    PRIMARY KEY (id)
);

-- Таблица и последовательность для photo
DROP SEQUENCE IF EXISTS photo_id_seq;
CREATE SEQUENCE photo_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS photo;
CREATE TABLE photo (
    id int8 NOT NULL DEFAULT nextval('photo_id_seq'),
    name varchar(1024),
    type varchar(128),
    base64 text,
    car_id int8,
    PRIMARY KEY (id),
    CONSTRAINT fk_from_photo_to_car_id
        FOREIGN KEY (car_id)
            REFERENCES car (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Таблица и последовательность для role
DROP SEQUENCE IF EXISTS role_id_seq;
CREATE SEQUENCE role_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS role;
CREATE TABLE role (
    id int8 NOT NULL DEFAULT nextval('role_id_seq'),
    name varchar(255),
    PRIMARY KEY (id),
    CONSTRAINT check_role_name CHECK (name::text = ANY (ARRAY['USER', 'SELLER', 'SUPPORT', 'ADMIN']::text[]))
);

-- Таблица и последовательность для update
DROP SEQUENCE IF EXISTS update_id_seq;
CREATE SEQUENCE update_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS update;
CREATE TABLE update (
    id int8 NOT NULL DEFAULT nextval('update_id_seq'),
    value jsonb,
    PRIMARY KEY (id)
);

-- Таблица и последовательность для user
DROP SEQUENCE IF EXISTS user_id_seq;
CREATE SEQUENCE user_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS "user";
CREATE TABLE "user" (
    id int8 NOT NULL DEFAULT nextval('user_id_seq'),
    email varchar(255),
    is_email_set boolean DEFAULT FALSE,
    username varchar(255),
    personal_id int8,
    seller_id int8,
    registered_at timestamptz(6),
    telegram_id int8,
    PRIMARY KEY (id),
    CONSTRAINT fk_from_user_to_personal_id
        FOREIGN KEY (personal_id)
            REFERENCES personal (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_from_user_to_seller_id
        FOREIGN KEY (seller_id)
            REFERENCES seller (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP SEQUENCE IF EXISTS user_role_link_id_seq;
CREATE SEQUENCE user_role_link_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- Таблица для связи user и role
DROP TABLE IF EXISTS user_role_link;
CREATE TABLE user_role_link (
    id int8 NOT NULL DEFAULT nextval('user_role_link_id_seq'),
    user_id int8 NOT NULL,
    role_id int8 NOT NULL,
    created_at timestamptz(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_from_user_role_link_to_user_id
        FOREIGN KEY (user_id)
            REFERENCES "user" (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_from_user_role_link_to_role_id
        FOREIGN KEY (role_id)
            REFERENCES role (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Таблица и последовательность для video
DROP SEQUENCE IF EXISTS video_id_seq;
CREATE SEQUENCE video_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS video;
CREATE TABLE video (
    id int8 NOT NULL DEFAULT nextval('video_id_seq'),
    name varchar(1024),
    type varchar(128),
    base64 text,
    car_id int8,
    PRIMARY KEY (id),
    CONSTRAINT fk_from_video_to_video_id
        FOREIGN KEY (car_id)
            REFERENCES car (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Таблица и последовательность для log
DROP SEQUENCE IF EXISTS log_id_seq;
CREATE SEQUENCE log_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS "log";
CREATE TABLE "log" (
   id int8 NOT NULL DEFAULT nextval('log_id_seq'),
   body text,
   threw_at timestamptz(6),
   PRIMARY KEY (id)
);

-- Таблица и последовательность для action
DROP SEQUENCE IF EXISTS action_id_seq;
CREATE SEQUENCE action_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP TABLE IF EXISTS action;
CREATE TABLE action (
    id int8 NOT NULL DEFAULT nextval('action_id_seq'),
    user_id int8,
    body text,
    type varchar(256),
    timestamp timestamptz(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_from_action_to_user_id
        FOREIGN KEY (user_id)
            REFERENCES "user" (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
