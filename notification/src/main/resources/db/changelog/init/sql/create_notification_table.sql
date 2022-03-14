CREATE TABLE IF NOT EXISTS notification
(
    id              SERIAL PRIMARY KEY NOT NULL,
    recipient       VARCHAR(100)       NOT NULL,
    channel         VARCHAR(100)       NOT NULL,
    code            VARCHAR(100)       NOT NULL,
    content         TEXT,
    status          VARCHAR(100)       NOT NULL,
    time_to_process TIMESTAMP          NOT NULL,
    created_at      TIMESTAMP          NOT NULL,
    attempt_count   INT                NOT NULL,
    event_id        VARCHAR(100)       NOT NULL
);
create index status_ttp_idx on notification using btree (status, time_to_process);
