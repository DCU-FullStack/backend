INSERT INTO users (username, password, email, phone_number, name, role, created_at, updated_at)
VALUES (
    'user',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',
    'user@example.com',
    '010-1234-5678',
    'Default User',
    'USER',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON DUPLICATE KEY UPDATE username = username; 