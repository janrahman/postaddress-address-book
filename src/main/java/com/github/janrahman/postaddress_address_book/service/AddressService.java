package com.github.janrahman.postaddress_address_book.service;

import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.NewAddress;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdateAddress;
import com.github.janrahman.postaddress_address_book.repository.AddressRepositoryApi;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class AddressService implements AddressServiceApi {

  private final AddressRepositoryApi addressRepositoryApi;

  private final JooqRecordToDTOMapper jooqRecordToDTOMapper;

  public AddressService(AddressRepositoryApi addressRepositoryApi) {
    this.addressRepositoryApi = addressRepositoryApi;
    jooqRecordToDTOMapper = new JooqRecordToDTOMapper();
  }

  @Override
  public ResponseEntity<Void> delete(Long id) {
    return Optional.ofNullable(id)
        .map(this::removeAddressesAndAssociation)
        .filter(it -> it > 0)
        .map(ignore -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Override
  public ResponseEntity<Address> getById(Long id) {
    return Optional.ofNullable(id)
        .map(addressRepositoryApi::findById)
        .map(jooqRecordToDTOMapper::toAddress)
        .map(ResponseEntity::ok)
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Override
  public ResponseEntity<Address> update(Long id, UpdateAddress updateAddress) {
    if (Objects.isNull(updateAddress)
        || Stream.of(
                updateAddress.getCity(),
                updateAddress.getPostalCode(),
                updateAddress.getStreet(),
                updateAddress.getStreetNumber())
            .allMatch(Objects::isNull)) {
      throw new IllegalArgumentException("No fields to update.");
    }

    // TODO: throw ResourceNotFoundException instead
    return Optional.ofNullable(id)
        .map(it -> addressRepositoryApi.update(it, updateAddress))
        .map(jooqRecordToDTOMapper::toAddress)
        .map(ResponseEntity::ok)
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Override
  public ResponseEntity<List<Address>> getAll() {
    List<Address> addresses =
        addressRepositoryApi.findAll().map(jooqRecordToDTOMapper::toAddress).toList();
    return ResponseEntity.ok(addresses);
  }

  @Override
  public ResponseEntity<Address> create(NewAddress newAddress) {
    if (Objects.isNull(newAddress)
        || Stream.of(
                newAddress.getCity(),
                newAddress.getPostalCode(),
                newAddress.getStreet(),
                newAddress.getStreetNumber())
            .anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("All information are required.");
    }

    Address stored =
        Optional.ofNullable(addressRepositoryApi.save(newAddress))
            .map(jooqRecordToDTOMapper::toAddress)
            .orElseThrow(() -> new IllegalStateException("Cannot create new address."));
    URI newAddressUri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(stored.getId())
            .toUri();
    return ResponseEntity.created(newAddressUri).body(stored);
  }

  private long removeAddressesAndAssociation(long id) {
    return addressRepositoryApi.deleteAssociation(id) + addressRepositoryApi.delete(id);
  }
}
