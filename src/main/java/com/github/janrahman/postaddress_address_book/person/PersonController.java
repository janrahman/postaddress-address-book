package com.github.janrahman.postaddress_address_book.person;

import com.github.janrahman.postaddress_address_book.openapi.api.PersonsApi;
import com.github.janrahman.postaddress_address_book.openapi.model.AddPerson;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.AvgAge;
import com.github.janrahman.postaddress_address_book.openapi.model.Person;
import com.github.janrahman.postaddress_address_book.openapi.model.PersonsIdAddressesPostRequest;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdatePersonInfo;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class PersonController implements PersonsApi {

  private final PersonServiceApi personService;

  public PersonController(PersonServiceApi personService) {
    this.personService = personService;
  }

  @Override
  public ResponseEntity<AvgAge> _personsAvgAgeGet(String postalCode) {
    return personService.getAverageAgeByPostalCode(postalCode);
  }

  @Override
  public ResponseEntity<List<Person>> _personsGet(
      String street, String streetNumber, String postalCode, String city) {
    return personService.getAll(street, streetNumber, postalCode, city);
  }

  @Override
  public ResponseEntity<List<Address>> _personsIdAddressesGet(Long id) {
    return personService.getAddresses(id);
  }


  @Override
  public ResponseEntity<Address> _personsIdAddressesPost(
      Long id, PersonsIdAddressesPostRequest personsIdAddressesPostRequest) {
    return personService.saveAddress(id, personsIdAddressesPostRequest);
  }

  @Override
  public ResponseEntity<Void> _personsIdDelete(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Person> _personsIdGet(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Person> _personsIdPut(Long id, UpdatePersonInfo updatePersonInfo) {
    return null;
  }

  @Override
  public ResponseEntity<Person> _personsPost(AddPerson addPerson) {
    return null;
  }
}
