CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL
);

COMMENT ON TABLE users IS 'Таблица пользователей системы';
COMMENT ON COLUMN users.id IS 'Идентификатор пользователя, автоинкремент, первичный ключ';
COMMENT ON COLUMN users.login IS 'Логин пользователя, username или email';
COMMENT ON COLUMN users.password IS 'Зашифрованный пароль (BCrypt)';