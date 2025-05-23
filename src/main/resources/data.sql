-- Insert test users into client table if not exists
INSERT INTO client (username, password, email, phone) 
SELECT 'testuser', 'password', 'test@example.com', '13800138000'
WHERE NOT EXISTS (SELECT 1 FROM client WHERE username = 'testuser');

-- Additional test user
INSERT INTO client (username, password, email, phone)
SELECT 'admin', 'admin123', 'admin@example.com', '13900139000'  
WHERE NOT EXISTS (SELECT 1 FROM client WHERE username = 'admin');

-- You can use these credentials to test the login functionality:
-- Username: testuser
-- Password: password
-- Or
-- Username: admin
-- Password: admin123 