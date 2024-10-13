-- Создание последовательности для userSettings
CREATE SEQUENCE IF NOT EXISTS user_settings_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- Создание таблицы userSettings
CREATE TABLE user_settings (
    id INT8 NOT NULL DEFAULT nextval('user_settings_id_seq'),
    user_id INT8 NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_id
         FOREIGN KEY (user_id) REFERENCES "user" (id)
              ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Создание последовательности для config
CREATE SEQUENCE IF NOT EXISTS config_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- Создание таблицы config
CREATE TABLE config (
    id INT8 NOT NULL DEFAULT nextval('config_id_seq'),
    code INT4 UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    value VARCHAR(1024) NOT NULL,
    description VARCHAR(4096),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (id)
);

-- Создание последовательности для setting
CREATE SEQUENCE IF NOT EXISTS setting_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- Создание таблицы setting
CREATE TABLE setting (
    id INT8 NOT NULL DEFAULT nextval('setting_id_seq'),
    code INT4 UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    value VARCHAR(1024) NOT NULL,
    description VARCHAR(4096),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (id)
);

-- Создание последовательности для option
CREATE SEQUENCE IF NOT EXISTS option_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- Создание таблицы option
CREATE TABLE option (
    id INT8 NOT NULL DEFAULT nextval('option_id_seq'),
    root_id INT8 NOT NULL,
    code INT4 UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    value VARCHAR(1024) NOT NULL,
    description VARCHAR(4096),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (id),
    CONSTRAINT fk_root_id
        FOREIGN KEY (root_id) REFERENCES setting (id)
            ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Создание таблицы user_config_link
CREATE SEQUENCE IF NOT EXISTS user_config_link_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE user_config_link (
    id INT8 NOT NULL DEFAULT nextval('user_config_link_id_seq'),
    user_settings_id INT8 NOT NULL,
    config_id INT8 NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (id),
    CONSTRAINT fk_settings_id
        FOREIGN KEY (user_settings_id) REFERENCES user_settings (id)
            ON UPDATE NO ACTION ON DELETE CASCADE,
    CONSTRAINT fk_config_id
        FOREIGN KEY (config_id) REFERENCES config (id)
            ON UPDATE NO ACTION ON DELETE CASCADE
);

-- Создание таблицы user_setting_link
CREATE SEQUENCE IF NOT EXISTS user_setting_link_id_seq
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE user_setting_link (
    id INT8 NOT NULL DEFAULT nextval('user_setting_link_id_seq'),
    user_settings_id INT8 NOT NULL,
    setting_id INT8 NOT NULL,
    selected_option_id INT8 NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    PRIMARY KEY (id),
    CONSTRAINT fk_settings_id
        FOREIGN KEY (user_settings_id) REFERENCES user_settings (id)
            ON UPDATE NO ACTION ON DELETE CASCADE,
    CONSTRAINT fk_setting_id
        FOREIGN KEY (setting_id) REFERENCES setting (id)
            ON UPDATE NO ACTION ON DELETE CASCADE,
    CONSTRAINT fk_selected_option_id
        FOREIGN KEY (selected_option_id) REFERENCES option (id)
            ON UPDATE NO ACTION ON DELETE CASCADE
);

-- Заполнение таблицы config
INSERT INTO config (id, code, title, value, description, created_at, updated_at)
VALUES
    (nextval('config_id_seq'), 1,'Максимальный размер файла', '1000000', 'Максимальный размер одного файла при загрузке', NOW(), NULL),
    (nextval('config_id_seq'), 2, 'Время жизни уведомления', '5000', 'Время жизни сообщения в миллисекундах', NOW(), NULL);

-- Заполнение таблицы setting
INSERT INTO setting (id, code, title, value, description, created_at, updated_at)
VALUES
    (nextval('setting_id_seq'), 1, 'Тема', 'VALUE_ON_OPTIONS', 'Тема приложения', NOW(), NULL),
    (nextval('setting_id_seq'), 2, 'Фильтрация и сортировка', 'VALUE_ON_OPTIONS', 'Показывать кнопку фильтрации и сортировки в главном списке машин', NOW(), NULL),
    (nextval('setting_id_seq'), 3, 'Добавление машины', 'VALUE_ON_OPTIONS', 'Показывать кнопку добавления машин в главном списке', NOW(), NULL),
    (nextval('setting_id_seq'), 4, 'Тестовый режим фильтрации и сортировки', 'true', 'Показывать сообщение о тестовом режиме работы фильтрации и сортировки', NOW(), NULL),
    (nextval('setting_id_seq'), 5, 'Автоматическая смена фото', 'VALUE_ON_OPTIONS', 'Включить автовоспроизведение фотографий машины', NOW(), NULL),
    (nextval('setting_id_seq'), 6, 'Уведомления', 'VALUE_ON_OPTIONS', 'Тип уведомлений об изменениях избранных машин', NOW(), NULL);

-- Опции для настройки "Тема"
INSERT INTO option (id, root_id, code, title, value, description, created_at, updated_at)
VALUES
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 1), 1, 'Системная', 'default', 'Тема по умолчанию', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 1), 2, 'Темная', 'dark', 'Темная тема', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 1), 3, 'Светлая', 'light', 'Светлая тема', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 2), 4, 'Да', 'true', 'Включить фильтрацию и сортировку', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 2), 5, 'Нет', 'false', 'Отключить фильтрацию и сортировку', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 3), 6, 'Да', 'true', 'Показывать кнопку добавления машин', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 3), 7, 'Нет', 'false', 'Скрывать кнопку добавления машин', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 4), 8, 'Да', 'true', 'Включить тестовый режим фильтрации и сортировки', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 4), 9, 'Нет', 'false', 'Отключить тестовый режим фильтрации и сортировки', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 5), 10, 'Да', 'true', 'Включить автовоспроизведение изображений', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 5), 11,'Нет', 'false', 'Отключить автовоспроизведение изображений', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 6), 12, 'Нигде', 'NOTHING', 'Без уведомлений', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 6), 13, 'Почта', 'MAIL', 'Уведомления через почту', NOW(), NULL),
    (nextval('option_id_seq'), (SELECT id FROM setting WHERE code = 6), 14, 'Все', 'ALL', 'Уведомления через все каналы', NOW(), NULL);