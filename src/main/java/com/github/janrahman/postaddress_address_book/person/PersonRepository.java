package com.github.janrahman.postaddress_address_book.person;

import com.github.janrahman.postaddress_address_book.jooq.model.Tables;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.PersonsRecord;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Records;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

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
}
