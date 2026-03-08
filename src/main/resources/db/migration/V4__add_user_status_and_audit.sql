-- Migration: Add account management and audit fields to users table
-- We use VARCHAR for the enum to keep it readable in SQL tools
ALTER TABLE users
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Best practice: Ensure existing users have an updatedAt equal to their createdAt
UPDATE users SET updated_at = created_at;

-- Indexing the status for faster filtering if the platform scales
CREATE INDEX idx_users_status ON users(status);
