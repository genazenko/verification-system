CREATE TABLE IF NOT EXISTS template
(
    id           SERIAL PRIMARY KEY NOT NULL,
    slug         VARCHAR(100)       NOT NULL,
    content      TEXT               NOT NULL,
    content_type VARCHAR(100)       NOT NULL
);
create unique index slug_idx on template using btree (slug);
