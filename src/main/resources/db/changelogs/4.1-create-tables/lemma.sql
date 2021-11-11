-- liquibase formatted sql

-- changeset asxmas:4-1-lemma
CREATE TABLE "public"."lemma"
(
    "id"                INTEGER                     NOT NULL,
    "lemma"             TEXT                NOT NULL,
    "frequency"         INTEGER                     NOT NULL,
    CONSTRAINT "lemma_pkey" PRIMARY KEY ("id")
);

COMMENT ON COLUMN "public"."lemma"."lemma" IS 'нормальная форма слова';
COMMENT ON COLUMN "public"."lemma"."frequency" IS 'количество страниц, на которых слово встречается хотя бы один раз. Максимальное значение не может превышать общее количество слов на сайте';
