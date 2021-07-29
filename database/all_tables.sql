-- user
CREATE TABLE "user"
(
    id        char(36)     NOT NULL,
    login     VARCHAR(30)  NOT NULL,
    full_name VARCHAR(50)  NOT NULL,
    email     VARCHAR(320) NOT NULL,
    password  TEXT         NOT NULL,
    "role"    VARCHAR(30)  NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_id UNIQUE (id);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_login UNIQUE (login);

-- refresh_token
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