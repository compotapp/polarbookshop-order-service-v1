ALTER TABLE orders
    ADD COLUMN created_by varchar(255),
    ADD COLUMN last_modified_by varchar(255);
