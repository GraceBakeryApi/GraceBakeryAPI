package cohort46.gracebakeryapi.accounting.controller;

import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.dto.AuthorizationDto;
import cohort46.gracebakeryapi.accounting.dto.ChangePasswordDto;
import cohort46.gracebakeryapi.accounting.dto.UserDto;
//import cohort46.gracebakeryapi.accounting.security.JWT.JwtUtil;
import cohort46.gracebakeryapi.accounting.security.JWT.JwtUtil;
import cohort46.gracebakeryapi.accounting.security.UserDetailsImpl;
import cohort46.gracebakeryapi.accounting.service.UserService;
import cohort46.gracebakeryapi.exception.UserNotFoundException;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")//@PreAuthorize("isAuthenticated() and !authentication.principal.isBlocked")
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //private final JwtUtil jwtUtil;
////////////////permit_all//////////////////
    @PostMapping("/user/reg")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto)  ;
    }

    @PostMapping("/user/login")
    public UserDto findUserByLogin(@RequestBody AuthorizationDto authorizationDto)//(@AuthenticationPrincipal UserDetailsImpl principal)
    {
        //principal = new UserDetailsImpl(userRepository.findUserByLogin(authorizationDto.getLogin()).orElseThrow(() -> new UserNotFoundException(  "such login" ))) ;
        return userService.userLogin(authorizationDto.getLogin(), authorizationDto.getPassword());
    }

    @GetMapping("/user/guest")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addGuest() {
        return null;//userService.addGuest()  ;
    }

    ////////////////user//////////////////

    @PutMapping("/me/password")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeMePassword(@AuthenticationPrincipal UserDetailsImpl principal,  @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changeMePassword(principal.getUser() , changePasswordDto);
    }

    @GetMapping("/me")
    public UserDto getMe(@AuthenticationPrincipal UserDetailsImpl principal) {
        return  userService.findUserById(principal.getId());
        //return modelMapper.map(principal.getUser() , UserDto.class);
    }

    @PatchMapping("/me")
    public UserDto updateMe( @AuthenticationPrincipal UserDetailsImpl principal, @RequestBody UserDto userDto) {
        return userService.updateMe(userDto, principal.getUser());
    }

    ////////////////user self or ADMIN//////////////////

    //@PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")


    ////////////////ADMIN//////////////////
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }


    @PatchMapping("/user/{id}")
    public UserDto updateUser( @RequestBody UserDto userDto, @PathVariable Long id) {
        return userService.updateUser(userDto, id);
    }

    @PutMapping("/user/password/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeUserPassword( @PathVariable Long id, @RequestBody AuthorizationDto authorizationDto) {
        userService.changeUserPassword(id , authorizationDto.getPassword());
    }


    @GetMapping("/users")
    public Iterable<UserDto> getUsersAll() {
        return userService.getUsersAll();
    }

    @GetMapping("/users/{role}")
    public Iterable<UserDto> getUsersByRole(@PathVariable String role) {
        return userService.findUsersByRole(role);
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
