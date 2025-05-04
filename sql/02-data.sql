-- Insert 10 persons
INSERT INTO PERSONS (email, firstname, name, gender, birthday)
VALUES
    ('john.doe@example.com', 'John', 'Doe', 'Male', '1980-01-01'),
    ('jane.smith@example.com', 'Jane', 'Smith', 'Female', '1985-05-15'),
    ('bob.johnson@example.com', 'Bob', 'Johnson', 'Male', '1990-12-31'),
    ('alice.williams@example.com', 'Alice', 'Williams', 'Female', '1975-07-01'),
    ('michael.brown@example.com', 'Michael', 'Brown', 'Male', '1992-03-20'),
    ('emily.davis@example.com', 'Emily', 'Davis', 'Female', '1988-11-10'),
    ('david.wilson@example.com', 'David', 'Wilson', 'Male', '1982-06-05'),
    ('olivia.anderson@example.com', 'Olivia', 'Anderson', 'Female', '1995-09-25'),
    ('william.thompson@example.com', 'William', 'Thompson', 'Male', '1978-04-12'),
    ('sophia.martinez@example.com', 'Sophia', 'Martinez', 'Female', '1990-08-18');

-- Insert 5 addresses
INSERT INTO ADDRESSES (street, street_number, postal_code, city)
VALUES
    ('Hauptstraße', '123', '12345', 'Musterstadt'),
    ('Eichenallee', '456a', '67890', 'Beispielort'),
    ('Lindenstraße', '789', '54321', 'Anderswo'),
    ('Kiefernweg', '321b', '09876', 'Irgendwo'),
    ('Ahornweg', '654', '43210', 'Anderer Ort');

-- Insert relationships between persons and addresses
INSERT INTO PERSONS_HAVE_ADDRESSES (person_email, address_id)
VALUES
    ('john.doe@example.com', 1),
    ('jane.smith@example.com', 2),
    ('bob.johnson@example.com', 3),
    ('alice.williams@example.com', 4),
    ('michael.brown@example.com', 5),
    ('john.doe@example.com', 2),
    ('jane.smith@example.com', 3),
    ('bob.johnson@example.com', 4),
    ('alice.williams@example.com', 5),
    ('michael.brown@example.com', 1);
