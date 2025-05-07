package com.github.janrahman.postaddress_address_book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.PersonsRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.Person;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JooqRecordToDTOMapperTest {

  private JooqRecordToDTOMapper subject;

  @BeforeEach
  void setUp() {
    subject = new JooqRecordToDTOMapper();
  }

  @Test
  void givenAddressRecord_whenToAddress_thenReturnMappedAddress() {
    // given
    AddressesRecord record =
        new AddressesRecord()
            .setId(1L)
            .setStreet("Hauptstraße")
            .setStreetNumber("12")
            .setPostalCode("12345")
            .setCity("Berlin");

    // when
    Address result = subject.toAddress(record);

    // then
    assertThat(result)
        .isNotNull()
        .extracting(
            Address::getId,
            Address::getStreet,
            Address::getStreetNumber,
            Address::getPostalCode,
            Address::getCity)
        .containsExactly(1L, "Hauptstraße", "12", "12345", "Berlin");
  }

  @Test
  void givenNullAddressRecord_whenToAddress_thenThrowNullPointerException() {
    assertThatThrownBy(() -> subject.toAddress(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void givenPersonRecord_whenToPerson_thenReturnMappedPerson() {
    // given
    PersonsRecord record =
        new PersonsRecord()
            .setId(1L)
            .setFirstname("Max")
            .setName("Mustermann")
            .setGender("Male")
            .setBirthday(LocalDate.of(1990, 1, 1));

    // when
    Person result = subject.toPerson(record);

    // then
    assertThat(result)
        .isNotNull()
        .extracting(
            Person::getId,
            Person::getFirstname,
            Person::getName,
            Person::getGender,
            Person::getBirthday)
        .containsExactly(1L, "Max", "Mustermann", Person.GenderEnum.MALE, LocalDate.of(1990, 1, 1));
  }

  @Test
  void givenNullPersonRecord_whenToPerson_thenThrowNullPointerException() {
    assertThatThrownBy(() -> subject.toPerson(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void givenPersonRecordWithInvalidGender_whenToPerson_thenThrowIllegalArgumentException() {
    // given
    PersonsRecord record =
        new PersonsRecord()
            .setId(1L)
            .setFirstname("Max")
            .setName("Mustermann")
            .setGender("Invalid")
            .setBirthday(LocalDate.of(1990, 1, 1));

    // then
    assertThatThrownBy(() -> subject.toPerson(record)).isInstanceOf(IllegalArgumentException.class);
  }
}
