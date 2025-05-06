CREATE TABLE IF NOT EXISTS PERSONS (
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    CONSTRAINT positive_id CHECK (id > 0),
    CONSTRAINT valid_gender CHECK (gender IN ('Male', 'Female', 'Nonbinary'))
);

CREATE TABLE IF NOT EXISTS ADDRESSES (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    street_number VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    CONSTRAINT positive_id CHECK (id > 0),
    CONSTRAINT valid_street_number CHECK (street_number ~ '^[0-9]+[a-z]?$'),
    CONSTRAINT valid_postal_code CHECK (postal_code ~ '^[0-9]{5}$')
);

CREATE TABLE IF NOT EXISTS PERSONS_HAVE_ADDRESSES (
    person_id BIGINT,
    address_id BIGINT,
    PRIMARY KEY (person_id, address_id),
    FOREIGN KEY (person_id) REFERENCES PERSONS(id),
    FOREIGN KEY (address_id) REFERENCES ADDRESSES(id)
);
