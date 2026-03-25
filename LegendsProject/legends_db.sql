-- Drop existing database (optional reset)
DROP DATABASE IF EXISTS legends_db;

-- Create database
CREATE DATABASE legends_db;

-- Use database
USE legends_db;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    party_level INT DEFAULT 1,
    room_count INT DEFAULT 1
);

-- Optional: insert a test user
INSERT INTO users (username, password, party_level, room_count)
VALUES ('test', 'test123', 1, 1);
