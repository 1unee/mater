-- Таблица и последовательность для photo
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