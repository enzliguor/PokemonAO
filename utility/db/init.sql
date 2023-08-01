CREATE TABLE type(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    icon VARCHAR(255) NOT NULL
);

CREATE TABLE move(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    power INT NOT NULL,
    type_id INT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES type (id)
);

CREATE TABLE species(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    sprite VARCHAR(255) NOT NULL,
    type_id INT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES type (id)
);

CREATE TABLE pokemon(
    id INT PRIMARY KEY AUTO_INCREMENT,
    entity_type VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    species_id INT NOT NULL,
    current_hp INT NOT NULL,
    max_hp INT NOT NULL,
    original_trainer VARCHAR(255) NOT NULL,
    original_species_id INT,
    FOREIGN KEY (species_id) REFERENCES species (id)
);

CREATE TABLE pokemon_move(
    pokemon_id INT NOT NULL,
    move_slot VARCHAR(255) NOT NULL,
    move_id INT NOT NULL,
    PRIMARY KEY (pokemon_id, move_slot),
    FOREIGN KEY (move_id) REFERENCES move (id),
    FOREIGN KEY (pokemon_id) REFERENCES pokemon (id)
);