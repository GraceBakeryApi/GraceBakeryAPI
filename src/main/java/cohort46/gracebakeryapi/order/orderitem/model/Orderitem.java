package cohort46.gracebakeryapi.order.orderitem.model;

import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import cohort46.gracebakeryapi.order.order.model.Order;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Orderitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    @ManyToMany
    private Set<Bakeryoptional> bakeryoptionals = new HashSet<>();

    @ManyToOne
    private Size size;

    @ManyToOne
    private Ingredient ingredient;

    private Integer quantity;

    private Double cost;

    private String comment;
}





//{orderitem_id* , order_id, product_id, [option_id,...],  size_id, ingridient_id, quantity, cost}
