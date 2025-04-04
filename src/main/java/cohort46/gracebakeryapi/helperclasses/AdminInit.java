package cohort46.gracebakeryapi.helperclasses;


import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.accounting.service.UserService;
import cohort46.gracebakeryapi.bakery.order.dao.OrderRepository;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AdminInit {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    public AdminInit(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;


        if(this.userService.findUsersByRole("ROOT").isEmpty()){
            UserAccount rootAccount = new UserAccount();
            rootAccount.setId(null);
            rootAccount.setRole(RoleEnum.ROOT);
            rootAccount.setPassword(passwordEncoder.encode("RootPassword123"));
            rootAccount.setLogin("root");
            rootAccount.setBirthdate(System.currentTimeMillis());
            this.userRepository.saveAndFlush(rootAccount);
                    /*
            this.userRepository.saveAndFlush(
                    UserAccount.builder()
                            .id(null)
                            .role(RoleEnum.ROOT)
                            .login("root")
                            .password(this.passwordEncoder.encode("RootPassword123"))
                            .firstName(null)
                            .lastName(null)
                            .email(null)
                            .phone(null)
                            .birthdate(System.currentTimeMillis())
                            .addresses(null)
                            .orders(null)
                            .cartId(null)
                            .build()
            );

                     */
        }


        if(this.userService.findUsersByRole("ADMIN").isEmpty()){
            UserAccount admin = new UserAccount();
            admin.setId(null);
            admin.setRole(RoleEnum.ADMIN);
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin");
            admin.setPhone("admin");
            admin.setBirthdate(System.currentTimeMillis());
            admin.setAddresses(null);
            admin.setOrders(null);
            admin.setCartId(null);
            admin = this.userRepository.saveAndFlush(admin);
                    /*
                    UserAccount.builder()
                            .id(null)
                            .role(RoleEnum.ADMIN)
                            .login("admin")
                            .password(this.passwordEncoder.encode("admin"))
                            .firstName("admin")
                            .lastName("admin")
                            .email("admin")
                            .phone("admin")
                            .birthdate(System.currentTimeMillis())
                            .addresses(null)
                            .orders(null)
                            .cartId(null)
                            .build()
            );

                     */
            Order adminsCart = new Order();
            adminsCart.setId(null);
            adminsCart.setUser(admin);
            this.orderRepository.saveAndFlush(adminsCart) ;
            admin.setCartId(adminsCart.getId());
            this.userRepository.saveAndFlush(admin);
        }
    }
}
