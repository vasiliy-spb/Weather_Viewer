CREATE TABLE sessions (
    id UUID PRIMARY KEY,
    user_id INTEGER NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_sessions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_sessions_user_id ON sessions(user_id);
CREATE INDEX idx_sessions_expires_at ON sessions(expires_at);

COMMENT ON TABLE sessions IS 'Сессии пользователей';
COMMENT ON COLUMN sessions.id IS 'Идентификатор сессии, UUID, первичный ключ';
COMMENT ON COLUMN sessions.user_id IS 'Пользователь, для которого сессия создана';
COMMENT ON COLUMN sessions.expires_at IS 'Время истечения сессии';

