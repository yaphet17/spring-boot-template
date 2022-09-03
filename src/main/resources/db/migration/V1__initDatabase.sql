CREATE SEQUENCE IF NOT EXISTS privilege_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS role_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS token_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE app_users
(
    appuser_id  BIGINT       NOT NULL,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255),
    user_name   VARCHAR(255),
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255),
    dob         date,
    auth_type   INTEGER,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    enabled     BOOLEAN,
    locked      BOOLEAN,
    deleted     BOOLEAN,
    CONSTRAINT pk_app_users PRIMARY KEY (appuser_id)
);

CREATE TABLE appuser_roles
(
    appuser_id BIGINT NOT NULL,
    role_id    BIGINT NOT NULL,
    CONSTRAINT pk_appuser_roles PRIMARY KEY (appuser_id, role_id)
);

CREATE TABLE confirmation_token
(
    confirmation_toke_id BIGINT       NOT NULL,
    token                VARCHAR(255) NOT NULL,
    created_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at          TIMESTAMP WITHOUT TIME ZONE,
    expires_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    confirmed_at         TIMESTAMP WITHOUT TIME ZONE,
    appuser_id           BIGINT       NOT NULL,
    CONSTRAINT pk_confirmation_token PRIMARY KEY (confirmation_toke_id)
);

CREATE TABLE privileges
(
    privilege_id   BIGINT       NOT NULL,
    privilege_name VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_privileges PRIMARY KEY (privilege_id)
);

CREATE TABLE role_privileges
(
    privilege_id BIGINT NOT NULL,
    role_id      BIGINT NOT NULL,
    CONSTRAINT pk_role_privileges PRIMARY KEY (privilege_id, role_id)
);

CREATE TABLE roles
(
    role_id          BIGINT       NOT NULL,
    role_name        VARCHAR(255) NOT NULL,
    role_description VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_roles PRIMARY KEY (role_id)
);

ALTER TABLE confirmation_token
    ADD CONSTRAINT token_unique UNIQUE (token);

ALTER TABLE app_users
    ADD CONSTRAINT uc_app_users_email UNIQUE (email);

ALTER TABLE privileges
    ADD CONSTRAINT uc_privileges_privilege_name UNIQUE (privilege_name);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_role_name UNIQUE (role_name);

ALTER TABLE confirmation_token
    ADD CONSTRAINT FK_CONFIRMATION_TOKEN_ON_APPUSER FOREIGN KEY (appuser_id) REFERENCES app_users (appuser_id);

ALTER TABLE appuser_roles
    ADD CONSTRAINT fk_approl_on_app_user FOREIGN KEY (appuser_id) REFERENCES app_users (appuser_id);

ALTER TABLE appuser_roles
    ADD CONSTRAINT fk_approl_on_role FOREIGN KEY (role_id) REFERENCES roles (role_id);

ALTER TABLE role_privileges
    ADD CONSTRAINT fk_rolpri_on_privilege FOREIGN KEY (privilege_id) REFERENCES privileges (privilege_id);

ALTER TABLE role_privileges
    ADD CONSTRAINT fk_rolpri_on_role FOREIGN KEY (role_id) REFERENCES roles (role_id);