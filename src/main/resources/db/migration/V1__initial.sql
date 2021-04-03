CREATE TABLE app_users
(
    user_id            BIGSERIAL PRIMARY KEY NOT NULL,
    email              TEXT UNIQUE           NOT NULL,
    password           TEXT                  NOT NULl,
    locked             BOOLEAN               NOT NULL,
    enabled            BOOLEAN               NOT NULL,
    created_date       TIMESTAMP             NOT NULL,
    last_modified_date TIMESTAMP             NOT NULL
);

CREATE TABLE tokens
(
    token_id  BIGSERIAL PRIMARY KEY NOT NULL,
    api_token TEXT                  NOT NULL,
    user_id   BIGINT                NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id)
);

CREATE TABLE roles
(
    role_id SMALLSERIAL PRIMARY KEY NOT NULL,
    name    TEXT UNIQUE             NOT NULL
);

CREATE TABLE users_roles
(
    user_id BIGINT   NOT NULL,
    role_id SMALLINT NOT NULL,
    FOREIGN KEY (role_id)
        REFERENCES roles (role_id),
    FOREIGN KEY (user_id)
        REFERENCES app_users (user_id)
);


CREATE TABLE user_details
(
    detail_id          BIGSERIAL NOT NULL PRIMARY KEY,
    first_name         TEXT      NOT NULL,
    last_name          TEXT      NOT NULL,
    middle_name        TEXT,
    house_number       TEXT      NOT NULL,
    region             TEXT      NOT NULL,
    city               TEXT      NOT NULL,
    town               TEXT      NOT NULL,
    user_id            BIGINT    NOT NULL,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id)
);

CREATE TABLE merchants
(
    merchant_id        BIGSERIAL NOT NULL PRIMARY KEY,
    name               TEXT      NOT NULL UNIQUE,
    city               TEXT      NOT NULL,
    region             TEXT      NOT NULL,
    address            TEXT      NOT NULL,
    email              TEXT      NOT NULL UNIQUE,
    user_id            BIGINT    NOT NULL,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id)
);

create table networks
(
    network_id SMALLSERIAL NOT NULL PRIMARY KEY,
    name       TEXT UNIQUE NOT NULL
);
CREATE TABLE phone_numbers
(
    phone_id           BIGSERIAL   NOT NULL PRIMARY KEY,
    number             TEXT UNIQUE NOT NULL,
    network_id         SMALLINT    NOT NULL,
    is_default         BOOLEAN     NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    last_modified_date TIMESTAMP   NOT NULL,
    FOREIGN KEY (network_id) REFERENCES networks (network_id)
);


CREATE TABLE fingerprints
(
    fingerprint_id     BIGSERIAL   NOT NULL PRIMARY KEY,
    image_url          TEXT UNIQUE NOT NULL,
    user_id            BIGINT      NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    last_modified_date TIMESTAMP   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id)
);

CREATE TABLE statuses
(
    status_id SMALLSERIAL NOT NULL PRIMARY KEY,
    name      TEXT UNIQUE NOT NULL
);

CREATE TABLE transactions
(
    transaction_id     BIGSERIAL        NOT NULL PRIMARY KEY,
    sender_id          BIGINT           NOT NULL,
    receiver_id        BIGINT           NOT NULL,
    price              DOUBLE PRECISION NOT NULL,
    status_id          SMALLINT         NOT NULL,
    created_date       TIMESTAMP        NOT NULL,
    last_modified_date TIMESTAMP        NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES phone_numbers (phone_id),
    FOREIGN KEY (receiver_id) REFERENCES phone_numbers (phone_id),
    FOREIGN KEY (status_id) REFERENCES statuses (status_id)
);

CREATE TABLE devices
(
    device_id          BIGSERIAL   NOT NULL PRIMARY KEY,
    name               TEXT UNIQUE NOT NULL,
    merchant_id        BIGINT      NOT NULL,
    created_date       TIMESTAMP   NOT NULL,
    last_modified_date TIMESTAMP   NOT NULL,
    FOREIGN KEY (merchant_id) REFERENCES merchants (merchant_id)
);



CREATE TABLE legal_documents
(
    document_id        BIGSERIAL PRIMARY KEY NOT NULL,
    document_url       TEXT                  NOT NULL UNIQUE,
    merchant_id        BIGINT                NOT NULL,
    created_date       TIMESTAMP             NOT NULL,
    last_modified_date TIMESTAMP             NOT NULL,
    FOREIGN KEY (merchant_id) REFERENCES merchants (merchant_id)
);

CREATE TABLE users_phone_numbers
(
    user_id         BIGINT NOT NULL,
    phone_number_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users (user_id),
    FOREIGN KEY (phone_number_id) REFERENCES phone_numbers (phone_id)
);

CREATE TABLE merchants_phone_numbers
(
    merchant_id     BIGINT NOT NULL,
    phone_number_id BIGINT NOT NULL,
    FOREIGN KEY (merchant_id) REFERENCES merchants (merchant_id),
    FOREIGN KEY (phone_number_id) REFERENCES phone_numbers (phone_id)
);


CREATE TABLE email_confirmation_tokens(
    token_id BIGSERIAL NOT NULL PRIMARY KEY,
    token TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    confirmed_at TIMESTAMP NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(user_id)
);



