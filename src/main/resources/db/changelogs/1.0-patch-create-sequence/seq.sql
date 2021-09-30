-- liquibase formatted sql

-- changeset asxmas:1-0-seq
CREATE SEQUENCE IF NOT EXISTS "public"."hibernate_sequence" AS bigint START WITH 1500 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
