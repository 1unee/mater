-- Таблица и последовательность для seller
CREATE SEQUENCE IF NOT EXISTS seller_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS seller (
    id int8 NOT NULL DEFAULT nextval('seller_id_seq'),
    score float4,
    PRIMARY KEY (id)
);

-- Таблица и последовательность для car
CREATE SEQUENCE IF NOT EXISTS car_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS car (
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
    documents_color varchar(255), -- Цвет по документам
    gearbox varchar(255), -- Тип коробки передач
    state varchar(255), -- Состояние целостности
    engine_oil_type varchar(255), -- Тип топлива для двигателя
    transmission varchar(255), -- Привод
    steering_wheel varchar(255), -- Руль
    PRIMARY KEY (id),
    CONSTRAINT fk_from_car_to_seller_id
        FOREIGN KEY (seller_id)
            REFERENCES seller (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Таблица и последовательность для contact
CREATE SEQUENCE IF NOT EXISTS contact_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS contact (
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
CREATE SEQUENCE IF NOT EXISTS personal_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS personal (
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

-- Таблица и последовательность для role
CREATE SEQUENCE IF NOT EXISTS role_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS role (
    id int8 NOT NULL DEFAULT nextval('role_id_seq'),
    name varchar(255),
    PRIMARY KEY (id),
    CONSTRAINT check_role_name CHECK (name::text = ANY (ARRAY['USER', 'SELLER', 'SUPPORT', 'ADMIN']::text[]))
);

-- Таблица и последовательность для update
CREATE SEQUENCE IF NOT EXISTS update_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS update (
    id int8 NOT NULL DEFAULT nextval('update_id_seq'),
    value jsonb,
    PRIMARY KEY (id)
);

-- Таблица и последовательность для user
CREATE SEQUENCE IF NOT EXISTS user_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS "user" (
    id int8 NOT NULL DEFAULT nextval('user_id_seq'),
    email varchar(255),
    is_email_set boolean DEFAULT FALSE,
    username varchar(255),
    personal_id int8,
    seller_id int8,
    registered_at timestamptz(6),
    telegram_id int8,
    telegram_chat_id int8,
    PRIMARY KEY (id),
    CONSTRAINT fk_from_user_to_personal_id
        FOREIGN KEY (personal_id)
            REFERENCES personal (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_from_user_to_seller_id
        FOREIGN KEY (seller_id)
            REFERENCES seller (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE SEQUENCE IF NOT EXISTS user_role_link_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- Таблица для связи user и role
CREATE TABLE IF NOT EXISTS user_role_link (
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
--     CONSTRAINT unique_user_role_link UNIQUE (user_id, role_id)
);

-- Таблица и последовательность для log
CREATE SEQUENCE IF NOT EXISTS log_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS "log" (
   id int8 NOT NULL DEFAULT nextval('log_id_seq'),
   body text,
   threw_at timestamptz(6),
   PRIMARY KEY (id)
);

-- Таблица и последовательность для action
CREATE SEQUENCE IF NOT EXISTS action_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS action (
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

-- Таблица и последовательность для car_file
CREATE SEQUENCE IF NOT EXISTS car_file_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS car_file (
    id int8 NOT NULL DEFAULT nextval('car_file_id_seq'),
    car_id int8,
    name VARCHAR(2048),
    type VARCHAR(1024),
    size int8,
    url TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_from_car_file_to_car_id
        FOREIGN KEY (car_id)
            REFERENCES "car" (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Таблица и последовательность для exception
CREATE SEQUENCE IF NOT EXISTS exception_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS exception (
     id INT8 DEFAULT nextval('exception_id_seq'),
     exception_type VARCHAR(255) NOT NULL,
     message TEXT,
     root_cause TEXT,
     class_name VARCHAR(255),
     method_name VARCHAR(255),
     stack_trace TEXT,
     timestamp TIMESTAMP NOT NULL,
     request_url VARCHAR(500),
     PRIMARY KEY (id)
);
