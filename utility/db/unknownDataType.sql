INSERT INTO type (id, name, icon)
VALUES (10000, 'Unknown Type', 'https://example.com/unknown.png');
INSERT INTO species(id, name, sprite, type_id)
VALUES (10000, 'Unknown Species', 'https://example.com/sprite', 10000);
INSERT INTO move (id, entity_type, name, power, type_id, original_move_id)
VALUES (10000, 'unknown_move', 'Unknown Move', 0, 10000, 150);