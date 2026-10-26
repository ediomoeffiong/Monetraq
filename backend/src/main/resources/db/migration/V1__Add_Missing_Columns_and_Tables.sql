-- Create users table if not exists
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    profile_photo_url VARCHAR(255),
    two_factor_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    account_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    last_password_change TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Note: The following lines are kept for backward compatibility if needed, 
-- but since this is V1 and failed on fresh DB, they are now redundant with the CREATE statement above for fresh installs.
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone_number VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_photo_url VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS two_factor_enabled BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS account_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE';
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_password_change TIMESTAMP;

-- Create user_settings table
CREATE TABLE IF NOT EXISTS user_settings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    theme VARCHAR(255) NOT NULL DEFAULT 'SYSTEM',
    language VARCHAR(255) NOT NULL DEFAULT 'en',
    default_currency VARCHAR(255) NOT NULL DEFAULT 'USD',
    updated_at TIMESTAMP
);

-- Create notification_preferences table
CREATE TABLE IF NOT EXISTS notification_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    email_notifications BOOLEAN NOT NULL DEFAULT TRUE,
    in_app_notifications BOOLEAN NOT NULL DEFAULT TRUE,
    transaction_alerts BOOLEAN NOT NULL DEFAULT TRUE,
    security_alerts BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at TIMESTAMP
);

-- Create security_sessions table
CREATE TABLE IF NOT EXISTS security_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    session_token VARCHAR(255) NOT NULL,
    ip_address VARCHAR(255),
    user_agent VARCHAR(500),
    created_at TIMESTAMP,
    last_accessed_at TIMESTAMP,
    expires_at TIMESTAMP NOT NULL
);

-- Create login_history table
CREATE TABLE IF NOT EXISTS login_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    ip_address VARCHAR(255),
    user_agent VARCHAR(500),
    login_time TIMESTAMP,
    success BOOLEAN NOT NULL,
    failure_reason VARCHAR(255)
);

-- Create transaction table if not exists
CREATE TABLE IF NOT EXISTS transaction (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    title VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    type VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    date TIMESTAMP NOT NULL,
    created_at TIMESTAMP,
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES users(id)
);
