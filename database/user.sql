CREATE TABLE user
(
    id        uuid     NOT NULL,
    login     VARCHAR(30)  NOT NULL,
    full_name VARCHAR(50)  NOT NULL,
    email     VARCHAR(320) NOT NULL,
    password  TEXT         NOT NULL,
    "role"    VARCHAR(30)  NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user
    ADD CONSTRAINT uc_user_id UNIQUE (id);

ALTER TABLE user
    ADD CONSTRAINT uc_user_login UNIQUE (login);