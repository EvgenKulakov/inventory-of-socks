--liquibase formatted sql

--changeset EvgeniyKulakov:1
CREATE TABLE IF NOT EXISTS socks
(
    id                SERIAL PRIMARY KEY,
    color             VARCHAR(50) NOT NULL,
    cotton_percentage INTEGER     NOT NULL CHECK (cotton_percentage between 0 and 100),
    quantity          INTEGER     NOT NULL CHECK (quantity >= 0),
    UNIQUE (color, cotton_percentage)
);