USE skillarydb;

CREATE TABLE IF NOT EXISTS settlement_runs(
    run_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    run_status VARCHAR(20) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE,
    executed_at TIMESTAMP,

    INDEX idx_status (run_status),
    INDEX idx_period (period_start, period_end)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS subscription_plans (
    plan_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    is_active BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS users (
    user_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    nickname VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS roles (
    role_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    `role` VARCHAR(50) NOT NULL UNIQUE,

    INDEX role_idx(`role`)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS payments (
    payment_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    payment_key VARCHAR(255) NOT NULL UNIQUE,
    credit INTEGER NOT NULL,
    credit_method VARCHAR(20) NOT NULL DEFAULT 'CARD',
    credit_status VARCHAR(20) NOT NULL DEFAULT 'READY',
    paid_at TIMESTAMP DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    user_id TINYINT NOT NULL,

    CONSTRAINT fk_payments_user_id
        FOREIGN KEY (user_id)
        REFERENCES users (user_id),

    INDEX payments_method_status_idx (credit_method, credit_status),
    INDEX payments_status_created_idx (credit_status, created_at DESC),
    INDEX payments_paid_idx (paid_at DESC)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS creators (
    creator_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    display_name VARCHAR(100) NOT NULL UNIQUE,
    `profile` TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    user_id TINYINT NOT NULL,
    
    CONSTRAINT fk_creators_user_id
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS posts (
    post_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    `url` VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,

    creator_id TINYINT NOT NULL,

    CONSTRAINT fk_posts_creator_id
        FOREIGN KEY (creator_id)
        REFERENCES creators (creator_id)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS subscribes(
    subscribe_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    subscribe_status VARCHAR(20) NOT NULL DEFAULT 'INACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    start_at TIMESTAMP,
    end_at TIMESTAMP,

    subscription_plan_id TINYINT NOT NULL,

    CONSTRAINT fk_subscribes_plan_id
        FOREIGN KEY (subscription_plan_id)
        REFERENCES subscription_plans (plan_id)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS contents (
    content_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    category VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    plan_id TINYINT DEFAULT NULL,
    post_id TINYINT NOT NULL,

    CONSTRAINT fk_contents_post_id
        FOREIGN KEY (post_id)
        REFERENCES posts (post_id),

    CONSTRAINT fk_contents_plan_id
        FOREIGN KEY (plan_id)
        REFERENCES subscription_plans (plan_id),
    
    INDEX posts_category_created_idx (category, created_at DESC)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS user_role (
    role_id TINYINT NOT NULL,
    user_id TINYINT NOT NULL,

    CONSTRAINT fk_role_id
        FOREIGN KEY (role_id)
        REFERENCES roles (role_id),

    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS creator_settlements(
    creator_settlement_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    gross_amount INTEGER NOT NULL,
    platform_fee INTEGER NOT NULL,
    payout_amount INTEGER NOT NULL,
    settlement_status VARCHAR(20) NOT NULL DEFAULT 'CALCULATING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    creator_id TINYINT NOT NULL,
    settlement_run_id TINYINT,

    CONSTRAINT fk_creator_settlements_creator_id
        FOREIGN KEY (creator_id)
        REFERENCES creators (creator_id),

    CONSTRAINT fk_creator_settlements_settlement_run_id
        FOREIGN KEY (settlement_run_id)
        REFERENCES settlement_runs (run_id),

    INDEX creator_settlement_status_created (settlement_status, created_at DESC)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS comments (
    comment_id TINYINT NOT NULL AUTO_INCREMENT,
    parent_comment_id TINYINT DEFAULT NULL,
    `like` INTEGER DEFAULT 0,
    post_id TINYINT NOT NULL,
    user_id TINYINT,

    CONSTRAINT fk_comment_parent
        FOREIGN KEY (parent_comment_id)
        REFERENCES comments (comment_id)
        ON DELETE CASCADE,
    
    CONSTRAINT fk_comment_post
        FOREIGN KEY (post_id)
        REFERENCES posts (post_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;



CREATE TABLE IF NOT EXISTS orders (
    order_id TINYINT PRIMARY KEY AUTO_INCREMENT,
    amount INTEGER NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expired_at TIMESTAMP NOT NULL,

    user_id TINYINT NOT NULL,
    subscription_plan_id TINYINT,
    content_id TINYINT,
    payment_id TINYINT,

    CONSTRAINT fk_orders_user_id
        FOREIGN KEY (user_id)
        REFERENCES users (user_id),

    CONSTRAINT fk_orders_subscription_plan_id
        FOREIGN KEY (subscription_plan_id)
        REFERENCES subscription_plans (plan_id),
    
    CONSTRAINT fk_orders_content_id
        FOREIGN KEY (content_id)
        REFERENCES contents (content_id),

    CONSTRAINT fk_orders_payment_id
        FOREIGN KEY (payment_id)
        REFERENCES payments (payment_id)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET=utf8mb4 
COLLATE=utf8mb4_unicode_ci;