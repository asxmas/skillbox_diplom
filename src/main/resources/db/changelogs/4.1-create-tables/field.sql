-- liquibase formatted sql

-- changeset asxmas:4.1-field
CREATE TABLE "public"."field"
(
    "id"                INTEGER                     NOT NULL,
    "name"              VARCHAR(255)                NOT NULL,
    "selector"          VARCHAR(255)                NOT NULL,
    "weight"            FLOAT                       NOT NULL,
    CONSTRAINT "field_pkey" PRIMARY KEY ("id")
);

COMMENT ON COLUMN "public"."field"."name" IS 'имя поля в странице';
COMMENT ON COLUMN "public"."field"."selector" IS 'CSS-выражение, позволяющее получить содержимое конкретного поля';
COMMENT ON COLUMN "public"."field"."weight" IS 'релевантность (вес) поля от 0 до 1';
