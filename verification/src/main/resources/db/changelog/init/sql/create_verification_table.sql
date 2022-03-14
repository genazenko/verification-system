CREATE TABLE IF NOT EXISTS verification
(
    id                   VARCHAR(100) PRIMARY KEY NOT NULL,
    code                 VARCHAR(100)             NOT NULL,
    user_agent           TEXT                     NOT NULL,
    client_ip            TEXT                     NOT NULL,
    identity             VARCHAR(100)             NOT NULL,
    type                 VARCHAR(100)             NOT NULL,
    expiration_time      TIMESTAMP                NOT NULL,
    status               VARCHAR(20)              NOT NULL,
    confirmation_attempt INTEGER                  NOT NULL
);
