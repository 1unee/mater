DROP SEQUENCE IF EXISTS car_id_seq;
CREATE SEQUENCE car_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP SEQUENCE IF EXISTS contact_id_seq;
CREATE SEQUENCE contact_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP SEQUENCE IF EXISTS personal_id_seq;
CREATE SEQUENCE personal_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for photo_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS photo_id_seq;
CREATE SEQUENCE photo_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP SEQUENCE IF EXISTS role_id_seq;
CREATE SEQUENCE role_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;


DROP SEQUENCE IF EXISTS seller_id_seq;
CREATE SEQUENCE seller_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP SEQUENCE IF EXISTS update_id_seq;
CREATE SEQUENCE update_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;


DROP SEQUENCE IF EXISTS user_id_seq;
CREATE SEQUENCE user_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

DROP SEQUENCE IF EXISTS video_id_seq;
CREATE SEQUENCE video_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;


DROP TABLE IF EXISTS car;
CREATE TABLE car (
    id int8 NOT NULL,
    vehicle_identification_number varchar(255),
    brand varchar(255),
    mileage numeric(38,2),
    model varchar(255),
    owners_amount int4,
    power int4,
    price numeric(38,2),
    production_year int4,
    seller_id int8
);

DROP TABLE IF EXISTS contact;
CREATE TABLE contact (
    id int8 NOT NULL,
    method varchar(255),
    phone_number varchar(255),
    social_network_reference bytea,
    seller_id int8
);

DROP TABLE IF EXISTS personal;
CREATE TABLE personal (
    id int8 NOT NULL,
    first_name varchar(255),
    last_name varchar(255),
    middle_name varchar(255),
    birth_date date
);

DROP TABLE IF EXISTS photo;
CREATE TABLE photo (
    id int8 NOT NULL,
    bytes bytea,
    car_id int8
);

DROP TABLE IF EXISTS role;
CREATE TABLE role (
    id int8 NOT NULL,
    name varchar(255)
);

DROP TABLE IF EXISTS seller;
CREATE TABLE seller (
    id int8 NOT NULL,
    score float4,
    user_id int8
);

DROP TABLE IF EXISTS update;
CREATE TABLE update (
id int8 NOT NULL,
value jsonb
);

DROP TABLE IF EXISTS "user";
CREATE TABLE "user" (
    id int8 NOT NULL,
    email varchar(255),
    username varchar(255),
    personal_id int8,
    registered_at timestamptz(6),
    telegram_id int8
);

DROP TABLE IF EXISTS user_role_link;
CREATE TABLE user_role_link (
    user_id int8 NOT NULL,
    role_id int8 NOT NULL
);

DROP TABLE IF EXISTS video;
CREATE TABLE video (
    id int8 NOT NULL,
    bytes bytea,
    car_id int8
);


-- ----------------------------
-- Primary Key structure for table car
-- ----------------------------
ALTER TABLE car ADD CONSTRAINT car_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table contact
-- ----------------------------
ALTER TABLE contact ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


-- ----------------------------
-- Primary Key structure for table personal
-- ----------------------------
ALTER TABLE personal ADD CONSTRAINT personal_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table photo
-- ----------------------------
ALTER TABLE photo ADD CONSTRAINT photo_pkey PRIMARY KEY (id);

-- ----------------------------
-- Checks structure for table role
-- ----------------------------
ALTER TABLE role ADD CONSTRAINT role_name_check CHECK (name::text = ANY (ARRAY['USER'::character varying, 'SELLER'::character varying, 'SUPPORT'::character varying, 'ADMIN'::character varying]::text[]));

-- ----------------------------
-- Primary Key structure for table role
-- ----------------------------
ALTER TABLE role ADD CONSTRAINT role_pkey PRIMARY KEY (id);

-- ----------------------------
-- Uniques structure for table seller
-- ----------------------------
ALTER TABLE seller ADD CONSTRAINT uk_etfpl3vymasxfqc4ri4r3euf6 UNIQUE (user_id);

-- ----------------------------
-- Primary Key structure for table seller
-- ----------------------------
ALTER TABLE seller ADD CONSTRAINT seller_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table update
-- ----------------------------
ALTER TABLE update ADD CONSTRAINT update_pkey PRIMARY KEY (id);

-- ----------------------------
-- Uniques structure for table user
-- ----------------------------
ALTER TABLE "user" ADD CONSTRAINT uk_ksjc0ysuebtdk02c7huk1co0c UNIQUE (personal_id);

-- ----------------------------
-- Primary Key structure for table user
-- ----------------------------
ALTER TABLE "user" ADD CONSTRAINT user_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table video
-- ----------------------------
ALTER TABLE video ADD CONSTRAINT video_pkey PRIMARY KEY (id);

-- ----------------------------
-- Foreign Keys structure for table car
-- ----------------------------
ALTER TABLE car ADD CONSTRAINT fkp2mae9c14a8056x5jh8yt41ls FOREIGN KEY (seller_id) REFERENCES seller (id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table contact
-- ----------------------------
ALTER TABLE contact ADD CONSTRAINT fk33kph6ohc9oggyahti5qm1tk9 FOREIGN KEY (seller_id) REFERENCES seller (id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table photo
-- ----------------------------
ALTER TABLE photo ADD CONSTRAINT fka8cqhuyx228468h5sqxo5dht5 FOREIGN KEY (car_id) REFERENCES car (id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table seller
-- ----------------------------
ALTER TABLE seller ADD CONSTRAINT fk8vkr7vvgw6tq5hffsloqsbuq3 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table user
-- ----------------------------
ALTER TABLE "user" ADD CONSTRAINT fkm147muhe1965iygq7m5yy6y57 FOREIGN KEY (personal_id) REFERENCES personal (id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table user_role_link
-- ----------------------------
ALTER TABLE user_role_link ADD CONSTRAINT fkerxr4gwn974wcvaptugd5pnpv FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE user_role_link ADD CONSTRAINT fkmvh5bwwolpe4oh94gx9xsgse6 FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table video
-- ----------------------------
ALTER TABLE video ADD CONSTRAINT fkfh49bcj9jwkixp4lg68ehg9xt FOREIGN KEY (car_id) REFERENCES car (id) ON DELETE NO ACTION ON UPDATE NO ACTION;
