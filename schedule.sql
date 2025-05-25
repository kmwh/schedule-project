use schedule;

CREATE TABLE author (
    id BIGINT PRIMARY KEY,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    email VARCHAR(255),
    name VARCHAR(255)
);

CREATE TABLE schedule (
    id BIGINT PRIMARY KEY,
    author_id BIGINT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    todo VARCHAR(200),
    password VARCHAR(255),
    FOREIGN KEY (author_id) REFERENCES author(id)
);
