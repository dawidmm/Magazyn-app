create sequence if not exists article_stock_id_seq start 1;

create table article_stock
(
    id         BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('article_stock_id_seq'),
    article_id BIGINT NOT NULL,
    amount     BIGINT NOT NULL,
    vat        int4   NOT NULL,
    price      int4   NOT NULL,
    files      TEXT   NULL,
    CONSTRAINT article_id_fk FOREIGN KEY (article_id) REFERENCES article
);