-- Таблица и последовательность для user_token
CREATE SEQUENCE IF NOT EXISTS user_token_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS user_token (
    id INT8 NOT NULL DEFAULT nextval('user_token_id_seq'),
    user_id INT8 NOT NULL,
    value INT4 UNIQUE NOT NULL,
    created_at TIMESTAMPTZ(6) DEFAULT NOW(),
    updated_at TIMESTAMPTZ(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES "user" (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);