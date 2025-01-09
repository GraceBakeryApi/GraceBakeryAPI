package cohort46.gracebakeryapi.bakery.address.service;

import cohort46.gracebakeryapi.bakery.address.dto.AddressDto;

public interface AddressService {
    AddressDto addAddress(AddressDto addressDto);//Long
    AddressDto findAddressById(Long addressId);
    AddressDto deleteAddress(Long id);
    AddressDto updateAddress(AddressDto addressDto, Long id);
    Iterable<AddressDto> findAddressesByIncluding(String including);Iterable<AddressDto> findAddressesByUserId(Long userid);
    Iterable<AddressDto> findAddressesByName(String including);
}
