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

create table article_stock_warehouses_ids
(
    article_stock_id BIGINT NOT NULL,
    warehouse_id     BIGINT NOT NULL,
    CONSTRAINT article_stock_fk FOREIGN KEY (article_stock_id) REFERENCES article_stock,
    CONSTRAINT warehouse_id_fk FOREIGN KEY (warehouse_id) REFERENCES warehouse
);