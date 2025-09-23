CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id INTEGER NOT NULL,
    latitude DECIMAL(17,15) NOT NULL,
    longitude DECIMAL(18,15) NOT NULL,
    CONSTRAINT fk_locations_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_locations_user_id ON locations(user_id);

COMMENT ON TABLE locations IS 'Локации пользователей для отслеживания погоды';
COMMENT ON COLUMN locations.id IS 'Идентификатор локации, автоинкремент, первичный ключ';
COMMENT ON COLUMN locations.name IS 'Название локации';
COMMENT ON COLUMN locations.user_id IS 'Пользователь, добавивший эту локацию';
COMMENT ON COLUMN locations.latitude IS 'Широта локации';
COMMENT ON COLUMN locations.longitude IS 'Долгота локации';