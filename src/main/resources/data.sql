INSERT INTO users (username, password, role, created_at)
SELECT 'admin', 'admin123', 'ADMIN', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password, role, created_at)
SELECT 'user1', 'pass123', 'USER', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user1');
