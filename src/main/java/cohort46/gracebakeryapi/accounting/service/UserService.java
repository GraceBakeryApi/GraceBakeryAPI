package cohort46.gracebakeryapi.accounting.service;

import cohort46.gracebakeryapi.accounting.dto.ChangePasswordDto;
import cohort46.gracebakeryapi.accounting.dto.UserDto;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.security.Principal;
import java.util.List;


public interface UserService {
    UserDto userLogin(String login, String password);
    UserDto addUser(UserDto userDto);//Long
    UserDto findUserById(Long userId);
    UserDto updateUser(UserDto userDto, Long id);
    UserDto updateMe(UserDto userDto, UserAccount user);
    UserDto findUserByEmail(String email);
    UserDto findUserByPhone(String phone);
    UserDto findUserByOrderById(Long order_id);
    void changeUserPassword(Long id, String password);
    void changeMePassword(UserAccount user, ChangePasswordDto changePasswordDto);
    UserDto findUserByLogin(String login);
    Iterable<UserDto> getUsersAll();
    Iterable<UserDto> findUserByBirthdate(Long birthdate);
    Iterable<UserDto> findUserByFirstName(String firstname);
    Iterable<UserDto> findUserByLastName(String lastname);
    List<UserDto> findUsersByRole(String role);
}