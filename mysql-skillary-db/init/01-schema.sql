CREATE TABLE IF NOT EXISTS contents(
    content_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    `url` VARCHAR(255) NOT NULL,
    body TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS creators(
    creator_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    display_name VARCHAR(100) NOT NULL UNIQUE,
    `profile` TEXT,
    is_deleted BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
);

CREATE TABLE IF NOT EXISTS creator_settlements(
    creator_settlement_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    gross_amount INTEGER NOT NULL,
    platform_fee INTEGER NOT NULL,
    payout_amount INTEGER NOT NULL,
    settlement_status VARCHAR(20) NOT NULL DEFAULT 'CALCULATING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX creator_settlement_status_created (settlement_status, created_at DESC)
);

CREATE TABLE IF NOT EXISTS payments(
    payment_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    order_id VARCHAR(100) NOT NULL UNIQUE,
    amount INTEGER NOT NULL,
    credit_method VARCHAR(20) NOT NULL DEFAULT 'CARD',
    credit_status VARCHAR(20) NOT NULL DEFAULT 'READY',
    paid_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX payments_method_status_idx (credit_method, credit_status),
    INDEX payments_status_created_idx(credit_status, created_at DESC),
    INDEX payments_paid_idx (paid_at DESC)
);

CREATE TABLE IF NOT EXISTS posts(
    post_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX posts_category_created_idx (category, created_at DESC)
);

CREATE TABLE IF NOT EXISTS roles(
    role_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    `role` VARCHAR(50) NOT NULL UNIQUE,

    INDEX role_idx(role)
);

CREATE TABLE IF NOT EXISTS settlement_runs(
    run_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    run_status VARCHAR(20) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE,
    executed_at TIMESTAMP,

    INDEX idx_status (run_status),
    INDEX idx_period (period_start, period_end)
);

CREATE TABLE IF NOT EXISTS subscribes(
    subscribe_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    subscribe_status VARCHAR(20) NOT NULL DEFAULT 'INACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    start_at TIMESTAMP,
    end_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subscription_plans(
    plan_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    is_active BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)

CREATE TABLE IF NOT EXISTS users(
    user_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    nickname VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);