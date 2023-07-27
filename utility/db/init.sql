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
    name VARCHAR(255) NOT NULL,
    species_id INT NOT NULL,
    current_hp INT NOT NULL,
    max_hp INT NOT NULL,
    original_trainer VARCHAR(255) NOT NULL,
    FOREIGN KEY (species_id) REFERENCES species (id)
);

CREATE TABLE pokemon_moves(
    pokemon_id INT NOT NULL,
    move_slot VARCHAR(255) NOT NULL,
    move_id INT NOT NULL,
    FOREIGN KEY (move_id) REFERENCES move (id),
    FOREIGN KEY (pokemon_id) REFERENCES pokemon (id)
);