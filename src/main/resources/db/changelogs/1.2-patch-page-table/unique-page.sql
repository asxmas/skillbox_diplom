-- liquibase formatted sql

-- changeset asxmas:1.2-unique-page

alter table page
    add constraint page_uindex unique (path);


