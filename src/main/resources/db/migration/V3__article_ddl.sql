create sequence "article_id_seq" start 1;

create table article
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('article_id_seq'),
    name TEXT NOT NULL UNIQUE,
    type TEXT NOT NULL
);