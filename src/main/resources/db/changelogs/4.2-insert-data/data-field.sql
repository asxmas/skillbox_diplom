-- liquibase formatted sql

-- changeset asxmas:4-2-data-field context:prod

INSERT INTO "public"."field" ("id", "name", "selector", "weight")
VALUES (1, 'title', 'title', 1.0),
       (2, 'body', 'body', 0.8);;