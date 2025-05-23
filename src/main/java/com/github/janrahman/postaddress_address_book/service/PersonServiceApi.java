package com.github.janrahman.postaddress_address_book.service;

import com.github.janrahman.postaddress_address_book.openapi.model.AddPerson;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.AvgAge;
import com.github.janrahman.postaddress_address_book.openapi.model.Person;
import com.github.janrahman.postaddress_address_book.openapi.model.PersonsIdAddressesPostRequest;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdatePersonInfo;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface PersonServiceApi {

  ResponseEntity<List<Person>> getAll(
      String street, String streetNumber, String postalCode, String city);

  ResponseEntity<List<Address>> getAddresses(Long id);

  ResponseEntity<Address> saveAddress(Long id, PersonsIdAddressesPostRequest personAddress);

  ResponseEntity<AvgAge> getAverageAgeByPostalCode(String postalCode);

  ResponseEntity<Void> delete(Long id);

  ResponseEntity<Person> getById(Long id);

  ResponseEntity<Person> update(Long id, UpdatePersonInfo updatePersonInfo);

  ResponseEntity<Person> save(AddPerson addPerson);
}
