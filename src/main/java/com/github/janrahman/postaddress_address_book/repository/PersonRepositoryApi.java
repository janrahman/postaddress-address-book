package com.github.janrahman.postaddress_address_book.repository;

import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.PersonsRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.AddPerson;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdatePersonInfo;
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

  boolean exists(long id);

  long saveAssociation(long id, long addressId);

  long deleteAssociation(long id);

  long delete(long id);

  PersonsRecord findById(long id);

  PersonsRecord update(long id, UpdatePersonInfo updatePersonInfo);

  PersonsRecord save(AddPerson addPerson);

  boolean existsByPersonInfo(String firstname, String name, LocalDate birthday, String value);
}
