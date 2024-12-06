-- Tabela de Utilizador
CREATE TABLE IF NOT EXISTS users (
    pk_users INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    type ENUM('USER', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN DEFAULT FALSE,
    locked BOOLEAN DEFAULT FALSE,
    phone VARCHAR(255) UNIQUE
);

-- Tabela de Tokens de Redefinição de Senha
CREATE TABLE password_reset_tokens (
    pk_password_reset_tokens INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    fk_user_id INT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (fk_user_id) REFERENCES users(pk_users) ON DELETE CASCADE
);

-- Tabela de Carteiras Digitais
CREATE TABLE digital_wallets (
    pk_digital_wallets INT AUTO_INCREMENT PRIMARY KEY,
    wallet_name VARCHAR(255) NOT NULL,
    balance DECIMAL(15, 2) DEFAULT 0.0 NOT NULL,
    fk_users INT NOT NULL,
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE CASCADE
);

-- Tabela de Banco
CREATE TABLE IF NOT EXISTS banks (
    pk_banks INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    created_date DATE
);

-- Tabela de Conta Bancária
CREATE TABLE IF NOT EXISTS bank_accounts (
    pk_bank_accounts INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL UNIQUE,
    fk_users INT,
    fk_banks INT,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.0,
    FOREIGN KEY (fk_banks) REFERENCES banks(pk_banks) ON DELETE SET NULL
);

-- Tabela de Transações
CREATE TABLE IF NOT EXISTS transactions (
    pk_transactions INT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_users INT,
    fk_source_account INT,
    fk_destination_account INT,
    FOREIGN KEY (fk_source_account) REFERENCES bank_accounts(pk_bank_accounts) ON DELETE SET NULL,
    FOREIGN KEY (fk_destination_account) REFERENCES bank_accounts(pk_bank_accounts) ON DELETE SET NULL
);

-- Tabela de Histórico de Transações
CREATE TABLE transaction_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES transactions(pk_transactions) ON DELETE CASCADE
);

-- Tabela de Promoções
CREATE TABLE IF NOT EXISTS promotions (
    pk_promotions INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE
);

-- Tabela de Notificações
CREATE TABLE IF NOT EXISTS notifications (
    pk_notifications INT AUTO_INCREMENT PRIMARY KEY,
    message TEXT NOT NULL,
    fk_users INT,
    status ENUM('SENT', 'FAILED') DEFAULT 'SENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE CASCADE
);

-- Tabela de Regras do Sistema
CREATE TABLE IF NOT EXISTS system_rules (
    pk_system_rules INT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    admin INT
);

-- Tabela de Parceiros
CREATE TABLE IF NOT EXISTS partners (
    pk_partners INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- Tabela para Relacionar Utilizador com Parceiros
CREATE TABLE IF NOT EXISTS user_partners (
    fk_users INT NOT NULL,
    fk_partners INT NOT NULL,
    PRIMARY KEY (fk_users, fk_partners),
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE CASCADE,
    FOREIGN KEY (fk_partners) REFERENCES partners(pk_partners) ON DELETE CASCADE
);

-- Tabela para Relacionar Utilizador com Promoções
CREATE TABLE user_promotions (
    user_id INT,
    promotion_id INT,
    accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, promotion_id),
    FOREIGN KEY (user_id) REFERENCES users(pk_users) ON DELETE CASCADE,
    FOREIGN KEY (promotion_id) REFERENCES promotions(pk_promotions) ON DELETE CASCADE
);

-- Tabela para Relacionar Utilizador com Histórico de Transações
CREATE TABLE user_transaction_history (
    user_id INT,
    transaction_history_id INT,
    PRIMARY KEY (user_id, transaction_history_id),
    FOREIGN KEY (user_id) REFERENCES users(pk_users) ON DELETE CASCADE,
    FOREIGN KEY (transaction_history_id) REFERENCES transaction_history(id) ON DELETE CASCADE
);

-- Tabela para Relacionar Utilizador com Notificações
CREATE TABLE user_notifications (
    user_id INT,
    notification_id INT,
    PRIMARY KEY (user_id, notification_id),
    FOREIGN KEY (user_id) REFERENCES users(pk_users) ON DELETE CASCADE,
    FOREIGN KEY (notification_id) REFERENCES notifications(pk_notifications) ON DELETE CASCADE
);
