-- liquibase formatted sql

-- changeset Tooba aziz:1674649039
CREATE TABLE sample (id VARCHAR(200) NOT NULL, name VARCHAR(200) DEFAULT '' NOT NULL, created_by VARCHAR(200) DEFAULT '' NOT NULL, updated_by VARCHAR(200) DEFAULT '' NOT NULL, created_at timestamp DEFAULT NOW() NOT NULL, updated_at timestamp DEFAULT NOW() NOT NULL);
