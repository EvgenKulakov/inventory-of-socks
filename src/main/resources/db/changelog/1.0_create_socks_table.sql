--liquibase formatted sql

--changeset EvgeniyKulakov:1
CREATE TABLE IF NOT EXISTS socks
(
    id                SERIAL PRIMARY KEY NOT NULL,
    color             VARCHAR(50)        NOT NULL,
    cotton_percentage INTEGER            NOT NULL,
    count             INTEGER            NOT NULL
);