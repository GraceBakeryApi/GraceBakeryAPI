package cohort46.gracebakeryapi.order.order.model;

import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.order.address.model.Address;
import cohort46.gracebakeryapi.order.orderitem.model.Orderitem;
//import cohort46.gracebakeryapi.helperclasses.OrderStatus;
import cohort46.gracebakeryapi.order.orderstatus.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private UserAccount user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Orderitem> orderitems = new HashSet<>();

    private Double total;
    private Long date;
    private Long creatingdate;

    @ManyToOne
    private Address address;

    @Embedded
    private OrderStatus orderstatus;

    private Double final_sum;

    private String comment;
}

//{order_id*, user_id, [orderitem_id, ...], total, date*,  delievery, orderstatus_id, final_sum}
