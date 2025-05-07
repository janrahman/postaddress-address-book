package com.github.janrahman.postaddress_address_book.repository;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.NewAddress;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdateAddress;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public interface AddressRepositoryApi {

  long delete(long id);

  long deleteAssociation(long id);

  AddressesRecord findById(long it);

  AddressesRecord update(long id, @NonNull UpdateAddress updateAddress);

  Stream<AddressesRecord> findAll();

  AddressesRecord save(@NonNull NewAddress newAddress);

  boolean exists(long addressId);

  boolean existsByAddressData(String city, String postalCode, String street, String streetNumber);
}
