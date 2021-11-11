-- liquibase formatted sql

-- changeset asxmas:4-1-index
CREATE TABLE "public"."index"
(
    "id"                INTEGER                     NOT NULL,
    "page_id"           INTEGER                     NOT NULL,
    "lemma_id"          INTEGER                     NOT NULL,
    "rank"              FLOAT                       NOT NULL,
    CONSTRAINT "index_pkey" PRIMARY KEY ("id")
);

COMMENT ON COLUMN "public"."index"."page_id" IS 'идентификатор страницы';
COMMENT ON COLUMN "public"."index"."lemma_id" IS 'идентификатор леммы';
COMMENT ON COLUMN "public"."index"."rank" IS 'ранг леммы на этой страницы';