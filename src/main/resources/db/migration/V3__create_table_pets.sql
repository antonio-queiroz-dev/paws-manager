CREATE TABLE pets(
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    nome_pet    VARCHAR(255)    NOT NULL,
    pet_type    ENUM('CACHORRO', 'GATO'),
    pet_gender  ENUM('MACHO', 'FEMEA'),
    idade       INT,
    peso        DECIMAL,
    raca        VARCHAR(255),

    tutor_id BIGINT,

    PRIMARY KEY (id),
    CONSTRAINT fk_pets_tutor FOREIGN KEY (tutor_id) REFERENCES tutores(id)
    );