package com.github.janrahman.postaddress_address_book.service;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.PersonsRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.AvgAge;
import com.github.janrahman.postaddress_address_book.openapi.model.Person;
import com.github.janrahman.postaddress_address_book.openapi.model.PersonsIdAddressesPostRequest;
import com.github.janrahman.postaddress_address_book.repository.AddressRepositoryApi;
import com.github.janrahman.postaddress_address_book.repository.PersonRepositoryApi;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PersonService implements PersonServiceApi {

  private final PersonRepositoryApi personRepository;

  private final AddressRepositoryApi addressRepository;

  private final JooqRecordToDTOMapper jooqRecordToDTOMapper;

  public PersonService(
      PersonRepositoryApi personRepository, AddressRepositoryApi addressRepository) {
    this.personRepository = personRepository;
    jooqRecordToDTOMapper = new JooqRecordToDTOMapper();
    this.addressRepository = addressRepository;
  }

  @Override
  public ResponseEntity<List<Person>> getAll(
      String street, String streetNumber, String postalCode, String city) {
    List<PersonsRecord> peopleRecords = getPersonsRecords(street, streetNumber, postalCode, city);
    return ResponseEntity.ok(peopleRecords.stream().map(jooqRecordToDTOMapper::toPerson).toList());
  }

  @Override
  public ResponseEntity<List<Address>> getAddresses(Long id) {
    List<Address> addresses =
        Optional.ofNullable(id)
            .map(personRepository::findAddressesByPersonId)
            .orElseThrow(() -> new IllegalArgumentException("Person with Id not found."))
            .stream()
            .map(jooqRecordToDTOMapper::toAddress)
            .toList();
    return ResponseEntity.ok(addresses);
  }

  @Override
  public ResponseEntity<Address> saveAddress(Long id, PersonsIdAddressesPostRequest personAddress) {
    if (Objects.isNull(id)) {
      throw new IllegalArgumentException("All fields must not be null.");
    }

    Long addressId =
        Optional.ofNullable(personAddress)
            .map(PersonsIdAddressesPostRequest::getAddressId)
            .orElseThrow(() -> new IllegalArgumentException("Person addressId must not be null."));

    if (!(personRepository.exists(id) && addressRepository.exists(addressId))) {
      throw new IllegalArgumentException("Person with Id and address not found.");
    }

    if (personRepository.saveAssociation(id, addressId) > 0) {
      Address address = jooqRecordToDTOMapper.toAddress(addressRepository.findById(addressId));
      return ResponseEntity.ok(address);
    }

    return ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<AvgAge> getAverageAgeByPostalCode(String postalCode) {
    LocalDate currentDate = LocalDate.now();
    double averageAge =
        personRepository.findAllPersonBirthdaysByPostalCode(postalCode).stream()
            .map(it -> Period.between(it, currentDate))
            .map(Period::getYears)
            .mapToDouble(Integer::doubleValue)
            .average()
            .orElse(-1d);

    AvgAge result = new AvgAge();
    result.setAverageAge(averageAge);

    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<Void> delete(Long id) {
    return Optional.ofNullable(id)
        .map(this::removePersonAndAssociation)
        .filter(it -> it > 0)
        .map(ignore -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  private long removePersonAndAssociation(long id) {
    return personRepository.deleteAssociation(id) + personRepository.delete(id);
  }

  private List<PersonsRecord> getPersonsRecords(
      String street, String streetNumber, String postalCode, String city) {
    if (Stream.of(street, streetNumber, postalCode, city).allMatch(Objects::isNull)) {
      return personRepository.findAll();
    }
    return personRepository.findAllByAddressData(street, streetNumber, postalCode, city);
  }
}
