package cohort46.gracebakeryapi.accounting.controller;

import cohort46.gracebakeryapi.accounting.dto.UserDto;
import cohort46.gracebakeryapi.accounting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto)  ;
    }//Long

    @GetMapping("/user/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PutMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserPassword(Principal principal,  @RequestHeader("X-Password") String password) {
        //
    }

    @PatchMapping("/user/password")
    public UserDto updateUser( @RequestBody UserDto userDto, @PathVariable Long id) {
        return userService.updateUser(userDto, id);
    }

    @GetMapping("/users")
    public Iterable<UserDto> getUsersAll() {
        return userService.getUsersAll();
    }

    @GetMapping("/user/phone/{phone}")
    public UserDto findUserByPhone(@PathVariable String phone) {
        return userService.findUserByPhone(phone);
    }

    @GetMapping("/user/email/{email}")
    public UserDto findUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

    @GetMapping("/users/firstname/{firstname}")
    public Iterable<UserDto> findUsersByFirstName(@PathVariable String firstname) {
        return userService.findUserByFirstName(firstname);
    }

    @GetMapping("/users/lastname/{lastname}")
    public Iterable<UserDto> findUsersByLastName(@PathVariable String lastname) {
        return userService.findUserByLastName(lastname);
    }

    @GetMapping("/users/birthdate/{birthdate}")
    public Iterable<UserDto> findUserByBirthdate(@PathVariable Long birthdate) {
        return userService.findUserByBirthdate(birthdate);
    }

    @GetMapping("/user/order/{order_id}")
    public UserDto findUserByOrderById(@PathVariable Long order_id) {
        return userService.findUserByOrderById(order_id);
    }
}
