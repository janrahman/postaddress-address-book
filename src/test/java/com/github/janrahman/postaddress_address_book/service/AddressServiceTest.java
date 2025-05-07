package com.github.janrahman.postaddress_address_book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.NewAddress;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdateAddress;
import com.github.janrahman.postaddress_address_book.repository.AddressRepositoryApi;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

  private MockHttpServletRequest request;

  @Mock private AddressRepositoryApi addressRepository;

  @InjectMocks private AddressService addressService;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    this.request.setScheme("http");
    this.request.setServerName("localhost");
    this.request.setServerPort(-1);
    this.request.setRequestURI("/address");
    this.request.setContextPath("/address");
  }

  @Test
  void givenValidNewAddress_whenSave_thenReturnSavedAddress() {
    // given
    NewAddress newAddress =
        new NewAddress()
            .street("Test Street")
            .streetNumber("123")
            .postalCode("12345")
            .city("Test City");

    AddressesRecord savedRecord =
        new AddressesRecord()
            .setId(1L)
            .setStreet(newAddress.getStreet())
            .setStreetNumber(newAddress.getStreetNumber())
            .setPostalCode(newAddress.getPostalCode())
            .setCity(newAddress.getCity());

    when(addressRepository.existsByAddressData(
            newAddress.getCity(),
            newAddress.getPostalCode(),
            newAddress.getStreet(),
            newAddress.getStreetNumber()))
        .thenReturn(false);
    when(addressRepository.save(newAddress)).thenReturn(savedRecord);

    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(this.request));

    // when
    ResponseEntity<Address> response = addressService.save(newAddress);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody())
        .extracting("id", "street", "streetNumber", "postalCode", "city")
        .containsExactly(1L, "Test Street", "123", "12345", "Test City");
  }

  @Test
  void givenNullAddress_whenSave_thenThrowIllegalArgumentException() {
    // when/then
    assertThatThrownBy(() -> addressService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("All information are required.");
  }

  @Test
  void givenExistingAddress_whenSave_thenThrowIllegalArgumentException() {
    // given
    NewAddress newAddress =
        new NewAddress()
            .street("Test Street")
            .streetNumber("123")
            .postalCode("12345")
            .city("Test City");

    when(addressRepository.existsByAddressData(
            newAddress.getCity(),
            newAddress.getPostalCode(),
            newAddress.getStreet(),
            newAddress.getStreetNumber()))
        .thenReturn(true);

    // when and then
    assertThatThrownBy(() -> addressService.save(newAddress))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Address already exists.");
  }

  @Test
  void givenExistingAddresses_whenGetAll_thenReturnAllAddresses() {
    // given
    AddressesRecord address1 =
        new AddressesRecord()
            .setId(1L)
            .setStreet("Street 1")
            .setStreetNumber("123")
            .setPostalCode("12345")
            .setCity("City 1");

    AddressesRecord address2 =
        new AddressesRecord()
            .setId(2L)
            .setStreet("Street 2")
            .setStreetNumber("456")
            .setPostalCode("67890")
            .setCity("City 2");

    when(addressRepository.findAll()).thenReturn(Stream.of(address1, address2));

    // when
    ResponseEntity<List<Address>> response = addressService.getAll();

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody())
        .hasSize(2)
        .extracting("street", "streetNumber", "postalCode", "city")
        .containsExactly(
            tuple("Street 1", "123", "12345", "City 1"),
            tuple("Street 2", "456", "67890", "City 2"));
  }

  @Test
  void givenValidAddressId_whenGetById_thenReturnAddress() {
    // given
    long id = 1L;
    AddressesRecord record =
        new AddressesRecord()
            .setId(id)
            .setStreet("Test Street")
            .setStreetNumber("123")
            .setPostalCode("12345")
            .setCity("Test City");

    when(addressRepository.findById(id)).thenReturn(record);

    // when
    ResponseEntity<Address> response = addressService.getById(id);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody())
        .extracting("id", "street", "streetNumber", "postalCode", "city")
        .containsExactly(1L, "Test Street", "123", "12345", "Test City");
  }

  @Test
  void givenNonExistentAddressId_whenGetById_thenReturnNotFound() {
    // given
    long id = 999L;
    when(addressRepository.findById(id)).thenReturn(null);

    // when
    ResponseEntity<Address> response = addressService.getById(id);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void givenValidUpdateAddress_whenUpdate_thenReturnUpdatedAddress() {
    // given
    long id = 1L;
    UpdateAddress updateAddress =
        new UpdateAddress()
            .street("Updated Street")
            .streetNumber("999")
            .postalCode("54321")
            .city("Updated City");

    AddressesRecord updatedRecord =
        new AddressesRecord()
            .setId(id)
            .setStreet(updateAddress.getStreet())
            .setStreetNumber(updateAddress.getStreetNumber())
            .setPostalCode(updateAddress.getPostalCode())
            .setCity(updateAddress.getCity());

    when(addressRepository.exists(id)).thenReturn(true);
    when(addressRepository.update(id, updateAddress)).thenReturn(updatedRecord);

    // when
    ResponseEntity<Address> response = addressService.update(id, updateAddress);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody())
        .extracting("id", "street", "streetNumber", "postalCode", "city")
        .containsExactly(1L, "Updated Street", "999", "54321", "Updated City");
  }

  @Test
  void givenNonExistentId_whenUpdate_thenReturnNotFound() {
    // given
    long id = 999L;
    UpdateAddress updateAddress = new UpdateAddress().street("Updated Street");

    when(addressRepository.exists(id)).thenReturn(false);

    // when
    ResponseEntity<Address> response = addressService.update(id, updateAddress);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void givenExistingId_whenDelete_thenReturnNoContent() {
    // given
    long id = 1L;
    when(addressRepository.exists(id)).thenReturn(true);
    when(addressRepository.deleteAssociation(id)).thenReturn(1L);
    when(addressRepository.delete(id)).thenReturn(1L);

    // when
    ResponseEntity<Void> response = addressService.delete(id);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void givenNonExistentId_whenDelete_thenReturnNotFound() {
    // given
    long id = 999L;
    when(addressRepository.exists(id)).thenReturn(false);

    // when
    ResponseEntity<Void> response = addressService.delete(id);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
