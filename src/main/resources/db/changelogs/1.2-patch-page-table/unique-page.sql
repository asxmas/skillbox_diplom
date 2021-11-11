-- liquibase formatted sql

-- changeset asxmas:1-2-unique-page

ALTER TABLE "public"."page"
    ADD CONSTRAINT "page_uindex" UNIQUE ("path");


