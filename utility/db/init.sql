CREATE TABLE type(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    icon VARCHAR(255) NOT NULL
);

CREATE TABLE move(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    power INT NOT NULL,
    type_id BIGINT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES type (id)
);

CREATE TABLE pokemon(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    sprite VARCHAR(255) NOT NULL,
    current_hp INT NOT NULL,
    max_hp INT NOT NULL,
    original_trainer VARCHAR(255) NOT NULL,
    type_id BIGINT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES type (id)
);

CREATE TABLE pokemon_moves(
    pokemon_id BIGINT NOT NULL,
    move_id BIGINT NOT NULL,
    FOREIGN KEY (move_id) REFERENCES move (id),
    FOREIGN KEY (pokemon_id) REFERENCES pokemon (id)
);