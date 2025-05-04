-- Persons
INSERT INTO PERSONS (firstname, name, gender, birthday)
VALUES ('Max', 'Mustermann', 'Male', '1985-01-01'),
       ('Lena', 'Müller', 'Female', '1990-05-15'),
       ('Tobias', 'Schmidt', 'Male', '1978-11-30'),
       ('Sophia', 'Schneider', 'Female', '1992-03-20'),
       ('Lukas', 'Weber', 'Male', '1988-07-12'),
       ('Emilia', 'Becker', 'Female', '1995-09-05'),
       ('Jonas', 'Hoffmann', 'Male', '1982-04-28'),
       ('Leonie', 'Fischer', 'Female', '1993-11-10'),
       ('Florian', 'Köhler', 'Male', '1987-06-18'),
       ('Amelie', 'Wagner', 'Female', '1991-02-14');

-- Addresses
INSERT INTO ADDRESSES (street, street_number, postal_code, city)
VALUES ('Hauptstraße', '12', '12345', 'Berlin'),
       ('Lindenallee', '7a', '54321', 'München'),
       ('Parkweg', '3', '67890', 'Hamburg'),
       ('Bergstraße', '21b', '24680', 'Köln'),
       ('Marktplatz', '5', '13579', 'Dresden');

-- Persons have Addresses
INSERT INTO PERSONS_HAVE_ADDRESSES (person_id, address_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 3),
       (3, 4),
       (4, 2),
       (5, 1),
       (5, 5),
       (6, 3),
       (7, 4),
       (8, 5),
       (9, 1),
       (9, 3),
       (10, 2),
       (10, 4);
