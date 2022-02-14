-- liquibase formatted sql

-- changeset asxmas:6.3-insert-data-to-field
INSERT INTO "public"."field" ("id", "name", "selector", "weight")
VALUES (1, 'title', 'title', '1.0'), (2, 'body', 'body', '0.8');;