CREATE USER oauth_backend WITH PASSWORD 'password';
CREATE DATABASE oauth_db WITH ENCODING='utf-8';
GRANT ALL PRIVILEGES ON DATABASE oauth_db TO oauth_backend;

\connect oauth_db;

-- oauth_client_details
CREATE TABLE IF NOT EXISTS t_oauth_client_details (
    client_id               VARCHAR(256) PRIMARY KEY,
    resource_ids            VARCHAR(256),
    client_secret           VARCHAR(256),
    scope                   VARCHAR(256),
    authorized_grant_types  VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(256)
);
ALTER TABLE t_oauth_client_details OWNER TO oauth_backend;

-- oauth_client_token
CREATE TABLE IF NOT EXISTS t_oauth_client_token (
    token_id                VARCHAR(256),
    token                   BYTEA,
    authentication_id       VARCHAR(256) PRIMARY KEY,
    user_name               VARCHAR(256),
    client_id               VARCHAR(256)
);
ALTER TABLE t_oauth_client_token OWNER TO oauth_backend;

-- oauth_access_token
CREATE TABLE IF NOT EXISTS t_oauth_access_token (
    token_id                VARCHAR(256),
    token                   BYTEA,
    authentication_id       VARCHAR(256) PRIMARY KEY,
    user_name               VARCHAR(256),
    client_id               VARCHAR(256),
    authentication          BYTEA,
    refresh_token           VARCHAR(256)
);
ALTER TABLE t_oauth_access_token OWNER TO oauth_backend;

-- oauth_refresh_token
CREATE TABLE IF NOT EXISTS t_oauth_refresh_token (
    token_id                VARCHAR(256),
    token                   BYTEA,
    authentication          BYTEA
);
ALTER TABLE t_oauth_refresh_token OWNER TO oauth_backend;

-- oauth_code
CREATE TABLE IF NOT EXISTS t_oauth_code (
    code                    VARCHAR(256),
    authentication          BYTEA
);
ALTER TABLE t_oauth_code OWNER TO oauth_backend;

-- oauth_approvals
CREATE TABLE IF NOT EXISTS t_oauth_approvals (
    user_id                 VARCHAR(256),
    client_id               VARCHAR(256),
    scope                   VARCHAR(256),
    status                  VARCHAR(10),
    expires_at              TIMESTAMP,
    last_modified_at        TIMESTAMP
);
ALTER TABLE t_oauth_approvals OWNER TO oauth_backend;

-- t_user
CREATE TABLE IF NOT EXISTS t_user (
    id                      VARCHAR(256) PRIMARY KEY,
    username                VARCHAR(256),
    password                VARCHAR(256),
    create_at               TIMESTAMP,
    update_at               TIMESTAMP
);
ALTER TABLE t_user OWNER TO oauth_backend;

-- t_role
CREATE TABLE IF NOT EXISTS t_role (
    id                      VARCHAR(256) PRIMARY KEY,
    name                    VARCHAR(256)
);
ALTER TABLE t_role OWNER TO oauth_backend;

-- t_user_role
CREATE TABLE IF NOT EXISTS t_user_role (
    id                      VARCHAR(256) PRIMARY KEY,
    user_id                 VARCHAR(256),
    role_id                 VARCHAR(256)
);
ALTER TABLE t_user_role OWNER TO oauth_backend;
