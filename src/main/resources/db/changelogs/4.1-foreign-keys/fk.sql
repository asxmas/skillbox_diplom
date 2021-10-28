-- liquibase formatted sql

-- changeset asxmas:4.1-fk

ALTER TABLE "public"."index"
    ADD CONSTRAINT "index_page_id_fk" FOREIGN KEY ("page_id") REFERENCES "public"."page" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
    ADD CONSTRAINT "index_lemma_id_fk" FOREIGN KEY ("lemma_id") REFERENCES "public"."lemma" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;
