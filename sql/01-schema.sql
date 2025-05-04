CREATE TABLE IF NOT EXISTS PERSONS (
    email VARCHAR(255) PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(255) NOT NULL,
    birthday DATE,
    CONSTRAINT valid_email CHECK(email ~ '^[a-zA-Z0-9_!#$%&''*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$'),
    CONSTRAINT valid_birthday CHECK (birthday IS NOT NULL)
);

CREATE TABLE IF NOT EXISTS ADDRESSES (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    street_number VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    CONSTRAINT positive_id CHECK (id > 0),
    CONSTRAINT valid_street_number CHECK (street_number ~ '^[0-9]+[a-z]?$'),
    CONSTRAINT valid_postal_code CHECK (postal_code ~ '^[0-9]{5}$')
);

CREATE TABLE IF NOT EXISTS PERSONS_HAVE_ADDRESSES (
    person_email VARCHAR(255),
    address_id INTEGER,
    PRIMARY KEY (person_email, address_id),
    FOREIGN KEY (person_email) REFERENCES PERSONS(email),
    FOREIGN KEY (address_id) REFERENCES ADDRESSES(id)
);
