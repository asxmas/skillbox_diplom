-- liquibase formatted sql

-- changeset asxmas:6.2-add-site-id-to-lemma
alter table "public"."lemma"
    ADD CONSTRAINT "lemma_site_id_fk" FOREIGN KEY ("site_id") REFERENCES "public"."site" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

