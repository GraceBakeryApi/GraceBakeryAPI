package cohort46.gracebakeryapi.accounting.service;

import cohort46.gracebakeryapi.accounting.dto.UserDto;


public interface UserService {

    UserDto addUser(UserDto userDto);//Long
    UserDto findUserById(Long userId);
    UserDto updateUser(UserDto userDto, Long id);
    UserDto findUserByEmail(String email);
    UserDto findUserByPhone(String phone);
    UserDto findUserByOrderById(Long order_id);
    Iterable<UserDto> getUsersAll();
    Iterable<UserDto> findUserByBirthdate(Long birthdate);
    Iterable<UserDto> findUserByFirstName(String firstname);
    Iterable<UserDto> findUserByLastName(String lastname);
}