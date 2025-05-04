package com.github.janrahman.postaddress_address_book.address;

import com.github.janrahman.postaddress_address_book.jooq.model.Tables;
import com.github.janrahman.postaddress_address_book.jooq.model.tables.records.AddressesRecord;
import com.github.janrahman.postaddress_address_book.openapi.model.NewAddress;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdateAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class AddressRepository implements AddressRepositoryApi {

  private final DSLContext context;

  public AddressRepository(DSLContext context) {
    this.context = context;
  }

  @Override
  public long delete(long id) {
    return context.deleteFrom(Tables.ADDRESSES).where(Tables.ADDRESSES.ID.eq(id)).execute();
  }

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

  @Override
  public AddressesRecord update(long id, @NonNull UpdateAddress updateAddress) {
    Map<Field<?>, Object> updates = new HashMap<>();

    if (Objects.nonNull(updateAddress.getStreet())) {
      updates.put(Tables.ADDRESSES.STREET, updateAddress.getStreet());
    }

    if (Objects.nonNull(updateAddress.getStreetNumber())) {
      updates.put(Tables.ADDRESSES.STREET_NUMBER, updateAddress.getStreetNumber());
    }

    if (Objects.nonNull(updateAddress.getPostalCode())) {
      updates.put(Tables.ADDRESSES.POSTAL_CODE, updateAddress.getPostalCode());
    }

    if (Objects.nonNull(updateAddress.getCity())) {
      updates.put(Tables.ADDRESSES.CITY, updateAddress.getCity());
    }

    return context
        .update(Tables.ADDRESSES)
        .set(updates)
        .where(Tables.ADDRESSES.ID.eq(id))
        .returning()
        .fetchOne();
  }

  @Override
  public Stream<AddressesRecord> findAll() {
    return context.selectFrom(Tables.ADDRESSES)
            .offset(0)
            .limit(100)
            .fetchStream();
  }

  @Override
  public AddressesRecord create(@NonNull NewAddress newAddress) {
    return context.insertInto(Tables.ADDRESSES)
            .set(Tables.ADDRESSES.STREET, newAddress.getStreet())
            .set(Tables.ADDRESSES.STREET_NUMBER, newAddress.getStreetNumber())
            .set(Tables.ADDRESSES.POSTAL_CODE, newAddress.getPostalCode())
            .set(Tables.ADDRESSES.CITY, newAddress.getCity())
            .returning()
            .fetchOne();
  }
}
