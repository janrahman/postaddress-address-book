package com.github.janrahman.postaddress_address_book.person;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.PersonsRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.Person;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PersonService implements PersonServiceApi {

  private final PersonRepositoryApi personRepository;

  public PersonService(PersonRepositoryApi personRepository) {
    this.personRepository = personRepository;
  }

  @Override
  public ResponseEntity<List<Person>> getAll(
      String street, String streetNumber, String postalCode, String city) {
    List<PersonsRecord> peopleRecords = getPersonsRecords(street, streetNumber, postalCode, city);
    return ResponseEntity.ok(peopleRecords.stream().map(this::toPerson).toList());
  }

  private List<PersonsRecord> getPersonsRecords(
      String street, String streetNumber, String postalCode, String city) {
    if (Stream.of(street, streetNumber, postalCode, city).allMatch(Objects::isNull)) {
      return personRepository.findAll();
    }
    return personRepository.findAllByAddressData(street, streetNumber, postalCode, city);
  }

  private Person toPerson(PersonsRecord record) {
    var person = new Person();

    person.setId(record.getId());
    person.setFirstname(record.getFirstname());
    person.setName(record.getName());
    person.setBirthday(record.getBirthday());
    person.setGender(Person.GenderEnum.fromValue(record.getGender()));

    return person;
  }
}
