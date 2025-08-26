-- CineFund Database Setup Script
-- Run this script after installing PostgreSQL

-- Connect to PostgreSQL as superuser and run these commands:
-- psql -U postgres -f setup-databases.sql

-- Create databases and users for CineFund application

-- Create user database
CREATE DATABASE user_db;
CREATE USER cinefund_user WITH PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE user_db TO cinefund_user;

-- Create movies database  
CREATE DATABASE movies;
GRANT ALL PRIVILEGES ON DATABASE movies TO cinefund_user;

-- Create investments database
CREATE DATABASE investments;
GRANT ALL PRIVILEGES ON DATABASE investments TO cinefund_user;

-- Connect to user_db and create tables
\c user_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'INVESTOR',
    wallet_balance DECIMAL(19,2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Display created databases
\l

-- Success message
\echo 'CineFund databases created successfully!'
\echo 'You can now start the Spring Boot services.'
