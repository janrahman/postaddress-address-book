package com.github.janrahman.postaddress_address_book.service;

import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.NewAddress;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdateAddress;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AddressServiceApi {

  ResponseEntity<Void> delete(Long id);

  ResponseEntity<Address> getById(Long id);

  ResponseEntity<Address> update(Long id, UpdateAddress updateAddress);

  ResponseEntity<List<Address>> getAll();

  ResponseEntity<Address> save(NewAddress newAddress);
}
