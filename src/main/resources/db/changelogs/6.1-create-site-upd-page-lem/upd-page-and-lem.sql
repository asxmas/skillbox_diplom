-- liquibase formatted sql

-- changeset asxmas:6-1-update-lemma-and-index-tables
ALTER TABLE "public"."page"
    ADD "site_id" INTEGER;

ALTER TABLE "public"."lemma"
    ADD "site_id" INTEGER;

