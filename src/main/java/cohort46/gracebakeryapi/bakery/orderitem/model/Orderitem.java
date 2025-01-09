package cohort46.gracebakeryapi.bakery.orderitem.model;

import cohort46.gracebakeryapi.bakery.bakeryoptional.model.Bakeryoptional;
import cohort46.gracebakeryapi.bakery.ingredient.model.Ingredient;
import cohort46.gracebakeryapi.bakery.order.model.Order;
import cohort46.gracebakeryapi.bakery.product.model.Product;
import cohort46.gracebakeryapi.bakery.productsize.model.Productsize;
import cohort46.gracebakeryapi.bakery.size.model.Size;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.repository.core.support.PropertiesBasedNamedQueries;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Orderitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //@Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Bakeryoptional> bakeryoptionals = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Size size;

    @ManyToOne(cascade = CascadeType.ALL)
    private Ingredient ingredient;

    private Integer quantity;

    private Double cost;

    private String comment;
}





//{orderitem_id* , order_id, product_id, [option_id,...],  size_id, ingridient_id, quantity, cost}
