--liquibase formatted sql

--changeset EvgeniyKulakov:1
CREATE TABLE IF NOT EXISTS socks
(
    id                SERIAL PRIMARY KEY,
    color             VARCHAR(50) NOT NULL,
    cotton_percentage INTEGER     NOT NULL CHECK (cotton_percentage >= 0 AND cotton_percentage <= 100),
    quantity          INTEGER     NOT NULL CHECK (quantity >= 0),
    UNIQUE (color, cotton_percentage)
);