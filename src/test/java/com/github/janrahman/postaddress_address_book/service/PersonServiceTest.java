package com.github.janrahman.postaddress_address_book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.AvgAge;
import com.github.janrahman.postaddress_address_book.openapi.model.PersonsIdAddressesPostRequest;
import com.github.janrahman.postaddress_address_book.repository.AddressRepositoryApi;
import com.github.janrahman.postaddress_address_book.repository.PersonRepositoryApi;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  @Mock private PersonRepositoryApi personRepository;

  @Mock private AddressRepositoryApi addressRepository;

  @InjectMocks private PersonService personService;

  @Test
  void givenValidPersonAndAddressIds_whenSaveAddress_thenReturnSavedAddress() {
    // given
    long personId = 1L;
    long addressId = 2L;
    PersonsIdAddressesPostRequest request = new PersonsIdAddressesPostRequest();
    request.setAddressId(addressId);

    AddressesRecord addressRecord = new AddressesRecord();
    addressRecord.setId(addressId);
    addressRecord.setStreet("Test Street");
    addressRecord.setStreetNumber("123");
    addressRecord.setPostalCode("12345");
    addressRecord.setCity("Test City");

    when(personRepository.exists(personId)).thenReturn(true);
    when(addressRepository.exists(addressId)).thenReturn(true);
    when(personRepository.saveAssociation(personId, addressId)).thenReturn(1L);
    when(addressRepository.findById(addressId)).thenReturn(addressRecord);

    // when
    ResponseEntity<Address> response = personService.saveAddress(personId, request);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody())
        .extracting("street", "streetNumber", "postalCode", "city")
        .containsExactly("Test Street", "123", "12345", "Test City");
  }

  @Test
  void givenNullPersonId_whenSaveAddress_thenThrowIllegalArgumentException() {
    // given
    PersonsIdAddressesPostRequest request = new PersonsIdAddressesPostRequest();
    request.setAddressId(1L);

    // when/then
    assertThatThrownBy(() -> personService.saveAddress(null, request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("All fields must not be null.");
  }

  @Test
  void givenValidPostalCode_whenGetAverageAge_thenReturnCorrectAverage() {
    // given
    String postalCode = "12345";
    LocalDate now = LocalDate.now();
    List<LocalDate> birthdays = List.of(now.minusYears(20), now.minusYears(30), now.minusYears(40));

    when(personRepository.findAllPersonBirthdaysByPostalCode(postalCode)).thenReturn(birthdays);

    // when
    ResponseEntity<AvgAge> response = personService.getAverageAgeByPostalCode(postalCode);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).extracting(AvgAge::getAverageAge).isEqualTo(30.0);
  }

  @Test
  void givenPostalCodeWithNoPeople_whenGetAverageAge_thenReturnNegativeOne() {
    // given
    String postalCode = "12345";
    when(personRepository.findAllPersonBirthdaysByPostalCode(postalCode)).thenReturn(List.of());

    // when
    ResponseEntity<AvgAge> response = personService.getAverageAgeByPostalCode(postalCode);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).extracting(AvgAge::getAverageAge).isEqualTo(-1.0);
  }
}
