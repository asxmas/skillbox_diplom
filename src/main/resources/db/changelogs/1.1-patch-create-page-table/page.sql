-- liquibase formatted sql

-- changeset asxmas:1-1-page
CREATE TABLE "public"."page"
(
    "id"                INTEGER                     NOT NULL,
    "path"              VARCHAR(255)                NOT NULL,
    "code"              INTEGER                     NOT NULL,
    "content"           TEXT                        NOT NULL,
    CONSTRAINT "page_pkey" PRIMARY KEY ("id")
);
CREATE UNIQUE INDEX
    ON "page" ("path");
COMMENT ON COLUMN "public"."page"."path" IS 'адрес страницы от корня сайта (должен начинаться со слеша, например: /news/372189/)';
COMMENT ON COLUMN "public"."page"."code" IS ' код ответа, полученный при запросе страницы (например, 200, 404, 500 или другие)';
COMMENT ON COLUMN "public"."page"."content" IS ' контент страницы (HTML-код)';
