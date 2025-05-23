package com.github.janrahman.postaddress_address_book.repository;

import com.github.janrahman.postaddress_address_book.jooq.model.Tables;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.PersonsRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.AddPerson;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdatePersonInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Records;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PersonRepository implements PersonRepositoryApi {

  private final DSLContext context;

  public PersonRepository(DSLContext context) {
    this.context = context;
  }

  @Override
  public @NonNull List<PersonsRecord> findAllByAddressData(
      String street, String streetNumber, String postalCode, String city) {
    List<Condition> filterCriteria =
        Stream.of(
                Pair.of(Tables.ADDRESSES.STREET, street),
                Pair.of(Tables.ADDRESSES.STREET_NUMBER, streetNumber),
                Pair.of(Tables.ADDRESSES.POSTAL_CODE, postalCode),
                Pair.of(Tables.ADDRESSES.CITY, city))
            .filter(it -> Objects.nonNull(it.getRight()))
            .map(it -> it.getLeft().eq(it.getRight()))
            .toList();
    return context
        .select(Tables.PERSONS)
        .from(Tables.PERSONS)
        .join(Tables.PERSONS_HAVE_ADDRESSES)
        .on(Tables.PERSONS.ID.eq(Tables.PERSONS_HAVE_ADDRESSES.PERSON_ID))
        .join(Tables.ADDRESSES)
        .on(Tables.ADDRESSES.ID.eq(Tables.PERSONS_HAVE_ADDRESSES.ADDRESS_ID))
        .where(filterCriteria)
        .fetch(Records.mapping(it -> it));
  }

  @Override
  public List<PersonsRecord> findAll() {
    return context.selectFrom(Tables.PERSONS).fetch();
  }

  @Override
  public @NonNull List<AddressesRecord> findAddressesByPersonId(Long id) {
    return context
        .select(Tables.ADDRESSES)
        .from(Tables.ADDRESSES)
        .join(Tables.PERSONS_HAVE_ADDRESSES)
        .on(Tables.ADDRESSES.ID.eq(Tables.PERSONS_HAVE_ADDRESSES.ADDRESS_ID))
        .where(Tables.PERSONS_HAVE_ADDRESSES.PERSON_ID.eq(id))
        .fetch(Records.mapping(it -> it));
  }

  @Override
  public @NonNull List<LocalDate> findAllPersonBirthdaysByPostalCode(String postalCode) {
    return context
        .select(Tables.PERSONS.BIRTHDAY)
        .from(Tables.PERSONS)
        .join(Tables.PERSONS_HAVE_ADDRESSES)
        .on(Tables.PERSONS.ID.eq(Tables.PERSONS_HAVE_ADDRESSES.PERSON_ID))
        .join(Tables.ADDRESSES)
        .on(Tables.ADDRESSES.ID.eq(Tables.PERSONS_HAVE_ADDRESSES.ADDRESS_ID))
        .where(Tables.ADDRESSES.POSTAL_CODE.eq(postalCode))
        .fetch(Records.mapping(it -> it));
  }

  @Override
  public boolean exists(long id) {
    return context.fetchExists(Tables.PERSONS, Tables.PERSONS.ID.eq(id));
  }

  @Override
  public long saveAssociation(long personId, long addressId) {
    return context
        .insertInto(Tables.PERSONS_HAVE_ADDRESSES)
        .set(Tables.PERSONS_HAVE_ADDRESSES.PERSON_ID, personId)
        .set(Tables.PERSONS_HAVE_ADDRESSES.ADDRESS_ID, addressId)
        .execute();
  }

  @Transactional
  @Override
  public long deleteAssociation(long id) {
    return context
        .deleteFrom(Tables.PERSONS_HAVE_ADDRESSES)
        .where(Tables.PERSONS_HAVE_ADDRESSES.PERSON_ID.eq(id))
        .execute();
  }

  @Transactional
  @Override
  public long delete(long id) {
    return context.deleteFrom(Tables.PERSONS).where(Tables.PERSONS.ID.eq(id)).execute();
  }

  @Override
  public PersonsRecord findById(long id) {
    return context.selectFrom(Tables.PERSONS).where(Tables.PERSONS.ID.eq(id)).fetchOne();
  }

  @Override
  public PersonsRecord update(long id, UpdatePersonInfo updatePersonInfo) {
    Map<Field<?>, Object> updates =
        Stream.of(
                Pair.of(Tables.PERSONS.FIRSTNAME, updatePersonInfo.getFirstname()),
                Pair.of(Tables.PERSONS.NAME, updatePersonInfo.getName()),
                Pair.of(Tables.PERSONS.BIRTHDAY, updatePersonInfo.getBirthday()),
                Pair.of(
                    Tables.PERSONS.GENDER,
                    Objects.requireNonNull(updatePersonInfo.getGender()).getValue()))
            .filter(it -> Objects.nonNull(it.getRight()))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

    return context
        .update(Tables.PERSONS)
        .set(updates)
        .where(Tables.PERSONS.ID.eq(id))
        .returning()
        .fetchOne();
  }

  @Override
  public PersonsRecord save(AddPerson addPerson) {
    return context
        .insertInto(Tables.PERSONS)
        .set(Tables.PERSONS.FIRSTNAME, addPerson.getFirstname())
        .set(Tables.PERSONS.NAME, addPerson.getName())
        .set(Tables.PERSONS.BIRTHDAY, addPerson.getBirthday())
        .set(Tables.PERSONS.GENDER, Objects.requireNonNull(addPerson.getGender()).getValue())
        .returning()
        .fetchOne();
  }

  @Override
  public boolean existsByPersonInfo(
      String firstname, String name, LocalDate birthday, String value) {
    return context.fetchExists(
        Tables.PERSONS,
        Tables.PERSONS
            .FIRSTNAME
            .eq(firstname)
            .and(Tables.PERSONS.NAME.eq(name))
            .and(Tables.PERSONS.BIRTHDAY.eq(birthday))
            .and(Tables.PERSONS.GENDER.eq(value)));
  }
}
