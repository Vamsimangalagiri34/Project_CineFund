-- Connect to investments database and create tables
\c investments;

-- Create investments table
CREATE TABLE IF NOT EXISTS investments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    producer_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'INR',
    transaction_id VARCHAR(255) UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    user_name VARCHAR(255),
    movie_title VARCHAR(255),
    producer_name VARCHAR(255),
    expected_return_percentage DECIMAL(5,2),
    actual_return_amount DECIMAL(19,2) DEFAULT 0.00,
    return_paid BOOLEAN DEFAULT FALSE,
    return_payment_date TIMESTAMP,
    investment_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    movie_id BIGINT,
    producer_id BIGINT,
    amount DECIMAL(19,2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    payment_gateway_response TEXT,
    failure_reason VARCHAR(500),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_investments_user_id ON investments(user_id);
CREATE INDEX IF NOT EXISTS idx_investments_movie_id ON investments(movie_id);
CREATE INDEX IF NOT EXISTS idx_investments_producer_id ON investments(producer_id);
CREATE INDEX IF NOT EXISTS idx_investments_transaction_id ON investments(transaction_id);
CREATE INDEX IF NOT EXISTS idx_investments_status ON investments(status);

CREATE INDEX IF NOT EXISTS idx_transactions_transaction_id ON transactions(transaction_id);
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions(status);

-- Insert some test data
INSERT INTO investments (
    user_id, movie_id, producer_id, amount, transaction_id, status,
    user_name, movie_title, producer_name, expected_return_percentage,
    investment_date, created_at, updated_at
) VALUES 
(101, 301, 201, 5000.00, 'TXN_ABC123DEF456', 'CONFIRMED',
 'John Doe', 'The Great Adventure', 'ABC Productions', 15.0,
 '2025-08-26 04:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(102, 301, 201, 3000.00, 'TXN_XYZ789GHI012', 'CONFIRMED',
 'Jane Smith', 'The Great Adventure', 'ABC Productions', 15.0,
 '2025-08-26 05:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(103, 302, 202, 7500.00, 'TXN_DEF456JKL789', 'PENDING',
 'Bob Johnson', 'Mystery Thriller', 'XYZ Studios', 12.0,
 '2025-08-26 06:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO transactions (
    transaction_id, user_id, movie_id, producer_id, amount, type, status,
    payment_method, description, created_at, completed_at
) VALUES 
('TXN_ABC123DEF456', 101, 301, 201, 5000.00, 'INVESTMENT', 'SUCCESS',
 'UPI', 'Investment in movie: The Great Adventure', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TXN_XYZ789GHI012', 102, 301, 201, 3000.00, 'INVESTMENT', 'SUCCESS',
 'CARD', 'Investment in movie: The Great Adventure', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TXN_DEF456JKL789', 103, 302, 202, 7500.00, 'INVESTMENT', 'PENDING',
 'UPI', 'Investment in movie: Mystery Thriller', CURRENT_TIMESTAMP, NULL);

-- Display created tables
\dt

-- Success message
\echo 'Investment service database tables created successfully with test data!'
