package com.github.janrahman.postaddress_address_book.address;

import com.github.janrahman.postaddress_address_book.openapi.api.AddressesApi;
import com.github.janrahman.postaddress_address_book.openapi.model.Address;
import com.github.janrahman.postaddress_address_book.openapi.model.NewAddress;
import com.github.janrahman.postaddress_address_book.openapi.model.UpdateAddress;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController implements AddressesApi {

  private final AddressServiceApi addressService;

  public AddressController(AddressServiceApi addressService) {
    this.addressService = addressService;
  }

  // TODO: add offset and limit params
  @Override
  public ResponseEntity<List<Address>> _addressesGet() {
    return addressService.getAll();
  }

  @Override
  public ResponseEntity<Void> _addressesIdDelete(Long id) {
    return addressService.delete(id);
  }

  @Override
  public ResponseEntity<Address> _addressesIdGet(Long id) {
    return addressService.getById(id);
  }

  @Override
  public ResponseEntity<Address> _addressesIdPut(Long id, UpdateAddress updateAddress) {
    return addressService.update(id, updateAddress);
  }

  @Override
  public ResponseEntity<Address> _addressesPost(NewAddress newAddress) {
    return addressService.create(newAddress);
  }
}
