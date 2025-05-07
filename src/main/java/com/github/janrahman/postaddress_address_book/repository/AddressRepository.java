package com.github.janrahman.postaddress_address_book.repository;

import com.github.janrahman.postaddress_address_book.jooq.model.Tables;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.NewAddress;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdateAddress;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AddressRepository implements AddressRepositoryApi {

  private final DSLContext context;

  public AddressRepository(DSLContext context) {
    this.context = context;
  }

  @Transactional
  @Override
  public long delete(long id) {
    return context.deleteFrom(Tables.ADDRESSES).where(Tables.ADDRESSES.ID.eq(id)).execute();
  }

  @Transactional
  @Override
  public long deleteAssociation(long id) {
    return context
        .deleteFrom(Tables.PERSONS_HAVE_ADDRESSES)
        .where(Tables.PERSONS_HAVE_ADDRESSES.ADDRESS_ID.eq(id))
        .execute();
  }

  @Override
  public AddressesRecord findById(long id) {
    return context.selectFrom(Tables.ADDRESSES).where(Tables.ADDRESSES.ID.eq(id)).fetchOne();
  }

  @Transactional
  @Override
  public AddressesRecord update(long id, @NonNull UpdateAddress updateAddress) {
    Map<Field<?>, Object> updates =
        Stream.of(
                Pair.of(Tables.ADDRESSES.STREET, updateAddress.getStreet()),
                Pair.of(Tables.ADDRESSES.STREET_NUMBER, updateAddress.getStreetNumber()),
                Pair.of(Tables.ADDRESSES.POSTAL_CODE, updateAddress.getPostalCode()),
                Pair.of(Tables.ADDRESSES.CITY, updateAddress.getCity()))
            .filter(it -> Objects.nonNull(it.getRight()))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

    return context
        .update(Tables.ADDRESSES)
        .set(updates)
        .where(Tables.ADDRESSES.ID.eq(id))
        .returning()
        .fetchOne();
  }

  @Override
  public Stream<AddressesRecord> findAll() {
    return context.selectFrom(Tables.ADDRESSES).offset(0).limit(100).fetchStream();
  }

  @Transactional
  @Override
  public AddressesRecord save(@NonNull NewAddress newAddress) {
    return context
        .insertInto(Tables.ADDRESSES)
        .set(Tables.ADDRESSES.STREET, newAddress.getStreet())
        .set(Tables.ADDRESSES.STREET_NUMBER, newAddress.getStreetNumber())
        .set(Tables.ADDRESSES.POSTAL_CODE, newAddress.getPostalCode())
        .set(Tables.ADDRESSES.CITY, newAddress.getCity())
        .returning()
        .fetchOne();
  }

  @Override
  public boolean exists(long addressId) {
    return context.fetchExists(Tables.ADDRESSES, Tables.ADDRESSES.ID.eq(addressId));
  }

  @Override
  public boolean existsByAddressData(
      String city, String postalCode, String street, String streetNumber) {
    return context.fetchExists(
        Tables.ADDRESSES,
        Tables.ADDRESSES
            .STREET
            .eq(street)
            .and(Tables.ADDRESSES.STREET_NUMBER.eq(streetNumber))
            .and(Tables.ADDRESSES.POSTAL_CODE.eq(postalCode))
            .and(Tables.ADDRESSES.CITY.eq(city)));
  }
}
