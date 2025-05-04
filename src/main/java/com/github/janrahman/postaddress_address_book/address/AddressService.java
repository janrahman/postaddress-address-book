package com.github.janrahman.postaddress_address_book.address;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdateAddress;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  private final AddressRepositoryApi addressRepositoryApi;

  public AddressService(AddressRepositoryApi addressRepositoryApi) {
    this.addressRepositoryApi = addressRepositoryApi;
  }

  public ResponseEntity<Void> delete(Long id) {
    return Optional.ofNullable(id)
        .map(this::removeAddressesAndAssociation)
        .filter(it -> it > 0)
        .map(ignore -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  public ResponseEntity<Address> getById(Long id) {
    return Optional.ofNullable(id)
            .map(addressRepositoryApi::findById)
            .map(this::toAddress)
            .map(ResponseEntity::ok)
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  public ResponseEntity<Address> update(Long id, UpdateAddress updateAddress) {
    return Optional.ofNullable(id)
            .map(it -> addressRepositoryApi.update(it, updateAddress))
            .map(this::toAddress)
            .map(ResponseEntity::ok)
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  private long removeAddressesAndAssociation(long id) {
    return addressRepositoryApi.deleteAssociation(id) + addressRepositoryApi.delete(id);
  }

  private Address toAddress(@NonNull AddressesRecord record) {
    Address address = new Address();

    address.setId(record.getId());
    address.setStreet(record.getStreet());
    address.setStreetNumber(record.getStreetNumber());
    address.setPostalCode(record.getPostalCode());
    address.setCity(record.getCity());

    return address;
  }
}
