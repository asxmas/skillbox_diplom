-- liquibase formatted sql

-- changeset asxmas:6-1-create-site
CREATE TABLE "public"."site"
(
    "id"                INTEGER                     NOT NULL,
    "status"            VARCHAR(20)                 NOT NULL,
    "status_time"       TIMESTAMP                   NOT NULL,
    "last_error"        TEXT,
    "url"               VARCHAR(255)                NOT NULL,
    "name"              VARCHAR(255)                NOT NULL,
    CONSTRAINT "site_pkey" PRIMARY KEY ("id")
);
COMMENT ON COLUMN "public"."site"."status" IS 'текущий статус полной индексации сайта - индексация или переиндексация в процессе, сайт полностью проиндексирован (готов к поиску) или не удалось проиндексировать (сайт не готов к поиску и не будет до устранения ошибок и перезапуска индексации)';
COMMENT ON COLUMN "public"."site"."status_time" IS 'дата и время статуса (в случае статуса INDEXING дата и время должны обновляться регулярно при добавлении каждой новой страницы в индекс)';
COMMENT ON COLUMN "public"."site"."last_error" IS 'текст ошибки индексации или NULL, если её не было';
COMMENT ON COLUMN "public"."site"."url" IS 'адрес главной страницы сайта';
COMMENT ON COLUMN "public"."site"."name" IS 'имя сайта';