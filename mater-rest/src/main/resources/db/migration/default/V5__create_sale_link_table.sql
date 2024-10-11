-- Таблица и последовательность для user_favorite_car_link
CREATE SEQUENCE IF NOT EXISTS sale_link_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS sale_link (
    id INT8 NOT NULL DEFAULT nextval('sale_link_id_seq'),
    buyer_id INT8 NOT NULL,
    car_id int8 NOT NULL,
    created_at timestamptz(6) DEFAULT NOW(),
    status VARCHAR(100) NOT NULL,
    score DECIMAL(4, 3) DEFAULT 5,
    PRIMARY KEY (id),
    CONSTRAINT fk_buyer_id
        FOREIGN KEY (buyer_id)
            REFERENCES "user" (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_car_id
        FOREIGN KEY (car_id)
            REFERENCES car (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);