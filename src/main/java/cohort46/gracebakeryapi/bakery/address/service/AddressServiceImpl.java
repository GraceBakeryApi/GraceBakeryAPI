package cohort46.gracebakeryapi.bakery.address.service;

//import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.bakery.address.controller.AddressController;
import cohort46.gracebakeryapi.bakery.address.dao.AddressRepository;
import cohort46.gracebakeryapi.bakery.address.dto.AddressDto;
import cohort46.gracebakeryapi.bakery.address.model.Address;
import cohort46.gracebakeryapi.bakery.order.service.OrderServiceImpl;
import cohort46.gracebakeryapi.exception.AddressNotFoundException;
import cohort46.gracebakeryapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final OrderServiceImpl orderServiceImpl;
    private AddressController addressController;

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public AddressDto addAddress(AddressDto addressDto) {
        Address address = modelMapper.map(addressDto, Address.class);
        UserAccount user = userRepository.findById(addressDto.getUserid()).orElseThrow(() -> new UserNotFoundException(addressDto.getUserid()));
        address.setUser(user);
        address.setId(null);
        address = addressRepository.save(address);
        if(address != null) {
            return modelMapper.map(address, AddressDto.class);
        }
        return null;
    }

    @Override
    public AddressDto findAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException(addressId));
        return modelMapper.map(address, AddressDto.class);
    }

    @Transactional
    @Override
    public AddressDto deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException(addressId));
        address.getUser().getAddresses().remove(address);
        userRepository.saveAndFlush(address.getUser());
        addressRepository.delete(address);
        addressRepository.flush();
        return modelMapper.map(address, AddressDto.class);
    }

    @Transactional
    @Override
    public AddressDto updateAddress(AddressDto addressDto, Long id) {
        addressDto.setId(id);
        Address address = addressRepository.findById(addressDto.getId()).orElseThrow(() -> new AddressNotFoundException(addressDto.getId()));
        modelMapper.map(addressDto, address);
        return modelMapper.map(addressRepository.save(address), AddressDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<AddressDto> findAddressesByUserId(Long userid) {
        return addressRepository.findAllByUserId(userid, Sort.by("id")).stream().map(a -> modelMapper.map(a, AddressDto.class)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<AddressDto> findAddressesByIncluding(String including) {
        List<Address> addresses = addressRepository.findAll().stream().toList();
        LinkedHashSet<AddressDto> addressDtos = new LinkedHashSet<>();
        for (Address address : addresses) {
            if( (address.getAddress().contains(including)) || (address.getCity().contains(including)) || (address.getStreet().contains(including)) ){
                addressDtos.add(modelMapper.map(address, AddressDto.class));
            }
        }
        return addressDtos;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<AddressDto> findAddressesByName(String including) {
        List<Address> addresses = addressRepository.findAll().stream().toList();
        LinkedHashSet<AddressDto> addressDtos = new LinkedHashSet<>();
        for (Address address : addresses) {
            if( (address.getUser().getFirstName().contains(including)) || (address.getUser().getLastName().contains(including)) || (address.getUser().getEmail().contains(including)) ){
                addressDtos.add(modelMapper.map(address, AddressDto.class));
            }
        }
        return addressDtos;
    }


    @Transactional(readOnly = true)
    @Override
    public Iterable<AddressDto> findAddressesAll(){
        return addressRepository.findAll().stream().map(a -> modelMapper.map(a, AddressDto.class)).toList();
    }

}