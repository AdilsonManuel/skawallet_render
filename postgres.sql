skawallet_db

CREATE TABLE users (
    pk_users SERIAL PRIMARY KEY,
    name CHARACTER VARYING NOT NULL,
    email CHARACTER VARYING UNIQUE NOT NULL,
    phone CHARACTER VARYING UNIQUE NOT NULL,
    password CHARACTER VARYING NOT NULL,
    type VARCHAR(50) CHECK (type IN ('USER', 'ADMIN')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN DEFAULT FALSE,
    locked BOOLEAN DEFAULT FALSE
);

CREATE TABLE banks (
    pk_banks SERIAL PRIMARY KEY,
    name CHARACTER VARYING NOT NULL,
    code CHARACTER VARYING UNIQUE NOT NULL,
    created_date TIMESTAMP DEFAULT NULL
);


-- Criação da tabela transactions
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY, -- Chave primária auto-incrementada
    amount NUMERIC(15, 2) NOT NULL, -- Valor da transação
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação
    fk_destination_account BIGINT NOT NULL, -- Chave estrangeira para a conta de destino
    fk_source_account BIGINT NOT NULL, -- Chave estrangeira para a conta de origem
    status transaction_status NOT NULL, -- Status da transação
    transaction_type  CHARACTER VARYING NOT NULL, -- Tipo de transação
    CONSTRAINT fk_destination_account FOREIGN KEY (fk_destination_account) REFERENCES bank_accounts(id),
    CONSTRAINT fk_source_account FOREIGN KEY (fk_source_account) REFERENCES bank_accounts(id)
);

CREATE TABLE user_tokens (
    pk_user_tokens SERIAL PRIMARY KEY,
    fk_users INT NOT NULL,
    access_token TEXT NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE CASCADE
);


-- Criando o tipo ENUM para transaction_type
CREATE TYPE transaction_type AS ENUM ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER');

-- Criando o tipo ENUM para status
CREATE TYPE transaction_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED');

-- Criando a tabela transactions
CREATE TABLE transactions (
    pk_transactions SERIAL PRIMARY KEY,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type transaction_type NOT NULL,  -- Usando o tipo ENUM criado
    status transaction_status NOT NULL,          -- Usando o tipo ENUM criado
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fk_users INT,
    fk_source_account INT,
    fk_destination_account INT,
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE SET NULL,
    FOREIGN KEY (fk_source_account) REFERENCES bank_accounts(pk_bank_accounts) ON DELETE SET NULL,
    FOREIGN KEY (fk_destination_account) REFERENCES bank_accounts(pk_bank_accounts) ON DELETE SET NULL
);


-- Criando o tipo ENUM para event_type
CREATE TYPE event_type AS ENUM ('CREATED', 'UPDATED', 'DELETED');

-- Criando a tabela transaction_history
CREATE TABLE transaction_history (
    pk_transaction_history SERIAL PRIMARY KEY,
    fk_transactions INT,
    event_type event_type NOT NULL,  -- Usando o tipo ENUM criado
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_transactions) REFERENCES transactions(pk_transactions) ON DELETE CASCADE
);


-- Criando o tipo ENUM para status
CREATE TYPE status_type AS ENUM ('SENT', 'READ');

-- Criando a tabela notifications
CREATE TABLE notifications (
    pk_notifications SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    fk_users INT,
    status status_type DEFAULT 'SENT',  -- Usando o tipo ENUM criado
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE CASCADE
);


CREATE TABLE promotions (
    pk_promotions SERIAL PRIMARY KEY,
    name CHARACTER VARYING NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE
);

CREATE TABLE system_rules (
    pk_system_rules SERIAL PRIMARY KEY,
    rule_name CHARACTER VARYING NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    admin INT,
    FOREIGN KEY (admin) REFERENCES users(pk_users) ON DELETE SET NULL
);

CREATE TABLE partners (
    pk_partners SERIAL PRIMARY KEY,
    name CHARACTER VARYING NOT NULL,
    description TEXT
);

CREATE TABLE user_partners (
    fk_users INT,
    fk_partners INT,
    PRIMARY KEY (fk_users, fk_partners),
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE CASCADE,
    FOREIGN KEY (fk_partners) REFERENCES partners(pk_partners) ON DELETE CASCADE
);

CREATE TABLE user_promotions (
    fk_users INT,
    fk_promotions INT,
    accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (fk_users, fk_promotions),
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE CASCADE,
    FOREIGN KEY (fk_promotions) REFERENCES promotions(pk_promotions) ON DELETE CASCADE
);

CREATE TABLE user_notifications (
    fk_users INT,
    fk_notifications INT,
    PRIMARY KEY (fk_users, fk_notifications),
    FOREIGN KEY (fk_users) REFERENCES users(pk_users) ON DELETE CASCADE,
    FOREIGN KEY (fk_notifications) REFERENCES notifications(pk_notifications) ON DELETE CASCADE
);