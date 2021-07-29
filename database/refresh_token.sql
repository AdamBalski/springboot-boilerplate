CREATE TABLE refresh_token
(
    id              BIGINT      NOT NULL,
    user_id         UUID        NOT NULL,
    token           VARCHAR(12) NOT NULL,
    expiration_date date        NOT NULL,
    CONSTRAINT pk_refresh_token PRIMARY KEY (id)
);

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refresh_token_id UNIQUE (id);

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESH_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id)