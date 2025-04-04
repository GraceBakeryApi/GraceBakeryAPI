package cohort46.gracebakeryapi.accounting.service;

import cohort46.gracebakeryapi.accounting.controller.UserController;
import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.dto.ChangePasswordDto;
import cohort46.gracebakeryapi.accounting.dto.UserDto;
import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.accounting.security.JWT.JwtUtil;
import cohort46.gracebakeryapi.accounting.security.UserDetailsImpl;
import cohort46.gracebakeryapi.bakery.order.dao.OrderRepository;
import cohort46.gracebakeryapi.bakery.order.model.Order;
//import cohort46.gracebakeryapi.bakery.order.service.OrderServiceImpl;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.exception.FailedDependencyException;
import cohort46.gracebakeryapi.exception.ForbiddenException;
import cohort46.gracebakeryapi.exception.OrderNotFoundException;
import cohort46.gracebakeryapi.exception.UserNotFoundException;
import cohort46.gracebakeryapi.helperclasses.GlobalVariables;
import cohort46.gracebakeryapi.helperclasses.OrdersStatusEnum;
import lombok.RequiredArgsConstructor;
//import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final OrderRepository orderRepository;
    //private final OrderServiceImpl orderServiceImpl;
    private UserController userController;

    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional
    @Override
    public UserDto userLogin(String login, String password) {
        UserAccount user = userRepository.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException(  "such login" )) ;
        if(passwordEncoder.matches(password, user.getPassword()) ) {
            UserDto userDto = modelMapper.map(user, UserDto.class) ;
            userDto.setToken(jwtUtil.createToken(new UserDetailsImpl(user)));
            return userDto;
        }
        else {
            throw new UserNotFoundException(  "such login" );
        }
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        /*
        RoleEnum role_temp = null;
        try {
            role_temp = RoleEnum.values()[userDto.getRole_Id().intValue()];

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new FailedDependencyException("error: invalid role specified")  ;
        }

         */
        userDto.setRole_Id((long) RoleEnum.USER.ordinal());
        if(checkSource(userDto)){
            UserAccount user = modelMapper.map(userDto, UserAccount.class);
            user.setId(null);
            user.setPassword(passwordEncoder.encode(userDto.getPassword())); //BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
            user = userRepository.saveAndFlush(user);
            if(user != null) {
                Order order = new Order();//создаем корзину для создаваемого user
                order.setUser(user);
                order.setOrderstatus(GlobalVariables.getStatusList().get(OrdersStatusEnum.Cart.ordinal()));
                user.getOrders().add(order);
                user.setCartId(orderRepository.saveAndFlush(order).getId());
                user = userRepository.saveAndFlush(user);

                UserDto tempDto = modelMapper.map(user, UserDto.class) ;
                tempDto.setToken(jwtUtil.createToken(new UserDetailsImpl(user)));
                return tempDto;
            }
        }
        //*/
        return null;
    }
    //BCryptPasswordEncoder()
    @Override
    public UserDto findUserById(Long userId) {
        UserAccount user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(  userId    ));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        UserAccount user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(   userDto.getId()   ));
        if( !user.getRole().equals(RoleEnum.ROOT) && checkSource(userDto)){
            userDto.setId(id);
            modelMapper.map(userDto, user);
            return modelMapper.map(userRepository.save(user), UserDto.class);
        }
        return null;
    }

    @Transactional
    @Override
    public UserDto updateMe(UserDto userDto, UserAccount user) {
        long id = user.getId();
        user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(   id   ));
        String password = user.getPassword();
        RoleEnum role = user.getRole();
        modelMapper.map(userDto, user);
        user.setPassword(password);
        user.setRole(role);
        UserDto tempDto = modelMapper.map(userRepository.save(user), UserDto.class);
        tempDto.setToken(jwtUtil.createToken(new UserDetailsImpl(user)));
        return tempDto;
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return modelMapper.map(userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(  "such email" )), UserDto.class) ;
    }

    @Override
    public UserDto findUserByPhone(String phone) {
        return modelMapper.map(userRepository.findUserByPhone(phone).orElseThrow(() -> new UserNotFoundException(  "such phone" )), UserDto.class) ;
    }

    @Override
    public UserDto findUserByLogin(String login) {
        //UserAccount user = userRepository.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException(  "such login" ));
        return modelMapper.map(userRepository.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException(  "such login" )), UserDto.class) ;
    }


    @Transactional(readOnly = true)
    @Override
    public Iterable<UserDto> getUsersAll() {
        //сортировка сначала по LastName потом FirstName
        return userRepository.findAll(Sort.by("lastName").and(Sort.by("firstName")))
                .stream().map(c -> modelMapper.map(c, UserDto.class)).toList() ;
    }

    @Override
    public UserDto findUserByOrderById(Long order_id) {
        Order order = orderRepository.findById(order_id).orElseThrow(() -> new OrderNotFoundException(order_id));
        return modelMapper.map(order.getUser(), UserDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<UserDto> findUserByBirthdate(Long birthdate) {
        return userRepository.findUserByBirthdate(birthdate, Sort.by("lastName").and(Sort.by("firstName")))
                .map(c -> modelMapper.map(c, UserDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<UserDto> findUserByFirstName(String firstname) {
        return userRepository.findUserByFirstName(firstname, Sort.by("lastName").and(Sort.by("firstName")))
                .map(c -> modelMapper.map(c, UserDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<UserDto> findUserByLastName(String lastname) {
        return userRepository.findUserByLastName(lastname, Sort.by("lastName").and(Sort.by("firstName")))
                .map(c -> modelMapper.map(c, UserDto.class)).toList() ;
    }

    @Transactional(readOnly = true)
    @Override
    public  List<UserDto> findUsersByRole(String role)
    {
        RoleEnum roleEnum;
        try {
            roleEnum = RoleEnum.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("bad status");
        }
        List<UserAccount> user = userRepository.findAll().stream().filter(u -> u.getRole().equals(roleEnum)).toList();
        List<UserDto> userDtos = user.stream().map(c -> modelMapper.map(c, UserDto.class)).toList();
        return userRepository.findAll().stream().filter(u -> u.getRole().equals(roleEnum)).map(c -> modelMapper.map(c, UserDto.class)).toList() ;
    }

    @Transactional
    @Override
    public void changeUserPassword(Long id, String password){
        //userRepository.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException(  "login " + login )).setPassword(passwordEncoder.encode(password));
        UserAccount user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException( id ));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void changeMePassword(UserAccount user, ChangePasswordDto changePasswordDto){
        long id = user.getId();
        user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(   id   ));

        if(passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword()) ) {
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
        }
        else {
            //throw new UserNotFoundException(  "connect error" );
        }

    }



    private String[] getDetails(String autorization){
        String token = autorization.split(" ")[1];
        token = new String(Base64.getDecoder().decode(token));
        return token.split(":");
    }

    private boolean checkSource (UserDto userDto) {
        if( !(
                (userDto.getRole_Id() == (long) RoleEnum.GUEST.ordinal()) ||
                        (userDto.getRole_Id() == (long) RoleEnum.USER.ordinal()) ||
                        (userDto.getRole_Id() == (long) RoleEnum.ADMIN.ordinal()) ||
                        (userDto.getRole_Id() == (long) RoleEnum.BLOCKED.ordinal())
        ) ){
            throw new FailedDependencyException("error ");
        }

        if(userRepository.findUserByLogin(userDto.getLogin()).isPresent())  {
            throw new FailedDependencyException("Login must be uniq ") ;}

        if(userRepository.findUserByEmail(userDto.getEmail()).isPresent())  {
            throw new FailedDependencyException("email must be uniq ") ;}

        return true;
    }

}