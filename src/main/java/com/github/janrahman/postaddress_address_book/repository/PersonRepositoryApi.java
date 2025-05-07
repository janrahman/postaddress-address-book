package com.github.janrahman.postaddress_address_book.person;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.PersonsRecord;
import java.time.LocalDate;
import java.util.List;
import org.springframework.lang.NonNull;

public interface PersonRepositoryApi {

  @NonNull
  List<PersonsRecord> findAllByAddressData(
      String street, String streetNumber, String postalCode, String city);

  List<PersonsRecord> findAll();

  @NonNull
  List<AddressesRecord> findAddressesByPersonId(Long id);

  @NonNull
  List<LocalDate> findAllPersonBirthdaysByPostalCode(String postalCode);
}
