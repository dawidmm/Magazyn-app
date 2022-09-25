create sequence users_id_sequence start 3;

create table users
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('users_id_sequence'),
    login    TEXT UNIQUE NOT NULL,
    password TEXT        NOT NULL,
    role     TEXT        NOT NULL,
    active   BOOLEAN     NOT NULL
);
