-- Create client table if not exists
CREATE TABLE IF NOT EXISTS client (
    uid BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- Make sure we have indexes for performance
CREATE INDEX IF NOT EXISTS idx_client_username ON client (username); 