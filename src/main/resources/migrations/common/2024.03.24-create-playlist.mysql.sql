-- liquibase formatted sql

-- changeset Tooba-Aziz:1674212523
CREATE TABLE playlist (
    id varchar(200) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE playlist_item (
    id varchar(200) PRIMARY KEY,
    playlist_id BIGINT NOT NULL,
    video_id VARCHAR(255) NOT NULL,
    start_time BIGINT NOT NULL,
    end_time BIGINT NOT NULL,
    sequence BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL
);
