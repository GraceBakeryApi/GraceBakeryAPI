package cohort46.gracebakeryapi.accounting.model;

import cohort46.gracebakeryapi.order.address.model.Address;
import cohort46.gracebakeryapi.order.order.model.Order;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@Entity
@Table(name = "user")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    RoleEnum role;
     @Column(unique =true)
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique =true)
    private String email;

    private String phone;

    private long birthdate;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Address> addresses = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    private Long cartId;
}

