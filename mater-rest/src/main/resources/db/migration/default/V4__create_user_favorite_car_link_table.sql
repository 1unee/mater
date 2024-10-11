-- Таблица и последовательность для user_favorite_car_link
CREATE SEQUENCE IF NOT EXISTS user_favorite_car_link_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS user_favorite_car_link (
    id INT8 NOT NULL DEFAULT nextval('user_favorite_car_link_id_seq'),
    user_id INT8 NOT NULL,
    car_id INT8 NOT NULL,
    created_at timestamptz(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES "user" (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_car_id
        FOREIGN KEY (car_id)
            REFERENCES car (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);