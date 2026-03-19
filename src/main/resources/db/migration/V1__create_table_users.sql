
CREATE TABLE users (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    email       VARCHAR(255)    NOT NULL,
    password    VARCHAR(255)    NOT NULL,
    role        ENUM('ADMIN', 'USER'),

    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
    );