-- Migration: Add account management and audit fields to users table
-- 2026 Senior Pattern: Atomic ALTER statements for cross-DB compatibility (H2/Postgres)

-- 1. Add columns individually to prevent syntax errors in H2
ALTER TABLE users ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

ALTER TABLE users ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 2. Data Migration: Sync updated_at with created_at for historical consistency
-- This ensures that @NotNull constraints are satisfied for old records
UPDATE users SET updated_at = created_at WHERE updated_at IS NULL;

-- 3. Performance: Indexing for scalability
-- Essential for administrative dashboards or filtering active/disabled users
CREATE INDEX idx_users_status ON users(status);

-- 4. Documentation (Senior Requirement)
COMMENT ON COLUMN users.status IS 'Lifecycle state of the user account (e.g., ACTIVE, DISABLED)';
COMMENT ON COLUMN users.updated_at IS 'Audit timestamp for the last modification of the record';

