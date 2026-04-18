-- Add role column to users table
ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'MEMBER';

-- Update test data with roles
UPDATE users SET role = 'ADMIN' WHERE username = 'admin';
UPDATE users SET role = 'MEMBER' WHERE username IN ('user1', 'user2', 'user3');

