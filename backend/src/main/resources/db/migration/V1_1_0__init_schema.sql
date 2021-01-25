CREATE TYPE Gender as enum('male', 'female');

CREATE TABLE IF NOT EXISTS Member (
    id uuid DEFAULT uuid_generate_v4(),
    mobile_number VARCHAR (13) UNIQUE NOT NULL,
    email VARCHAR (256) UNIQUE NOT NULL,
    password CHAR (60) NOT NULL,
    first_name VARCHAR (50) NOT NULL,
    last_name VARCHAR (50) NOT NULL,
    gender Gender NOT NULL,
    date_of_birth DATE NOT NULL,
    roles VARCHAR (100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Product (
    code VARCHAR (10) NOT NULL,
    name VARCHAR (50) NOT NULL,
    price BIGINT NOT NULL,
    store_name VARCHAR (50) NOT NULL,
    brand_name VARCHAR (50) NOT NULL,
    category_name VARCHAR (50) NOT NULL,
    desc TEXT NOT NULL,
    created_by uuid NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (code)
);
