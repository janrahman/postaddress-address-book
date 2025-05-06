package com.github.janrahman.postaddress_address_book.converter;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.PersonsRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.Person;
import org.springframework.lang.NonNull;

public class JooqRecordToDTOMapper {

  public Address toAddress(@NonNull AddressesRecord record) {
    Address address = new Address();

    address.setId(record.getId());
    address.setStreet(record.getStreet());
    address.setStreetNumber(record.getStreetNumber());
    address.setPostalCode(record.getPostalCode());
    address.setCity(record.getCity());

    return address;
  }

  public Person toPerson(@NonNull PersonsRecord record) {
    var person = new Person();

    person.setId(record.getId());
    person.setFirstname(record.getFirstname());
    person.setName(record.getName());
    person.setBirthday(record.getBirthday());
    person.setGender(Person.GenderEnum.fromValue(record.getGender()));

    return person;
  }
}
