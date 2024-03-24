-- liquibase formatted sql

-- changeset Tooba-Aziz:1674212523
CREATE TABLE VODVideoSource (
    id varchar(200) PRIMARY KEY,
    url VARCHAR(255),
    downloadUrl VARCHAR(255),
    thumbnailUrl VARCHAR(255),
    title VARCHAR(255)
);
