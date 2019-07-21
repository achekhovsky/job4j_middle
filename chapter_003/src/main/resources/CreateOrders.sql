INSERT INTO OrderImage (img) VALUES (X'01030405060708090a0b0c0d0e0f');
INSERT INTO OrderImage (img) VALUES (X'01010204020708090a0b0c0d0e0f');
INSERT INTO OrderImage (img) VALUES (X'010203040708090a0b0c0d0e0f');

INSERT INTO j4jorders (id, createDate, name, description, done, oi_fk) VALUES (1, '2019-04-25', 'order_1', 'description_1', false, 1);
INSERT INTO j4jorders (id, createDate, name, description, done, oi_fk) VALUES (2, '2019-04-23', 'order_2', 'description_2', true, 2);
INSERT INTO j4jorders (id, createDate, name, description, done, oi_fk) VALUES (3, '2019-04-21', 'order_3', 'description_3', false, 3);
