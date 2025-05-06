package com.github.janrahman.postaddress_address_book.person;

import com.github.janrahman.postaddress_address_book.openapi.model.Person;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface PersonServiceApi {

  ResponseEntity<List<Person>> getAll(
      String street, String streetNumber, String postalCode, String city);
}
