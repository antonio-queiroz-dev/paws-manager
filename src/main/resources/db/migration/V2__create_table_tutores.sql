CREATE TABLE tutores(
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255)    NOT NULL,
    email       VARCHAR(255),
    telefone    VARCHAR(255),

    numero_casa VARCHAR(255),
    cidade      VARCHAR(255),
    rua         VARCHAR(255),

    PRIMARY KEY(id)
    );