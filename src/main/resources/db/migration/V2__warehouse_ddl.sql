create sequence "seq_warehouse_id" start 1;

create table warehouse
(
    id             BIGINT PRIMARY KEY DEFAULT nextval('seq_warehouse_id'),
    warehouse_name TEXT NOT NULL
);

create table warehouse_users_ids
(
    user_id      BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users,
    CONSTRAINT warehouse_id_fk FOREIGN KEY (warehouse_id) REFERENCES warehouse
);
