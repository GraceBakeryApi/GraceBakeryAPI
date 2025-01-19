package cohort46.gracebakeryapi.accounting.service;

import cohort46.gracebakeryapi.accounting.controller.UserController;
import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.dto.UserDto;
import cohort46.gracebakeryapi.accounting.model.User;
import cohort46.gracebakeryapi.bakery.order.dao.OrderRepository;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.section.dao.SectionRepository;
import cohort46.gracebakeryapi.exception.OrderNotFoundException;
import cohort46.gracebakeryapi.exception.ResourceNotFoundException;
import cohort46.gracebakeryapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final OrderRepository orderRepository;
    private UserController userController;

    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Transactional
    @Override
    public void changeLogin(String email) {

    }

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setId(null);
        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
        user = userRepository.save(user);
        if(user != null) {
            return modelMapper.map(user, UserDto.class);
        }
         //*/
        return null;
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(  userId    ));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        userDto.setId(id);
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(   userDto.getId()   ));
        modelMapper.map(userDto, user);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return modelMapper.map(userRepository.findUserByEmail(email), UserDto.class) ;
    }

    @Override
    public UserDto findUserByPhone(String phone) {
        return modelMapper.map(userRepository.findUserByPhone(phone), UserDto.class) ;
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

    //@Transactional(readOnly = true)


}